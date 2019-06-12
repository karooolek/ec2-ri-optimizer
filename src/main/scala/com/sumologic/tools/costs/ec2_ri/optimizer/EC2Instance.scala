package com.sumologic.tools.costs.ec2_ri.optimizer

import com.amazonaws.services.ec2.model.Instance

case class EC2Instance(id: String, family: String, running: Boolean) {
  // nothing
}

object EC2Instance {
  def fromAWSInstance(awsInstance: Instance): EC2Instance = {
    EC2Instance(awsInstance.getInstanceId, awsInstance.getInstanceType, awsInstance.getState.getName == "running")
  }
}
