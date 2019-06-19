package com.sumologic.tools.costs.ec2_ri.optimizer

import com.sumologic.tools.costs.ec2_ri.optimizer.analizer.{Ec2RiAnalizer, Ec2RiAnalysis}
import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstance
import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.summarizer.ReservedInstancesSummarizer
import com.sumologic.tools.costs.ec2_ri.optimizer.running.RunningInstance
import com.sumologic.tools.costs.ec2_ri.optimizer.running.downloader.aws.AwsRunningInstancesDownloader
import com.sumologic.tools.costs.ec2_ri.optimizer.running.summarizer.RunningInstancesSummarizer
import com.sumologic.tools.costs.ri_ri.optimizer.ri.downloader.aws.AwsReservedInstancesDownloader

import scala.collection.mutable.ListBuffer
import scala.io.Source

class Ec2RiCostsOptimizer(accountsFilename: String) {
  def analize(): Ec2RiAnalysis = {
    val totalRunninginstances = new ListBuffer[RunningInstance]()
    val totalReservedInstances = new ListBuffer[ReservedInstance]()

    val bufferedSource = Source.fromFile(accountsFilename)
    for (line <- bufferedSource.getLines) {
      val args = line.split(";")
      val regionName = args(0)
      val awsKey = args(1)
      val awsSecret = args(2)

      val awsRunningInstances = new AwsRunningInstancesDownloader(regionName, awsKey, awsSecret).download()
      totalRunninginstances.appendAll(awsRunningInstances)

      val awsReservedInstances = new AwsReservedInstancesDownloader(regionName, awsKey, awsSecret).download()
      totalReservedInstances.appendAll(awsReservedInstances)
    }
    bufferedSource.close

    val totalEc2instancesSummary = new RunningInstancesSummarizer(totalRunninginstances).summarize()
    val totalReservedInstancesSummary = new ReservedInstancesSummarizer(totalReservedInstances).summarize()
    val ec2RiAnalysis = new Ec2RiAnalizer(totalEc2instancesSummary, totalReservedInstancesSummary).analize()

    ec2RiAnalysis
  }

//  def optimize(): {
//    // TODO waaat?
//  }
}

object Ec2RiCostsOptimizer {
  def main(args: Array[String]): Unit = {
    println(new Ec2RiCostsOptimizer(args(0)).analize().toJsonString);
  }
}