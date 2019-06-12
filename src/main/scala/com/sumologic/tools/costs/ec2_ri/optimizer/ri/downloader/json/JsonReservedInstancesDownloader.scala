package com.sumologic.tools.costs.ri_ri.optimizer.ri.downloader.json

import com.sumologic.tools.costs.ri_ri.optimizer.ri.ReservedInstance
import com.sumologic.tools.costs.ri_ri.optimizer.ri.downloader.ReservedInstancesDownloader

class JsonReservedInstancesDownloader(jsonStringReservedInstances: String) extends ReservedInstancesDownloader {
  override def download(): Seq[ReservedInstance] = {
    ReservedInstance.seqFromJsonString(jsonStringReservedInstances)
  }
}
