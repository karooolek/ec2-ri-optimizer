package com.sumologic.tools.costs.ec2_ri.optimizer.analizer

import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstancesSummary
import com.sumologic.tools.costs.ec2_ri.optimizer.running.RunningInstancesSummary
import com.sumologic.tools.costs.ec2_ri.optimizer.utils.AwsFamilyPriceNormalizator

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

    Ec2RiAnalysis(
      familiesSizeDiffs,
      overReservedFamiliesPrices,
      underReservedFamiliesPrices,
      totalOverReservedFamiliesPrice,
      totalUnderReservedFamiliesPrice
    )
  }

}
