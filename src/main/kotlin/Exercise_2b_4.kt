import org.chocosolver.solver.Model

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

  private fun Array<out Enum<*>>.toIntValues(): IntArray = this.map { it.ordinal }.toIntArray()

  @JvmStatic
  fun main(args: Array<String>) {
    val model = Model("pc problem")

    val maxCPU = 4

    val numCPUs = model.intVar(1, maxCPU)
    val usageType = model.intVar(UsageType.values().toIntValues())
    val clockRates = model.intVarArray(maxCPU, ClockRate.values().toIntValues())

    for (i in 1 until clockRates.size) {
      val c5 = model.arithm(clockRates[0], "=", clockRates[i])
      val c6 = model.arithm(numCPUs, ">", i)
      model.ifThen(c6, c5)
    }

    val c1 = model.arithm(usageType, "=", UsageType.SCIENTIFIC.ordinal)
    clockRates.forEach {
      val c2 = model.arithm(it, "=", ClockRate.FOUR.ordinal)
      model.ifThen(c1, c2)
    }

    val c3 = model.arithm(usageType, "=", UsageType.MULTIMEDIA.ordinal)
    clockRates.forEach {
      val c4 = model.arithm(it, "!=", ClockRate.ONE.ordinal)
      model.ifThen(c3, c4)
    }

    val solutions = model.solver.findAllSolutions()

    println(solutions.size)
  }
}