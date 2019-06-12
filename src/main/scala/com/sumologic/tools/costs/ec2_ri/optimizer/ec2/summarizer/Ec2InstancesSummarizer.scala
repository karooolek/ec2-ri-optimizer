package com.sumologic.tools.costs.ec2_ri.optimizer.ec2.summarizer

import com.sumologic.tools.costs.ec2_ri.optimizer.ec2.Ec2InstancesSummary

trait Ec2InstancesSummarizer {
 def summarize() : Ec2InstancesSummary
}
