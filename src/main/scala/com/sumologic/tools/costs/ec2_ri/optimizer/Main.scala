package com.sumologic.tools.costs.ec2_ri.optimizer

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.Ec2Instance
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.aws.AwsEc2InstancesDownloader
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.summarizer.instances.InstancesEc2InstancesSummarizer
import com.sumologic.tools.costs.ri_ri.optimizer.ri.ReservedInstance
import com.sumologic.tools.costs.ri_ri.optimizer.ri.downloader.aws.AwsReservedInstancesDownloader
import com.sumologic.tools.costs.ri_ri.optimizer.ri.summarizer.instances.InstancesReservedInstancesSummarizer

import scala.collection.mutable.ListBuffer
import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val filename = args(0)

    val bufferedSource = Source.fromFile(filename)

    val totalEc2instances = new ListBuffer[Ec2Instance]()
    val totalReservedInstances = new ListBuffer[ReservedInstance]()

    for (line <- bufferedSource.getLines) {
      println(line)

      val args = line.split(";")
      val regionName = args(0)
      val awsKey = args(1)
      val awsSecret = args(2)

      val awsEc2instances = new AwsEc2InstancesDownloader(regionName, awsKey, awsSecret).download()
      totalEc2instances.addAll(awsEc2instances)
      val awsEc2instancesSummary = new InstancesEc2InstancesSummarizer(awsEc2instances).summarize()
      println(awsEc2instances.length + " aws instances summary:")
      println(awsEc2instancesSummary.toJsonString)

      val awsReservedInstances = new AwsReservedInstancesDownloader(regionName, awsKey, awsSecret).download()
      totalReservedInstances.addAll(awsReservedInstances)
      val awsReservedInstancesSummary = new InstancesReservedInstancesSummarizer(awsReservedInstances).summarize()
      println(awsReservedInstances.length + " aws reserved instances:")
      for (awsReservedInstance <- awsReservedInstances) {
        println(awsReservedInstance.toJsonString)
      }
      println(awsReservedInstances.length + " aws reserved instances summary:")
      println(awsEc2instancesSummary.toJsonString)
    }

    val totalEc2instancesSummary = new InstancesEc2InstancesSummarizer(totalEc2instances.toSeq).summarize()
    println("TOTAL " + totalEc2instances.length + " instances summary:")
    println(totalEc2instancesSummary.toJsonString)

    val totalReservedInstancesSummary = new InstancesReservedInstancesSummarizer(totalReservedInstances.toSeq).summarize()
    println("TOTAL " + totalReservedInstances.length + " reserved instances summary:")
    println(totalReservedInstancesSummary.toJsonString)

    bufferedSource.close
  }
}
