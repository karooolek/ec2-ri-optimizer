package com.sumologic.tools.costs.ec2_ri.optimizer

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.{Ec2Instance, Ec2InstancesSummary}
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.aws.AwsEc2InstancesDownloader
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.json.JsonEc2InstancesDownloader
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.summarizer.instances.InstancesEc2InstancesSummarizer
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.summarizer.json.JsonEc2InstancesSummarizer

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val filename = args(0)

    val bufferedSource = Source.fromFile(filename)

    val allAwsEc2instances = new ListBuffer[Ec2Instance]()

    for (line <- bufferedSource.getLines) {
      println(line)

      val args = line.split(";")
      val regionName = args(0)
      val awsKey = args(1)
      val awsSecret = args(2)

      val awsEc2instances = new AwsEc2InstancesDownloader(regionName, awsKey, awsSecret).download()
      allAwsEc2instances.addAll(awsEc2instances)
      val awsEc2instancesSummary = new InstancesEc2InstancesSummarizer(awsEc2instances).summarize()

      println(awsEc2instances.length + " aws instances summary:")
      println(awsEc2instancesSummary.toJsonString)
    }

    val allAwsEc2instancesSummary = new InstancesEc2InstancesSummarizer(allAwsEc2instances.toSeq).summarize()
    println("TOTAL " + allAwsEc2instances.length + " aws instances summary:")
    println(allAwsEc2instancesSummary.toJsonString)

    bufferedSource.close
  }
}
