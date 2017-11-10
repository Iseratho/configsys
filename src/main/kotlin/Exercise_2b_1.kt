import org.chocosolver.solver.Model
import kotlinchoco.*

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


  @JvmStatic
  fun main(args: Array<String>) {
    val model = Model("pc problem")

    val usageType = model.intVar(UsageType.values().toIntValues())
    val clockRate = model.intVar(ClockRate.values().toIntValues())

    val c1 = model.arithm(usageType, "=", UsageType.SCIENTIFIC)
    val c2 = model.arithm(clockRate, "=", ClockRate.FOUR)
    model.ifThen(c1, c2)

    val c3 = model.arithm(usageType, "=", UsageType.MULTIMEDIA)
    val c4 = model.arithm(clockRate, "!=", ClockRate.ONE)
    model.ifThen(c3, c4)

    val solutions = model.solver.findAllSolutions()

    solutions.forEach {
      val ut = UsageType.values()[it.getIntVal(usageType)]
      val cr = ClockRate.values()[it.getIntVal(clockRate)]
      println("UsageType: $ut, Clockrate: $cr")
    }

    println(solutions.size)
  }
}