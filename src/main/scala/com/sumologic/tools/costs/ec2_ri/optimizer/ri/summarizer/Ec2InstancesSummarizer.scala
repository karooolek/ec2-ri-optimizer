package com.sumologic.tools.costs.ri_ri.optimizer.ri.summarizer

import com.sumologic.tools.costs.ec2_ri.optimizer.ri.ReservedInstancesSummary
import com.sumologic.tools.costs.ri_ri.optimizer.ri.ReservedInstance

class ReservedInstancesSummarizer(reservedInstances: Seq[ReservedInstance]) {
  def summarize(): ReservedInstancesSummary = {
    val riFamiliesInsances = reservedInstances.groupBy(_.family)
    val riFamiliesCounts = collection.mutable.Map[String, Double]()
    for (riFamily <- riFamiliesInsances.keys) {
      val riFamilyInstances = riFamiliesInsances(riFamily)
      var totalActiveFamilySize = riFamilyInstances.filter(_.active).foldLeft(0.0)(_ + _.size)
      riFamiliesCounts.put(riFamily, totalActiveFamilySize)
    }
    ReservedInstancesSummary(riFamiliesCounts.toMap)
  }
}
