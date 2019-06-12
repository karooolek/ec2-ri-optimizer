package com.sumologic.tools.costs.ec2_ri.optimizer.ec2

import com.amazonaws.services.ec2.model.Instance
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

case class Ec2Instance(id: String, family: String, size: Double, running: Boolean) {
  // nothing

  implicit lazy val formats = DefaultFormats

  def toJsonString: String = {
    write(this)
  }
}

object Ec2Instance {

  def fromAwsInstance(awsInstance: Instance): Ec2Instance = {
    Ec2Instance(
      awsInstance.getInstanceId,
      awsInstance.getInstanceType.split('.')(0),
      NormalizationFactor.apply(awsInstance.getInstanceType.split('.')(1)),
      awsInstance.getState.getName == "running"
    )
  }

  implicit lazy val formats = DefaultFormats

  def fromJsonString(jsonStringEc2Instance: String): Ec2Instance = {
    read[Ec2Instance](jsonStringEc2Instance)
  }

  def seqFromJsonString(jsonStringEc2Instance: String): Seq[Ec2Instance] = {
    read[Seq[Ec2Instance]](jsonStringEc2Instance)
  }

  def toListJsonString(ec2instances: Seq[Ec2Instance]): String = {
    write(ec2instances)
  }
}
