package com.sumologic.tools.costs.ec2_ri.optimizer

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.Ec2InstancesDownloader
import com.sumologic.tools.costs.ec2_ri.optimizer.ri.{ReservedInstance, RiDownloader}

class Ec2RiCostsOptimizer(ec2instancesDownloader: Ec2InstancesDownloader, riDownloader: RiDownloader) {

  def analyze() = {
    null
  }

  def optimize(): List[ReservedInstance] = {
    // TODO
    null
  }
}
