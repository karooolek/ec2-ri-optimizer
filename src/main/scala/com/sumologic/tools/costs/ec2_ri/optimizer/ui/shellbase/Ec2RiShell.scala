package com.sumologic.tools.costs.ec2_ri.optimizer.ui.shellbase

import com.sumologic.shellbase.{ShellBase, ShellCommand}

object Ec2RiShell extends ShellBase("ec2ri-optimizer") {
  override def commands: Seq[ShellCommand] = {
    Seq(
      new Ec2RiOptimizerCommandSet
    )
  }
}
