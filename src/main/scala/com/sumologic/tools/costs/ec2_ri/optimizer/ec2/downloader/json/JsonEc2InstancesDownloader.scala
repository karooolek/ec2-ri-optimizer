package com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.json

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.Ec2Instance
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.Ec2InstancesDownloader

import scala.io.Source

class JsonEc2InstancesDownloader(jsonStringEc2Instances: String) extends Ec2InstancesDownloader {
  def this(jsonSource: Source) {
    this(jsonSource.getLines.mkString)
  }

  override def download(): Seq[Ec2Instance] = {
    Ec2Instance.seqFromJsonString(jsonStringEc2Instances)
  }
}
