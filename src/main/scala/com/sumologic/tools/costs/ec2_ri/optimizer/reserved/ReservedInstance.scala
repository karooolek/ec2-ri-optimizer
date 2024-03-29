package com.sumologic.tools.costs.ec2_ri.optimizer.reserved

import com.amazonaws.services.ec2.model.ReservedInstances
import com.sumologic.tools.costs.ec2_ri.optimizer.utils.AwsInstanceSizeNormalizator
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

case class ReservedInstance(id: String, family: String, size: Double, active: Boolean, convertible: Boolean) {
  // nothing

  implicit lazy val formats = DefaultFormats

  def toJsonString: String = {
    write(this)
  }
}

object ReservedInstance {

  def fromAwsReservedInstance(awsReservedInstances: ReservedInstances): ReservedInstance = {
    val iType = awsReservedInstances.getInstanceType.split('.')
    ReservedInstance(
      awsReservedInstances.getReservedInstancesId,
      iType(0),
      awsReservedInstances.getInstanceCount * AwsInstanceSizeNormalizator.normalize(iType(1)),
      awsReservedInstances.getState == "active",
      awsReservedInstances.getOfferingClass == "convertible"
    )
  }

  implicit lazy val formats = DefaultFormats

  def fromJsonString(jsonStringReservedInstance: String): ReservedInstance = {
    read[ReservedInstance](jsonStringReservedInstance)
  }

  def seqFromJsonString(jsonStringReservedInstances: String): Seq[ReservedInstance] = {
    read[Seq[ReservedInstance]](jsonStringReservedInstances)
  }

  def toListJsonString(reservedInstances: Seq[ReservedInstance]): String = {
    write(reservedInstances)
  }
}
