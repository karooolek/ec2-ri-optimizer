package com.sumologic.tools.costs.ec2_ri.optimizer.analizer

import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstancesSummary
import com.sumologic.tools.costs.ec2_ri.optimizer.running.RunningInstancesSummary
import com.sumologic.tools.costs.ec2_ri.optimizer.utils.AwsFamilyPriceNormalizator

import scala.collection.mutable

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
      case (family: String, ec2riSizeDiff: Ec2RiSizeDiff) => (family, AwsFamilyPriceNormalizator.normalize(family) * (ec2riSizeDiff.reserved - ec2riSizeDiff.running))
    })

    val underReservedFamiliesPrices = familiesSizeDiffs.filter({
      case (family: String, ec2riSizeDiff: Ec2RiSizeDiff) => ec2riSizeDiff.reserved < ec2riSizeDiff.running
    }).map({
      case (family: String, ec2riSizeDiff: Ec2RiSizeDiff) => (family, AwsFamilyPriceNormalizator.normalize(family) * (ec2riSizeDiff.reserved - ec2riSizeDiff.running))
    })

    val totalOverReservedFamiliesPrice = overReservedFamiliesPrices.values.sum
    val totalUnderReservedFamiliesPrice = underReservedFamiliesPrices.values.sum
    val totalReservationPrice = totalOverReservedFamiliesPrice + totalUnderReservedFamiliesPrice

    val possibleInputs = overReservedFamiliesPrices.filter({
      case (family: String, overSize: Double) => {
        familiesSizeDiffs.getOrElse(family, null) != null && familiesSizeDiffs.getOrElse(family, null).reservedConvertibleSize > 0.0
      }
    })

    val possibleOutputs = underReservedFamiliesPrices.filter({
      case (family: String, underSize: Double) => {
        familiesSizeDiffs.getOrElse(family, null) != null && familiesSizeDiffs.getOrElse(family, null).reservedConvertibleSize > 0.0
      }
    })

    val suggestedConversions = mutable.Seq[Ec2RiConversion]()
    // TODO find possible convsersions
//    while (possibleInputs.values.sum > 0.0 && possibleOutputs.values.sum < 0.0) {
//
//    }

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
