package com.sumologic.tools.costs.ec2_ri.optimizer.summarizer

import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{write, read}

case class Ec2InstancesSummary(familiesCounts: Map[String, Float]) {
  // nothing

  def toJsonString: String = {
    write(this)(formats = DefaultFormats)
  }
}

object Ec2InstancesSummary {
  def fromJsonString(jsonStringEc2InstancesSummary: String): Ec2InstancesSummary = {
    read[Ec2InstancesSummary](jsonStringEc2InstancesSummary)(formats = DefaultFormats, mf = Manifest.classType(Ec2InstancesSummary.getClass))
  }
}