package com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.aws

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.regions.Regions
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.Ec2Instance
import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.downloader.Ec2InstancesDownloader

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

class AwsEc2InstancesDownloader(awsRegion: String, awsKey: String, awsSecret: String) extends Ec2InstancesDownloader {

  def download(): List[Ec2Instance] = {
    val ec2client = AmazonEC2Client.builder().
      withRegion(Regions.fromName(awsRegion)).
      withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsKey, awsSecret))).
      build()

    var ec2instances = ListBuffer[Ec2Instance]()
    var awsDescribeInstances = ec2client.describeInstances()
    for (awsReservation <- awsDescribeInstances.getReservations.asScala) {
      for (awsInstance <- awsReservation.getInstances.asScala) {
        ec2instances += Ec2Instance.fromAwsInstance(awsInstance)
      }
    }
    var nextToken = awsDescribeInstances.getNextToken
    while (nextToken != null) {
      awsDescribeInstances = ec2client.describeInstances(new DescribeInstancesRequest().withNextToken(nextToken))
      for (awsReservation <- awsDescribeInstances.getReservations.asScala) {
        for (awsInstance <- awsReservation.getInstances.asScala) {
          ec2instances += Ec2Instance.fromAwsInstance(awsInstance)
        }
      }
      nextToken = awsDescribeInstances.getNextToken
    }

    ec2client.shutdown()

    ec2instances.toList
  }

}
