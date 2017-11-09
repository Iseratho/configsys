import org.chocosolver.solver.Model

object Exercise_2b_3 {
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

    val numCPUs = 4

    val usageType = model.intVar(UsageType.values().toIntValues())
    val clockRates = model.intVarArray(numCPUs, ClockRate.values().toIntValues())

    for (i in 1 until clockRates.size) {
      model.arithm(clockRates[0], "=", clockRates[i]).post()
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