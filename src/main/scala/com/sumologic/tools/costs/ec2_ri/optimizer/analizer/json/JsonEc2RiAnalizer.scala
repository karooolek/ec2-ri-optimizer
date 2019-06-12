package com.sumologic.tools.costs.ec2_ri.optimizer.analizer.json

import com.sumologic.tools.costs.ec2_ri.optimizer.analizer.{Ec2RiAnalizer, Ec2RiAnalysis}
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.Ec2InstancesSummary

import scala.io.Source

class JsonEc2RiAnalizer(jsonStringEc2RiAnalysisSummary: String) extends Ec2RiAnalizer(null, null) {
  def this(jsonSource: Source) {
    this(jsonSource.getLines.mkString)
  }

  override def analize() = {
    Ec2RiAnalysis.fromJsonString(jsonStringEc2RiAnalysisSummary)
  }
}
