package com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.Ec2Instance

trait Ec2InstancesDownloader {
  def download(): List[Ec2Instance]
}
