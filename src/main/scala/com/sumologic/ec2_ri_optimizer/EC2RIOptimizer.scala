package com.sumologic.ec2_ri_optimizer

import com.amazonaws.regions.Regions
import com.amazonaws.services.appsync.AWSAppSyncClientBuilder
import com.amazonaws.services.appsync.model.ListApiKeysRequest

object EC2RIOptimizer {

  def main(args: Array[String]) {
    val apiId = args(0)

    val awsClient = AWSAppSyncClientBuilder.standard().
      withRegion(Regions.EU_CENTRAL_1).
      build()

    val apiKeys = awsClient.listApiKeys(new ListApiKeysRequest().withApiId(apiId)).getApiKeys
    apiKeys.forEach(apiKey => {
      println(apiKey.toString)
    })

    awsClient.shutdown()
  }
}
