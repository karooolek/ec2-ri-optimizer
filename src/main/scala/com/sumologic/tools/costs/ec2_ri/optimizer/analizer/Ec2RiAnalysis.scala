package com.sumologic.tools.costs.ec2_ri.optimizer.analizer

import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

case class Ec2RiAnalysis(familiesSizeDiffs: Map[String, Ec2RiSizeDiff], overReservedFamilies: Seq[String], underReservedFamilies: Seq[String]) {
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



