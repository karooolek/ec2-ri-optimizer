package com.sumologic.tools.costs.ri_ri.optimizer.ri.summarizer.json

import com.sumologic.tools.costs.ec2_ri.optimizer.ri.ReservedInstancesSummary
import com.sumologic.tools.costs.ri_ri.optimizer.ri.summarizer.ReservedInstancesSummarizer

import scala.io.Source

class JsonReservedInstancesSummarizer(jsonStringReservedInstancesSummary: String) extends ReservedInstancesSummarizer(null) {
  def this(jsonSource: Source) {
    this(jsonSource.getLines.mkString)
  }

  override def summarize(): ReservedInstancesSummary = {
    ReservedInstancesSummary.fromJsonString(jsonStringReservedInstancesSummary)
  }
}
