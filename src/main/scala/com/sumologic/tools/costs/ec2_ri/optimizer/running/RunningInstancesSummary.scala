package com.sumologic.tools.costs.ec2_ri.optimizer.running

import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

case class RunningInstancesSummary(runningFamiliesSizes: Map[String, Double]) {
  // nothing

  implicit lazy val formats = DefaultFormats

  def toJsonString: String = {
    write(this)
  }
}

object RunningInstancesSummary {

  implicit lazy val formats = DefaultFormats

  def fromJsonString(jsonStringRunningInstancesSummary: String): RunningInstancesSummary = {
    read[RunningInstancesSummary](jsonStringRunningInstancesSummary)
  }
}