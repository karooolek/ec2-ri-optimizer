package com.sumologic.tools.costs.ec2_ri.optimizer

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.regions.Regions
import com.amazonaws.services.ec2.AmazonEC2Client

import scala.collection.JavaConverters._

object EC2RICostsOptimizer {

  def main(args: Array[String]) {
    val awsKey = args(0)
    val awsSecret = args(1)

    val ec2client = AmazonEC2Client.builder().
      withRegion(Regions.EU_CENTRAL_1).
      withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsKey, awsSecret))).
      build()

    val ec2instances = ec2client.describeInstances().getReservations.asScala

    println("instances:")
    for (ec2instance <- ec2instances) {
      println(ec2instance)
    }

    println("reserved instances:")
    val ec2reservedInstances = ec2client.describeReservedInstances().getReservedInstances.asScala
    for (ec2reservedInstance <- ec2reservedInstances) {
      println(ec2reservedInstance)
    }

    ec2client.shutdown()
  }
}
