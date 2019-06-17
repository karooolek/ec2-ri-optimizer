package com.sumologic.tools.costs.ec2_ri.optimizer.converter.aws

import com.sumologic.tools.costs.ec2_ri.optimizer.analizer.Ec2RiConversion
import com.sumologic.tools.costs.ec2_ri.optimizer.converter.ReservedInstancesConverter
import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.ReservedInstance

class AwsReservedInstancesConverter(awsRegion: String,
                                    awsKey: String,
                                    awsSecret: String,
                                    reservedInstances: Seq[ReservedInstance],
                                    conversions: Seq[Ec2RiConversion]) extends ReservedInstancesConverter(reservedInstances, conversions) {
  override def convert(): Seq[ReservedInstance] = {
    // TODO
    null
  }
}
