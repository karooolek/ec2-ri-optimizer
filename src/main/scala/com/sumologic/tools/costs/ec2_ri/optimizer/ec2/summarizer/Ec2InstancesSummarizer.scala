package com.sumologic.tools.costs.ec2_ri.optimizer.ec2.summarizer

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.{Ec2Instance, Ec2InstancesSummary}

class Ec2InstancesSummarizer(ec2instances: Seq[Ec2Instance]) {
  def summarize(): Ec2InstancesSummary = {
    val ec2familiesInstances = ec2instances.groupBy(_.family)
    val ec2familiesCounts = collection.mutable.Map[String, Double]()
    for (ec2family <- ec2familiesInstances.keys) {
      val ec2familyInstances = ec2familiesInstances(ec2family)
      var totalRunningFamilySize = ec2familyInstances.filter(_.running).foldLeft(0.0)(_ + _.size)
      ec2familiesCounts.put(ec2family, totalRunningFamilySize)
    }
    Ec2InstancesSummary(ec2familiesCounts.toMap)
  }
}
