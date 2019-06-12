package com.sumologic.tools.costs.ri_ri.optimizer.ri.summarizer

import com.sumologic.tools.costs.ec2_ri.optimizer.ri.ReservedInstancesSummary

trait ReservedInstancesSummarizer {
 def summarize() : ReservedInstancesSummary
}
