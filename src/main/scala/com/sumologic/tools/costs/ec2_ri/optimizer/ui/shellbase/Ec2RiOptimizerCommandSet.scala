package com.sumologic.tools.costs.ec2_ri.optimizer.ui.shellbase

import com.sumologic.shellbase.{ShellCommand, ShellCommandSet}
import com.sumologic.tools.costs.ec2_ri.optimizer.Ec2RiOptimizer
import org.apache.commons.cli.CommandLine

class Ec2RiOptimizerCommandSet extends ShellCommandSet("ec2ri-optimizer", "Amazon Web Services EC2 running instances and RI reserved instances optimizer tool") {

  commands += new ShellCommand("analize", "Analizes current running and reserved instances") {
    override def execute(cmdLine: CommandLine): Boolean = {
      val accountsFilename = cmdLine.getOptionValue("accounts")
      val ec2riAnalysis = new Ec2RiOptimizer(accountsFilename).analize()
      println("under-reserved instances:")
      println(ec2riAnalysis.underReservedFamiliesPrices)
      println("over-reserved instances:")
      println(ec2riAnalysis.overReservedFamiliesPrices)
      true
    }
  }

  commands += new ShellCommand("suggest", "Suggests reserved instances conversions") {
    override def execute(cmdLine: CommandLine): Boolean = {
      val accountsFilename = cmdLine.getOptionValue("accounts")
      val ec2riAnalysis = new Ec2RiOptimizer(accountsFilename).analize()
      println("reserved instances conversions suggestions:")
      println(ec2riAnalysis.suggestedConversions)
      true
    }
  }
}
