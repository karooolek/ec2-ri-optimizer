package com.sumologic.tools.costs.ec2_ri.optimizer

import com.sumologic.tools.costs.ec2_ri.optimizer.analizer.Ec2RiAnalizer
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.Ec2Instance
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.aws.AwsEc2InstancesDownloader
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.summarizer.Ec2InstancesSummarizer
import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstance
import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.summarizer.ReservedInstancesSummarizer
import com.sumologic.tools.costs.ri_ri.optimizer.ri.downloader.aws.AwsReservedInstancesDownloader

import scala.collection.mutable.ListBuffer
import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val filename = args(0)

    val totalEc2instances = new ListBuffer[Ec2Instance]()
    val totalReservedInstances = new ListBuffer[ReservedInstance]()

    val bufferedSource = Source.fromFile(filename)
    for (line <- bufferedSource.getLines) {
      val args = line.split(";")
      val regionName = args(0)
      val awsKey = args(1)
      val awsSecret = args(2)

      println(s"Region: ${regionName}")

      val awsEc2instances = new AwsEc2InstancesDownloader(regionName, awsKey, awsSecret).download()
      totalEc2instances.addAll(awsEc2instances)
      val awsEc2instancesSummary = new Ec2InstancesSummarizer(awsEc2instances).summarize()
      println(awsEc2instances.length + " aws instances summary:")
      println(awsEc2instancesSummary.toJsonString)
      println(Ec2Instance.toListJsonString(awsEc2instances))

      val awsReservedInstances = new AwsReservedInstancesDownloader(regionName, awsKey, awsSecret).download()
      totalReservedInstances.addAll(awsReservedInstances)
      val awsReservedInstancesSummary = new ReservedInstancesSummarizer(awsReservedInstances).summarize()
      println(awsReservedInstances.length + " aws reserved instances summary:")
      println(awsReservedInstancesSummary.toJsonString)
      println(ReservedInstance.toListJsonString(awsReservedInstances))

      println
    }
    bufferedSource.close

    val totalEc2instancesSummary = new Ec2InstancesSummarizer(totalEc2instances.toSeq).summarize()
    //        val totalEc2instancesSummary = new JsonEc2InstancesSummarizer(Source.fromFile("ec2_summary.json")).summarize()
    println("TOTAL instances summary:")
    println(totalEc2instancesSummary.toJsonString)

    val totalReservedInstancesSummary = new ReservedInstancesSummarizer(totalReservedInstances.toSeq).summarize()
    //        val totalReservedInstancesSummary = new JsonReservedInstancesSummarizer(Source.fromFile("ri_summary.json")).summarize()
    println("TOTAL reserved instances summary:")
    println(totalReservedInstancesSummary.toJsonString)

    val ec2RiAnalysis = new Ec2RiAnalizer(totalEc2instancesSummary, totalReservedInstancesSummary).analize()
    //    val ec2RiAnalysis = new JsonEc2RiAnalizer(Source.fromFile("ec2_ri_analysis.json")).analize()
    println("TOTAL (reserved - running) instances analysis:")
    println(ec2RiAnalysis.toJsonString)

  }
}
