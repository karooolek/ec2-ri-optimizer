package com.sumologic.tools.costs.ec2_ri.optimizer.running.downloader

import com.sumologic.tools.costs.ec2_ri.optimizer.running.RunningInstance

trait RunningInstancesDownloader {
  def download(): Seq[RunningInstance]
}
