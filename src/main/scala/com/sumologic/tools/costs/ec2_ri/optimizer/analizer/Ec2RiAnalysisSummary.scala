package com.sumologic.tools.costs.ec2_ri.optimizer.analizer

import org.json4s.DefaultFormats
import org.json4s.native.Serialization.write

case class Ec2RiAnalysisSummary(familiesSizeDiffs: Map[String, Double]) {
  // nothing

  implicit lazy val formats = DefaultFormats

  def toJsonString: String = {
    write(this)
  }
}



