package com.sumologic.tools.costs.ec2_ri.optimizer.utils

object AwsInstanceSizeNormalizator {
  def normalize(instanceSize: String): Float = {
    factors.get(instanceSize) match {
      case Some(v) => v
      case None => throw new RuntimeException(s"Normalization factor not known for $instanceSize")
    }
  }

  // normalization factors from AWS documentation: http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/apply_ri.html
  private val factors = Map[String, Float](
    "nano" -> 0.25f,
    "micro" -> 0.5f,
    "small" -> 1f,
    "medium" -> 2f,
    "large" -> 4f,
    "xlarge" -> 8f,
    "2xlarge" -> 16f,
    "4xlarge" -> 32f,
    "8xlarge" -> 64f,
    "9xlarge" -> 72f,
    "10xlarge" -> 80f,
    "12xlarge" -> 96f,
    "16xlarge" -> 128f,
    "18xlarge" -> 144f,
    "24xlarge" -> 192f,
    "32xlarge" -> 256f
  )
}
