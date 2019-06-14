package com.sumologic.tools.costs.ec2_ri.optimizer.ec2

import com.amazonaws.services.ec2.model.Instance
import com.sumologic.tools.costs.ec2_ri.optimizer.utils.AwsInstanceSizeNormalizator
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
    val iType = awsInstance.getInstanceType.split('.')
    Ec2Instance(
      awsInstance.getInstanceId,
      iType(0),
      AwsInstanceSizeNormalizator.normalize(iType(1)),
      awsInstance.getState.getName == "running"
    )
  }

  implicit lazy val formats = DefaultFormats

  def fromJsonString(jsonStringEc2Instances: String): Ec2Instance = {
    read[Ec2Instance](jsonStringEc2Instances)
  }

  def seqFromJsonString(jsonStringEc2Instance: String): Seq[Ec2Instance] = {
    read[Seq[Ec2Instance]](jsonStringEc2Instance)
  }

  def toListJsonString(ec2instances: Seq[Ec2Instance]): String = {
    write(ec2instances)
  }
}
