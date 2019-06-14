package com.sumologic.tools.costs.ec2_ri.optimizer.reserved.summarizer

import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.{ReservedInstance, ReservedInstancesSummary}

class ReservedInstancesSummarizer(reservedInstances: Seq[ReservedInstance]) {
  def summarize(): ReservedInstancesSummary = {
    val reservedFamiliesSizes = reservedInstances.groupBy(_.family) map { case (family: String, reservedInstances: Seq[ReservedInstance]) =>
      (family, reservedInstances.filter(_.active).map(_.size).sum)
    }
    ReservedInstancesSummary(reservedFamiliesSizes.toMap)
  }
}
