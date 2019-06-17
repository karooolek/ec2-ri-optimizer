package com.sumologic.tools.costs.ec2_ri.optimizer.converter

import com.sumologic.tools.costs.ec2_ri.optimizer.analizer.Ec2RiConversion
import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstance

abstract class ReservedInstancesConverter(reservedInstances: Seq[ReservedInstance], conversions: Seq[Ec2RiConversion]) {
  def convert(): Seq[ReservedInstance]
}
