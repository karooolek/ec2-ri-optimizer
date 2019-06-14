package com.sumologic.tools.costs.ec2_ri.optimizer.reserved

import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

case class ReservedInstancesSummary(familiesTotalSizes: Map[String, Double]) {
  // nothing

  implicit lazy val formats = DefaultFormats

  def toJsonString: String = {
    write(this)
  }
}

object ReservedInstancesSummary {
  implicit lazy val formats = DefaultFormats

  def fromJsonString(jsonStringReservedInstancesSummary: String): ReservedInstancesSummary = {
    read[ReservedInstancesSummary](jsonStringReservedInstancesSummary)
  }
}

