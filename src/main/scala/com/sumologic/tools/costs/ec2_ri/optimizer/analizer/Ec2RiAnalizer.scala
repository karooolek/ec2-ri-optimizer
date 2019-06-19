package com.sumologic.tools.costs.ec2_ri.optimizer.analizer

import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstancesSummary
import com.sumologic.tools.costs.ec2_ri.optimizer.running.RunningInstancesSummary

import scala.collection.mutable.ListBuffer

class Ec2RiAnalizer(runningInstancesSummary: RunningInstancesSummary, reservedInstancesSummary: ReservedInstancesSummary) {
  def analize() = {
    val allFamilies = runningInstancesSummary.runningFamiliesSizes.keys ++ reservedInstancesSummary.reservedFamiliesSizes.keys

    val familiesSizeDiffs = allFamilies.map(family => (
      family, {
      val runningSize = runningInstancesSummary.runningFamiliesSizes.getOrElse(family, 0.0)

      val reservedFamilySummary = reservedInstancesSummary.reservedFamiliesSizes.getOrElse(family, null)
      val reservedSize = if (reservedFamilySummary == null) 0.0 else reservedFamilySummary.size
      val reservedConvertibleSize = if (reservedFamilySummary == null) 0.0 else reservedFamilySummary.convertibleSize

      Ec2RiSizeDiff(runningSize, reservedSize, reservedConvertibleSize)
    }
    )).toMap

    val overReservedFamiliesPrices = familiesSizeDiffs.filter({
      case (family: String, ec2riSizeDiff: Ec2RiSizeDiff) => ec2riSizeDiff.reserved > ec2riSizeDiff.running
    }).map({
      case (family: String, ec2riSizeDiff: Ec2RiSizeDiff) => (family, ec2riSizeDiff.reserved - ec2riSizeDiff.running)
    })

    val underReservedFamiliesPrices = familiesSizeDiffs.filter({
      case (family: String, ec2riSizeDiff: Ec2RiSizeDiff) => ec2riSizeDiff.reserved < ec2riSizeDiff.running
    }).map({
      case (family: String, ec2riSizeDiff: Ec2RiSizeDiff) => (family, ec2riSizeDiff.running - ec2riSizeDiff.reserved)
    })

    val totalOverReservedFamiliesPrice = overReservedFamiliesPrices.values.sum
    val totalUnderReservedFamiliesPrice = underReservedFamiliesPrices.values.sum
    val totalReservationPrice = totalOverReservedFamiliesPrice - totalUnderReservedFamiliesPrice

    val possibleInputs = ListBuffer[(String, Double)]()
    possibleInputs.appendAll(overReservedFamiliesPrices.filter({
      case (family: String, overSize: Double) => {
        familiesSizeDiffs.getOrElse(family, null) != null && familiesSizeDiffs.getOrElse(family, null).reservedConvertibleSize > 0.0
      }
    }).toSeq.sortWith(_._2 > _._2))

    val possibleOutputs = ListBuffer[(String, Double)]()
    possibleOutputs.appendAll(underReservedFamiliesPrices.filter({
      case (family: String, underSize: Double) => {
        familiesSizeDiffs.getOrElse(family, null) != null && familiesSizeDiffs.getOrElse(family, null).reservedConvertibleSize > 0.0
      }
    }).toSeq.sortWith(_._2 > _._2))

    val suggestedConversions = ListBuffer[Ec2RiConversion]()
    while (!possibleInputs.isEmpty && !possibleOutputs.isEmpty) {
      var possibleInput = possibleInputs(0)
      val possibleInputFamily = possibleInput._1
      val possibleInputSize = possibleInput._2

      var possibleOutput = possibleOutputs(0)
      val possibleOutputFamily = possibleOutput._1
      val possibleOutputSize = possibleOutput._2

      if (possibleInputSize < possibleOutputSize) {
        suggestedConversions.append(Ec2RiConversion(
          possibleInputs.remove(0),
          (possibleOutputFamily, possibleInputSize)
        ))

        possibleOutputs.remove(0)
        possibleOutputs.prepend((possibleOutputFamily, possibleOutputSize + possibleInputSize))
        possibleOutputs.sortWith(_._2 > _._2)
      } else if (possibleInputSize > possibleOutputSize) {
        suggestedConversions.append(Ec2RiConversion(
          (possibleInputFamily, possibleOutputSize),
          possibleOutputs.remove(0)
        ))

        possibleInputs.remove(0)
        possibleInputs.prepend((possibleInputFamily, possibleInputSize - possibleOutputSize))
        possibleInputs.sortWith(_._2 > _._2)
      } else {
        suggestedConversions.append(Ec2RiConversion(
          possibleInputs.remove(0),
          possibleOutputs.remove(0)
        ))
      }

    }

    Ec2RiAnalysis(
      familiesSizeDiffs,
      overReservedFamiliesPrices,
      underReservedFamiliesPrices,
      totalOverReservedFamiliesPrice,
      totalUnderReservedFamiliesPrice,
      totalReservationPrice,
      suggestedConversions.toSeq
    )
  }

}
