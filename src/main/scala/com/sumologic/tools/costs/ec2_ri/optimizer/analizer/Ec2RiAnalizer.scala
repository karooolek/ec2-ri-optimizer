package com.sumologic.tools.costs.ec2_ri.optimizer.analizer

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.Ec2InstancesSummary
import com.sumologic.tools.costs.ec2_ri.optimizer.ri.ReservedInstancesSummary

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class Ec2RiAnalizer(ec2InstancesSummary: Ec2InstancesSummary, reservedInstancesSummary: ReservedInstancesSummary) {
  def analyze() = {
    val allFamilies = new ListBuffer[String]()
    allFamilies.addAll(ec2InstancesSummary.familiesTotalSizes.keys)
    allFamilies.addAll(reservedInstancesSummary.familiesTotalSizes.keys)

    val familiesSizeDiffs = mutable.Map[String, Double]()
    for (family <- allFamilies) {
      val runningSize = ec2InstancesSummary.familiesTotalSizes.getOrElse(family, 0.0)
      val reservedSize = reservedInstancesSummary.familiesTotalSizes.getOrElse(family, 0.0)
      val sizeDiff = reservedSize - runningSize;
      familiesSizeDiffs.put(family, sizeDiff)
    }

    Ec2RiAnalysisSummary(familiesSizeDiffs.toMap)
  }
}
