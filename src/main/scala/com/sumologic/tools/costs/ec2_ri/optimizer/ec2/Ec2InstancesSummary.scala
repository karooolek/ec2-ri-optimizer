package com.sumologic.tools.costs.ec2_ri.optimizer.ec2

import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

case class Ec2InstancesSummary(familiesTotalSizes: Map[String, Float]) {
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