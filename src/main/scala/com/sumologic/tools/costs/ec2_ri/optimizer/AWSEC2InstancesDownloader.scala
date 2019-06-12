package com.sumologic.tools.costs.ec2_ri.optimizer

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.regions.Regions
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.DescribeInstancesRequest

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

class AWSEC2InstancesDownloader(awsRegion: String, awsKey: String, awsSecret: String) {

  def download(): List[EC2Instance] = {
    val ec2client = AmazonEC2Client.builder().
      withRegion(Regions.fromName(awsRegion)).
      withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsKey, awsSecret))).
      build()

    var ec2instances = ListBuffer[EC2Instance]()
    var awsDescribeInstances = ec2client.describeInstances()
    for (awsReservation <- awsDescribeInstances.getReservations.asScala) {
      for (awsInstance <- awsReservation.getInstances.asScala) {
        ec2instances += EC2Instance.fromAWSInstance(awsInstance)
      }
    }
    var nextToken = awsDescribeInstances.getNextToken
    while (nextToken != null) {
      awsDescribeInstances = ec2client.describeInstances(new DescribeInstancesRequest().withNextToken(nextToken))
      for (awsReservation <- awsDescribeInstances.getReservations.asScala) {
        for (awsInstance <- awsReservation.getInstances.asScala) {
          ec2instances += EC2Instance.fromAWSInstance(awsInstance)
        }
      }
      nextToken = awsDescribeInstances.getNextToken
    }

    ec2client.shutdown()

    ec2instances.toList
  }

}
