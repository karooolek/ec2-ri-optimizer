package com.sumologic.tools.costs.ec2_ri.optimizer.reserved.summarizer

import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.{ReservedInstance, ReservedInstancesSummary}

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
