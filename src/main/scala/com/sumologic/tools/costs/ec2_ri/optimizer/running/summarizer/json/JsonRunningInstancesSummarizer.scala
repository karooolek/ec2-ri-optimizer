package com.sumologic.tools.costs.ec2_ri.optimizer.running.summarizer.json

import com.sumologic.tools.costs.ec2_ri.optimizer.running.RunningInstancesSummary
import com.sumologic.tools.costs.ec2_ri.optimizer.running.summarizer.RunningInstancesSummarizer

import scala.io.Source

class JsonRunningInstancesSummarizer(jsonStringRunningInstancesSummary: String) extends RunningInstancesSummarizer(null) {
  def this(jsonSource: Source) {
    this(jsonSource.getLines.mkString)
  }

  override def summarize(): RunningInstancesSummary = {
    RunningInstancesSummary.fromJsonString(jsonStringRunningInstancesSummary)
  }
}
