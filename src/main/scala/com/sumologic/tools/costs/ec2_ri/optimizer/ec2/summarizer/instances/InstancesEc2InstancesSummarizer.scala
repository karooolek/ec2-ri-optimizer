package com.sumologic.tools.costs.ec2_ri.optimizer.ec2.summarizer.instances

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.summarizer.Ec2InstancesSummarizer
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.{Ec2Instance, Ec2InstancesSummary}

import scala.collection.immutable

class InstancesEc2InstancesSummarizer(ec2instances: Seq[Ec2Instance]) extends Ec2InstancesSummarizer {
  override def summarize(): Ec2InstancesSummary = {
    val ec2familiesInstances = ec2instances.groupBy(_.family)
    val ec2familiesCounts = collection.mutable.Map[String, Float]()
    for (ec2family <- ec2familiesInstances.keys) {
      val ec2familyInstances = ec2familiesInstances(ec2family)
      var totalFamilySize = 0.0f
      for (ec2familyInstance <- ec2familyInstances) {
        totalFamilySize += ec2familyInstance.size
      }
      ec2familiesCounts.put(ec2family, totalFamilySize)
    }
    Ec2InstancesSummary(ec2familiesCounts.toMap)
  }
}
