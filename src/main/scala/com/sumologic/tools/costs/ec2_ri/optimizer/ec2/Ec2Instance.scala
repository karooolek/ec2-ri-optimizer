package com.sumologic.tools.costs.ec2_ri.optimizer.ec2

import com.amazonaws.services.ec2.model.Instance
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

case class Ec2Instance(id: String, family: String, size: Double, running: Boolean) {
  // nothing

  def toJsonString: String = {
    write(this)(formats = DefaultFormats)
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

  def fromJsonString(jsonStringEc2Instance: String): Ec2Instance = {
    read[Ec2Instance](jsonStringEc2Instance)(formats = DefaultFormats, mf = Manifest.classType(Ec2Instance.getClass))
  }

  def seqFromJsonString(jsonStringEc2Instance: String): Seq[Ec2Instance] = {
    read[Seq[Ec2Instance]](jsonStringEc2Instance)(formats = DefaultFormats, mf = Manifest.classType(Ec2Instance.getClass))
  }

  def toListJsonString(ec2instances: Seq[Ec2Instance]): String = {
    write(ec2instances)(formats = DefaultFormats)
  }
}
