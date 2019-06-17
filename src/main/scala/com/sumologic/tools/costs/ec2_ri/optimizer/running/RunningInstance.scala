package com.sumologic.tools.costs.ec2_ri.optimizer.running

import com.amazonaws.services.ec2.model.Instance
import com.sumologic.tools.costs.ec2_ri.optimizer.utils.AwsInstanceSizeNormalizator
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

case class RunningInstance(id: String, family: String, size: Double, running: Boolean) {
  // nothing

  implicit lazy val formats = DefaultFormats

  def toJsonString: String = {
    write(this)
  }
}

object RunningInstance {

  def fromAwsInstance(awsInstance: Instance): RunningInstance = {
    val iType = awsInstance.getInstanceType.split('.')
    RunningInstance(
      awsInstance.getInstanceId,
      iType(0),
      AwsInstanceSizeNormalizator.normalize(iType(1)),
      awsInstance.getState.getName == "running"
    )
  }

  implicit lazy val formats = DefaultFormats

  def fromJsonString(jsonStringRunningInstances: String): RunningInstance = {
    read[RunningInstance](jsonStringRunningInstances)
  }

  def seqFromJsonString(jsonStringRunningInstance: String): Seq[RunningInstance] = {
    read[Seq[RunningInstance]](jsonStringRunningInstance)
  }

  def toListJsonString(runningInstances: Seq[RunningInstance]): String = {
    write(runningInstances)
  }
}
