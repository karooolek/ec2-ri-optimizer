package com.sumologic.tools.costs.ri_ri.optimizer.ri.summarizer.json

import com.sumologic.tools.costs.ec2_ri.optimizer.ri.ReservedInstancesSummary
import com.sumologic.tools.costs.ri_ri.optimizer.ri.summarizer.ReservedInstancesSummarizer

class JsonReservedInstancesSummarizer(jsonStringReservedInstancesSummary: String) extends ReservedInstancesSummarizer(null) {
  override def summarize(): ReservedInstancesSummary = {
    ReservedInstancesSummary.fromJsonString(jsonStringReservedInstancesSummary)
  }
}
