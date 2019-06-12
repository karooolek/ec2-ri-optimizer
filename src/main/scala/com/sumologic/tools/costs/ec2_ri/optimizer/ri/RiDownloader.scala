package com.sumologic.tools.costs.ec2_ri.optimizer.ri

import com.sumologic.tools.costs.ec2_ri.optimizer.ReservedInstance

trait RiDownloader {
  def download(): List[ReservedInstance]
}
