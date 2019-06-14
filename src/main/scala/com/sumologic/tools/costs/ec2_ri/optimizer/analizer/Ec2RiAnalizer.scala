package com.sumologic.tools.costs.ec2_ri.optimizer.analizer

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.Ec2InstancesSummary
import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstancesSummary

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class Ec2RiAnalizer(ec2InstancesSummary: Ec2InstancesSummary, reservedInstancesSummary: ReservedInstancesSummary) {
  def analize() = {
    val allFamilies = new ListBuffer[String]()
    allFamilies.addAll(ec2InstancesSummary.familiesTotalSizes.keys)
    allFamilies.addAll(reservedInstancesSummary.familiesTotalSizes.keys)

    val familiesSizeDiffs = mutable.Map[String, Ec2RiSizeDiff]()
    for (family <- allFamilies) {
      val runningSize = ec2InstancesSummary.familiesTotalSizes.getOrElse(family, 0.0)
      val reservedSize = reservedInstancesSummary.familiesTotalSizes.getOrElse(family, 0.0)
      val absDiff = runningSize - reservedSize
      val relDiff = runningSize / reservedSize
      familiesSizeDiffs.put(family, Ec2RiSizeDiff(absDiff, relDiff))
    }

    Ec2RiAnalysis(familiesSizeDiffs.toMap)
  }
}
