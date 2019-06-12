package com.sumologic.tools.costs.ec2_ri.optimizer.analizer

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.Ec2InstancesSummary
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{write, read}

case class Ec2RiAnalysis(familiesSizeDiffs: Map[String, Double]) {
  // nothing

  implicit lazy val formats = DefaultFormats

  def toJsonString: String = {
    write(this)
  }
}

object Ec2RiAnalysis {
  implicit lazy val formats = DefaultFormats

  def fromJsonString(jsonStringEc2RiAnalysisSummary: String): Ec2RiAnalysis = {
    read[Ec2RiAnalysis](jsonStringEc2RiAnalysisSummary)
  }
}



