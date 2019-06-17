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
    "m1" -> 0.25f,
    "m2" -> 0.5f,
    "m3" -> 0.75f,
    "m4" -> 1.0f,
    "m4" -> 1.25f,
    "m5" -> 1.5f,
    "c1" -> 0.5f,
    "c2" -> 1.0f,
    "c3" -> 1.5f,
    "c4" -> 2.0f,
    "c5" -> 2.5f
  )
}
