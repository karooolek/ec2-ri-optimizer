package com.sumologic.tools.costs.ec2_ri.optimizer.running.downloader.json

import com.sumologic.tools.costs.ec2_ri.optimizer.running.RunningInstance
import com.sumologic.tools.costs.ec2_ri.optimizer.running.downloader.RunningInstancesDownloader

import scala.io.Source

class JsonRunningInstancesDownloader(jsonStringRunningInstances: String) extends RunningInstancesDownloader {
  def this(jsonSource: Source) {
    this(jsonSource.getLines.mkString)
  }

  override def download(): Seq[RunningInstance] = {
    RunningInstance.seqFromJsonString(jsonStringRunningInstances)
  }
}
