package com.sumologic.tools.costs.ec2_ri.optimizer.ri.downloader

import com.sumologic.tools.costs.ec2_ri.optimizer.ri.ReservedInstance

trait RiDownloader {
  def download(): List[ReservedInstance]
}
