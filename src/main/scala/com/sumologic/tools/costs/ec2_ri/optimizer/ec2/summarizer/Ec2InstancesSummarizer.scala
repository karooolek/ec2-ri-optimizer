package com.sumologic.tools.costs.ec2_ri.optimizer.ec2.summarizer

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.{Ec2Instance, Ec2InstancesSummary}

class Ec2InstancesSummarizer(ec2instances: Seq[Ec2Instance]) {
  def summarize(): Ec2InstancesSummary = {
    val ec2familiesSizes = ec2instances.groupBy(_.family) map { case (family: String, ec2instances: Seq[Ec2Instance]) =>
      (family, ec2instances.filter(_.running).map(_.size).sum)
    }
    Ec2InstancesSummary(ec2familiesSizes.toMap)
  }
}
