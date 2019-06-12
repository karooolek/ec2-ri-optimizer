package com.sumologic.tools.costs.ec2_ri.optimizer

import com.amazonaws.services.ec2.model.Instance
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

case class EC2Instance(id: String, family: String, running: Boolean) {
  // nothing

  def toJsonString(): String = {
    write(this)(formats =  DefaultFormats)
  }
}

object EC2Instance {
  def fromAWSInstance(awsInstance: Instance): EC2Instance = {
    EC2Instance(awsInstance.getInstanceId, awsInstance.getInstanceType, awsInstance.getState.getName == "running")
  }

  def fromJsonString(jsonStringEc2Instance: String): EC2Instance = {
    read[EC2Instance](jsonStringEc2Instance)(formats =  DefaultFormats, mf = Manifest.classType(EC2Instance.getClass))
  }
}
