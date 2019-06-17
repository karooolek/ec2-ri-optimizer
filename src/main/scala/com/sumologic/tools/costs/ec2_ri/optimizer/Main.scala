package com.sumologic.tools.costs.ec2_ri.optimizer

import com.sumologic.tools.costs.ec2_ri.optimizer.analizer.Ec2RiAnalizer
import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstance
import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.summarizer.ReservedInstancesSummarizer
import com.sumologic.tools.costs.ec2_ri.optimizer.running.RunningInstance
import com.sumologic.tools.costs.ec2_ri.optimizer.running.downloader.aws.AwsRunningInstancesDownloader
import com.sumologic.tools.costs.ec2_ri.optimizer.running.summarizer.RunningInstancesSummarizer
import com.sumologic.tools.costs.ri_ri.optimizer.ri.downloader.aws.AwsReservedInstancesDownloader

import scala.collection.mutable.ListBuffer
import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val filename = args(0)

    val totalRunninginstances = new ListBuffer[RunningInstance]()
    val totalReservedInstances = new ListBuffer[ReservedInstance]()

    val bufferedSource = Source.fromFile(filename)
    for (line <- bufferedSource.getLines) {
      val args = line.split(";")
      val regionName = args(0)
      val awsKey = args(1)
      val awsSecret = args(2)

      println(s"Region: ${regionName}")

      val awsRunningInstances = new AwsRunningInstancesDownloader(regionName, awsKey, awsSecret).download()
      totalRunninginstances.addAll(awsRunningInstances)
      val awsRunninginstancesSummary = new RunningInstancesSummarizer(awsRunningInstances).summarize()
      println(awsRunningInstances.length + " aws running instances summary:")
      println(awsRunninginstancesSummary.toJsonString)
      println(RunningInstance.toListJsonString(awsRunningInstances))

      val awsReservedInstances = new AwsReservedInstancesDownloader(regionName, awsKey, awsSecret).download()
      totalReservedInstances.addAll(awsReservedInstances)
      val awsReservedInstancesSummary = new ReservedInstancesSummarizer(awsReservedInstances).summarize()
      println(awsReservedInstances.length + " aws reserved instances summary:")
      println(awsReservedInstancesSummary.toJsonString)
      println(ReservedInstance.toListJsonString(awsReservedInstances))

      println
    }
    bufferedSource.close

//        val totalRunninginstances = new JsonRunningInstancesDownloader(Source.fromFile("running_instances.json")).download();
//        val totalReservedInstances = new JsonReservedInstancesDownloader(Source.fromFile("reserved_instances.json")).download();

    val totalEc2instancesSummary = new RunningInstancesSummarizer(totalRunninginstances.toSeq).summarize()
    //        val totalEc2instancesSummary = new JsonEc2InstancesSummarizer(Source.fromFile("running_instances_summary.json")).summarize()
    println("TOTAL instances summary:")
    println(totalEc2instancesSummary.toJsonString)

    val totalReservedInstancesSummary = new ReservedInstancesSummarizer(totalReservedInstances.toSeq).summarize()
    //        val totalReservedInstancesSummary = new JsonReservedInstancesSummarizer(Source.fromFile("reserved_instances_summary.json")).summarize()
    println("TOTAL reserved instances summary:")
    println(totalReservedInstancesSummary.toJsonString)

    val ec2RiAnalysis = new Ec2RiAnalizer(totalEc2instancesSummary, totalReservedInstancesSummary).analize()
    //    val ec2RiAnalysis = new JsonEc2RiAnalizer(Source.fromFile("ec2_ri_analysis.json")).analize()
    println("TOTAL instances analysis:")
    println(ec2RiAnalysis.toJsonString)

  }
}
