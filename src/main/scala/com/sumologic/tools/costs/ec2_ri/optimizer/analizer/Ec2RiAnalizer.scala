package com.sumologic.tools.costs.ec2_ri.optimizer.analizer

import com.sumologic.tools.costs.ec2_ri.optimizer.running.RunningInstancesSummary
import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstancesSummary

class Ec2RiAnalizer(ec2InstancesSummary: RunningInstancesSummary, reservedInstancesSummary: ReservedInstancesSummary) {
  def analize() = {
    val allFamilies = ec2InstancesSummary.runningFamiliesSizes.keys ++ reservedInstancesSummary.reservedFamiliesSizes.keys
    val familiesSizeDiffs = allFamilies.map(family => (
      family, {
      val runningSize = ec2InstancesSummary.runningFamiliesSizes.getOrElse(family, 0.0)
      val reservedSize = reservedInstancesSummary.reservedFamiliesSizes.getOrElse(family, 0.0)
      val absDiff = runningSize - reservedSize
      val relDiff = runningSize / reservedSize
      Ec2RiSizeDiff(runningSize, reservedSize, absDiff, relDiff)
    }
    ))
    Ec2RiAnalysis(familiesSizeDiffs.toMap)
  }
}
