package com.sumologic.tools.costs.ri_ri.optimizer.ri.downloader.aws

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.regions.Regions
import com.amazonaws.services.ec2.AmazonEC2Client
import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstance
import com.sumologic.tools.costs.ri_ri.optimizer.ri.downloader.ReservedInstancesDownloader

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

class AwsReservedInstancesDownloader(awsRegion: String, awsKey: String, awsSecret: String) extends ReservedInstancesDownloader {
  override def download(): Seq[ReservedInstance] = {
    val ec2client = AmazonEC2Client.builder().
      withRegion(Regions.fromName(awsRegion)).
      withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsKey, awsSecret))).
      build()

    var reservedInstances = ListBuffer[ReservedInstance]()
    var awsDescribeReservedInstancesResponse = ec2client.describeReservedInstances()
    for (awsReservedInstance <- awsDescribeReservedInstancesResponse.getReservedInstances.asScala) {
      reservedInstances += ReservedInstance.fromAwsReservedInstance(awsReservedInstance)
    }

    ec2client.shutdown()

    reservedInstances.toList
  }
}
