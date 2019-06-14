package com.sumologic.tools.costs.ec2_ri.optimizer.analizer

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.Ec2InstancesSummary
import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstancesSummary

class Ec2RiAnalizer(ec2InstancesSummary: Ec2InstancesSummary, reservedInstancesSummary: ReservedInstancesSummary) {
  def analize() = {
    val allFamilies = ec2InstancesSummary.ec2familiesSizes.keys ++ reservedInstancesSummary.reservedFamiliesSizes.keys
    val familiesSizeDiffs = allFamilies.map(family => (
      family, {
      val runningSize = ec2InstancesSummary.ec2familiesSizes.getOrElse(family, 0.0)
      val reservedSize = reservedInstancesSummary.reservedFamiliesSizes.getOrElse(family, 0.0)
      val absDiff = runningSize - reservedSize
      val relDiff = runningSize / reservedSize
      Ec2RiSizeDiff(runningSize, reservedSize, absDiff, relDiff)
    }
    ))
    Ec2RiAnalysis(familiesSizeDiffs.toMap)
  }
}
