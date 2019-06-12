package com.sumologic.tools.costs.ec2_ri.optimizer

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.{Ec2Instance, Ec2InstancesSummary}
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.aws.AwsEc2InstancesDownloader
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.json.JsonEc2InstancesDownloader
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.summarizer.instances.InstancesEc2InstancesSummarizer
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.summarizer.json.JsonEc2InstancesSummarizer

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

      val awsEc2instances = new AwsEc2InstancesDownloader(regionName, awsKey, awsSecret).download()
      println(awsEc2instances.length + " aws instances:")
      for (awsEc2Instances <- awsEc2instances) {
        println(awsEc2Instances.toJsonString)
      }

//      val jsonEc2instances = new JsonEc2InstancesDownloader(Ec2Instance.toListJsonString(awsEc2instances)).download()
//      println(jsonEc2instances.length + " json instances:")
//      for (jsonEc2instance <- jsonEc2instances) {
//        println(jsonEc2instance.toJsonString)
//      }

      val awsEc2instancesSummary = new InstancesEc2InstancesSummarizer(awsEc2instances).summarize()
      println("aws instances summary:")
      println(awsEc2instancesSummary.toJsonString)


      val jsonEc2instancesSummary = new JsonEc2InstancesSummarizer(awsEc2instancesSummary.toJsonString).summarize()
      println("json instances summary:")
      println(jsonEc2instancesSummary.toJsonString)

      //      println("instances summary:")
      //      val ec2instanceSummary = Ec2InstancesSummary(
      //        Map(
      //          "c3" -> 2.5f,
      //          "m5" -> 4.0f,
      //          "c2" -> 10.5f,
      //        )
      //      )
      //      println(ec2instanceSummary.toJsonString)


      //    val ec2reservedInstances = new AWSReservedInstancesDownloader(regionName,awsKey, awsSecret).download()
      //    println(ec2reservedInstances.length + " reserved instances:")
      //    for (ec2reservedInstance <- ec2reservedInstances) {
      //      println(ec2reservedInstance)
      //    }

    }
    bufferedSource.close
  }
}
