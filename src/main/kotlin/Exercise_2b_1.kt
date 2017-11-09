import org.chocosolver.solver.Model

object PcConfigurator {
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

    val usageType = model.intVar(UsageType.values().toIntValues())
    val clockRate = model.intVar(ClockRate.values().toIntValues())

    val c1 = model.arithm(usageType, "=", UsageType.SCIENTIFIC.ordinal)
    val c2 = model.arithm(clockRate, "=", ClockRate.FOUR.ordinal)
    model.ifThen(c1, c2)

    val c3 = model.arithm(usageType, "=", UsageType.MULTIMEDIA.ordinal)
    val c4 = model.arithm(clockRate, "!=", ClockRate.ONE.ordinal)
    model.ifThen(c3, c4)

    val solutions = model.solver.findAllSolutions()

    println(solutions.size)
  }
}