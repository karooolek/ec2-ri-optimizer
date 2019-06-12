package com.sumologic.tools.costs.ri_ri.optimizer.ri.downloader

import com.sumologic.tools.costs.ri_ri.optimizer.ri.ReservedInstance

trait ReservedInstancesDownloader {
  def download(): Seq[ReservedInstance]
}
