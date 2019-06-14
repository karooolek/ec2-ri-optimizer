package com.sumologic.tools.costs.ec2_ri.optimizer.ec2.summarizer.json

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.Ec2InstancesSummary
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.summarizer.Ec2InstancesSummarizer

import scala.io.Source

class JsonEc2InstancesSummarizer(jsonStringEc2InstancesSummary: String) extends Ec2InstancesSummarizer(null) {
  def this(jsonSource: Source) {
    this(jsonSource.getLines.mkString)
  }

  override def summarize(): Ec2InstancesSummary = {
    Ec2InstancesSummary.fromJsonString(jsonStringEc2InstancesSummary)
  }
}
