package com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.aws

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.regions.Regions
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.Ec2Instance
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.Ec2InstancesDownloader

import scala.collection.JavaConverters._

class AwsEc2InstancesDownloader(awsRegion: String, awsKey: String, awsSecret: String) extends Ec2InstancesDownloader {

  def download(): Seq[Ec2Instance] = {
    val ec2client = AmazonEC2Client.builder().
      withRegion(Regions.fromName(awsRegion)).
      withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsKey, awsSecret))).
      build()

    var awsDescribeInstancesResponse = ec2client.describeInstances()
    val ec2instances = awsDescribeInstancesResponse.getReservations.asScala.flatMap(
      awsReservation => awsReservation.getInstances.asScala.map(
        awsInstance => Ec2Instance.fromAwsInstance(awsInstance)
      )
    )

    var nextToken = awsDescribeInstancesResponse.getNextToken
    while (nextToken != null) {
      awsDescribeInstancesResponse = ec2client.describeInstances(new DescribeInstancesRequest().
        withNextToken(nextToken)
      )
      ec2instances.addAll(awsDescribeInstancesResponse.getReservations.asScala.flatMap(
        awsReservation => awsReservation.getInstances.asScala.map(
          awsInstance => Ec2Instance.fromAwsInstance(awsInstance)
        )
      ))
      nextToken = awsDescribeInstancesResponse.getNextToken
    }

    ec2client.shutdown()

    ec2instances.toSeq
  }

}
