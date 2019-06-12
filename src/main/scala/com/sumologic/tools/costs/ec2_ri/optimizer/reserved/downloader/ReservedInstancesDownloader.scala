package com.sumologic.tools.costs.ri_ri.optimizer.ri.downloader

import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstance

trait ReservedInstancesDownloader {
  def download(): Seq[ReservedInstance]
}
