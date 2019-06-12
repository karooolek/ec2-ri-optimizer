package com.sumologic.tools.costs.ec2_ri.optimizer.ri

trait RiDownloader {
  def download(): List[ReservedInstance]
}
