package com.sumologic.tools.costs.ec2_ri.optimizer.analizer

import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstancesSummary
import com.sumologic.tools.costs.ec2_ri.optimizer.running.RunningInstancesSummary

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
    ))
    Ec2RiAnalysis(familiesSizeDiffs.toMap)
  }
}
