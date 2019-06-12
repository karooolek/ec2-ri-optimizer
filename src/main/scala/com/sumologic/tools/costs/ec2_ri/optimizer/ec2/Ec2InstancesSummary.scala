package com.sumologic.tools.costs.ec2_ri.optimizer.ec2

import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

case class Ec2InstancesSummary(familiesTotalSizes: Map[String, Double]) {
  // nothing

  implicit lazy val formats = DefaultFormats

  def toJsonString: String = {
    write(this)
  }
}

object Ec2InstancesSummary {

  implicit lazy val formats = DefaultFormats

  def fromJsonString(jsonStringEc2InstancesSummary: String): Ec2InstancesSummary = {
    read[Ec2InstancesSummary](jsonStringEc2InstancesSummary)
  }
}