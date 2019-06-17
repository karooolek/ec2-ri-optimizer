package com.sumologic.tools.costs.ec2_ri.optimizer.utils

object AwsFamilyPriceNormalizator {
  def normalize(awsFamily: String): Float = {
    factors.get(awsFamily) match {
      case Some(v) => v
      case None => throw new RuntimeException(s"Normalization factor not known for $awsFamily")
    }
  }

  // TODO get costs from AWS documentation
  private val factors = Map[String, Float](
    "m1" -> 0.7f,
    "m2" -> 0.8f,
    "m3" -> 0.9f,
    "m4" -> 1.0f,
    "m4" -> 1.1f,
    "m5" -> 1.2f,
    "c1" -> 1.8f,
    "c2" -> 1.9f,
    "c3" -> 2.0f,
    "c4" -> 2.1f,
    "c5" -> 2.2f,
    "t1" -> 0.5f,
    "t2" -> 0.6f,
    "t3" -> 0.7f,
    "t4" -> 0.8f,
    "t5" -> 0.9f,
    "r1" -> 1.0f,
    "r2" -> 1.2f,
    "r3" -> 1.4f,
    "r4" -> 1.6f,
    "r5" -> 1.8f
  )
}
