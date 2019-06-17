package com.sumologic.tools.costs.ec2_ri.optimizer.running.summarizer

import com.sumologic.tools.costs.ec2_ri.optimizer.running.{RunningInstance, RunningInstancesSummary}

class RunningInstancesSummarizer(runnningInstances: Seq[RunningInstance]) {
  def summarize(): RunningInstancesSummary = {
    val ec2familiesSizes = runnningInstances.groupBy(_.family) map { case (family: String, runningInstances: Seq[RunningInstance]) =>
      (family, runningInstances.filter(_.running).map(_.size).sum)
    }
    RunningInstancesSummary(ec2familiesSizes.toMap)
  }
}
