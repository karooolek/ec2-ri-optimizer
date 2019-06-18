package com.sumologic.tools.costs.ec2_ri.optimizer.analizer

import com.sumologic.tools.costs.ec2_ri.optimizer.reserved.{ReservedInstanceSummary, ReservedInstancesSummary}
import com.sumologic.tools.costs.ec2_ri.optimizer.running.RunningInstancesSummary
import org.scalatest._

class Ec2RiAnalizerSpec extends FlatSpec with Matchers {

  "A EC2 to RI analizer" should "give empty output for empty inputs" in {
    val ec2riAnalizer = new Ec2RiAnalizer(
      RunningInstancesSummary(
        Map[String, Double]()
      ),
      ReservedInstancesSummary(
        Map[String, ReservedInstanceSummary]())

    )
    ec2riAnalizer.analize() shouldBe new Ec2RiAnalysis(
      Map[String, Ec2RiSizeDiff](),
      Map[String, Double](),
      Map[String, Double](),
      0.0,
      0.0,
      0.0,
      Seq[Ec2RiConversion]()
    )
  }

  it should "suggest no conversions without under/over reserved instances" in {
    val ec2riAnalizer = new Ec2RiAnalizer(
      RunningInstancesSummary(
        Map[String, Double](
          "m1" -> 5.0,
          "c1" -> 10.0,
          "t1" -> 15.0
        )
      ),
      ReservedInstancesSummary(
        Map[String, ReservedInstanceSummary](
          "m1" -> ReservedInstanceSummary(5.0, 5.0),
          "c1" -> ReservedInstanceSummary(10.0, 10.0),
          "t1" -> ReservedInstanceSummary(15.0, 15.0)
        )
      )
    )
    ec2riAnalizer.analize().suggestedConversions.isEmpty shouldBe true
  }

  it should "suggest no conversions with overreserved m1 and without underreserved instances" in {
    val ec2riAnalizer = new Ec2RiAnalizer(
      RunningInstancesSummary(
        Map[String, Double](
          "m1" -> 3.0,
          "c1" -> 10.0,
          "t1" -> 15.0
        )
      ),
      ReservedInstancesSummary(
        Map[String, ReservedInstanceSummary](
          "m1" -> ReservedInstanceSummary(5.0, 5.0),
          "c1" -> ReservedInstanceSummary(10.0, 10.0),
          "t1" -> ReservedInstanceSummary(15.0, 15.0)
        )
      )
    )
    ec2riAnalizer.analize().suggestedConversions.isEmpty shouldBe true
  }

  it should "suggest no conversions without overreserved instances and with underreserved m1" in {
    val ec2riAnalizer = new Ec2RiAnalizer(
      RunningInstancesSummary(
        Map[String, Double](
          "m1" -> 3.0,
          "c1" -> 10.0,
          "t1" -> 15.0
        )
      ),
      ReservedInstancesSummary(
        Map[String, ReservedInstanceSummary](
          "m1" -> ReservedInstanceSummary(3.0, 3.0),
          "c1" -> ReservedInstanceSummary(10.0, 10.0),
          "t1" -> ReservedInstanceSummary(15.0, 15.0)
        )
      )
    )
    ec2riAnalizer.analize().suggestedConversions.isEmpty shouldBe true
  }

  it should "suggest m1->c1 conversion with overreserved m1 and underreserved c1" in {
    val ec2riAnalizer = new Ec2RiAnalizer(
      RunningInstancesSummary(
        Map[String, Double](
          "m1" -> 3.0,
          "c1" -> 12.0,
          "t1" -> 15.0
        )
      ),
      ReservedInstancesSummary(
        Map[String, ReservedInstanceSummary](
          "m1" -> ReservedInstanceSummary(5.0, 5.0),
          "c1" -> ReservedInstanceSummary(10.0, 10.0),
          "t1" -> ReservedInstanceSummary(15.0, 15.0)
        )
      )
    )
    ec2riAnalizer.analize().suggestedConversions(0).input._1 shouldBe "m1"
    ec2riAnalizer.analize().suggestedConversions(0).output._1 shouldBe "c1"
  }
}