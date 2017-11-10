import org.chocosolver.solver.Model
import kotlinchoco.*

object Exercise_2b_4 {
  enum class UsageType {
    INTERNET,
    MULTIMEDIA,
    SCIENTIFIC
  }

  enum class ClockRate {
    ONE,
    TWO,
    FOUR
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val model = Model("pc problem")

    val maxCPU = 4

    val numCPUs = model.intVar(1, maxCPU)
    val usageType = model.intVar(intValues<UsageType>())
    val clockRates = model.intVarArray(maxCPU, intValues<ClockRate>())

    for (i in 1 until clockRates.size) {
      val c5 = model.arithm(clockRates[0], "=", clockRates[i])
      val c6 = model.arithm(numCPUs, ">", i)
      model.ifThen(c6, c5)
    }

    for (i in 0 until clockRates.size) {
      val c5 = model.arithm(clockRates[i], "=", 0)
      val c6 = model.arithm(numCPUs, "<", i)
      model.ifThen(c6, c5)
    }

    val c1 = model.arithm(usageType, "=", UsageType.SCIENTIFIC)
    clockRates.forEach {
      val c2 = model.arithm(it, "=", ClockRate.FOUR)
      model.ifThen(c1, c2)
    }

    val c3 = model.arithm(usageType, "=", UsageType.MULTIMEDIA)
    clockRates.forEach {
      val c4 = model.arithm(it, "!=", ClockRate.ONE)
      model.ifThen(c3, c4)
    }

    val solutions = model.solver.findAllSolutions()

    solutions.forEach {
      val ut = UsageType.values()[it.getIntVal(usageType)]
      val crs = clockRates.map { cr ->
        ClockRate.values()[it.getIntVal(cr)]
      }
      println("UsageType: $ut, Clockrate: $crs")
    }

    println(solutions.size)
  }
}