package com.sumologic.tools.costs.ec2_ri.optimizer

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.Ec2Instance
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.aws.AwsEc2InstancesDownloader
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.json.JsonEc2InstancesDownloader

import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val filename = args(0)

    val bufferedSource = Source.fromFile(filename)
    for (line <- bufferedSource.getLines) {
      println(line)

      val args = line.split(";")
      val regionName = args(0)
      val awsKey = args(1)
      val awsSecret = args(2)

      val ec2instances = new AwsEc2InstancesDownloader(regionName, awsKey, awsSecret).download()
      println(ec2instances.length + " aws instances:")
      for (ec2instance <- ec2instances) {
        println(ec2instance.toJsonString)
      }

      val ec2instances2 = new JsonEc2InstancesDownloader(Ec2Instance.toListJsonString(ec2instances))
      println(ec2instances.length + " json instances:")
      for (ec2instance <- ec2instances) {
        println(ec2instance.toJsonString)
      }

      //    val ec2reservedInstances = new AWSReservedInstancesDownloader(regionName,awsKey, awsSecret).download()
      //    println(ec2reservedInstances.length + " reserved instances:")
      //    for (ec2reservedInstance <- ec2reservedInstances) {
      //      println(ec2reservedInstance)
      //    }

    }
    bufferedSource.close
  }
}
