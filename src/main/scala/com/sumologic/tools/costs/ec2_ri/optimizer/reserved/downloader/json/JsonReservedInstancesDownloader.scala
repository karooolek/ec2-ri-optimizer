package com.sumologic.tools.costs.ec2_ri.optimizer.reserved.downloader.json

import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstance
import com.sumologic.tools.costs.ri_ri.optimizer.ri.downloader.ReservedInstancesDownloader

import scala.io.Source

class JsonReservedInstancesDownloader(jsonStringReservedInstances: String) extends ReservedInstancesDownloader {
  def this(jsonSource: Source) {
    this(jsonSource.getLines.mkString)
  }

  override def download(): Seq[ReservedInstance] = {
    ReservedInstance.seqFromJsonString(jsonStringReservedInstances)
  }
}
