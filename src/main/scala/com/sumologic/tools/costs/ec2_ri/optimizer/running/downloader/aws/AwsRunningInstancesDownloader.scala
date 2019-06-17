package com.sumologic.tools.costs.ec2_ri.optimizer.running.downloader.aws

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.regions.Regions
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.sumologic.tools.costs.ec2_ri.optimizer.running.RunningInstance
import com.sumologic.tools.costs.ec2_ri.optimizer.running.downloader.RunningInstancesDownloader

import scala.collection.JavaConverters._

class AwsRunningInstancesDownloader(awsRegion: String, awsKey: String, awsSecret: String) extends RunningInstancesDownloader {

  def download(): Seq[RunningInstance] = {
    val ec2client = AmazonEC2Client.builder().
      withRegion(Regions.fromName(awsRegion)).
      withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsKey, awsSecret))).
      build()

    var awsDescribeInstancesResponse = ec2client.describeInstances()
    val runningInstances = awsDescribeInstancesResponse.getReservations.asScala.flatMap(
      awsReservation => awsReservation.getInstances.asScala.map(
        awsInstance => RunningInstance.fromAwsInstance(awsInstance)
      )
    )

    var nextToken = awsDescribeInstancesResponse.getNextToken
    while (nextToken != null) {
      awsDescribeInstancesResponse = ec2client.describeInstances(new DescribeInstancesRequest().
        withNextToken(nextToken)
      )
      runningInstances.addAll(awsDescribeInstancesResponse.getReservations.asScala.flatMap(
        awsReservation => awsReservation.getInstances.asScala.map(
          awsInstance => RunningInstance.fromAwsInstance(awsInstance)
        )
      ))
      nextToken = awsDescribeInstancesResponse.getNextToken
    }

    ec2client.shutdown()

    runningInstances.toSeq
  }

}
