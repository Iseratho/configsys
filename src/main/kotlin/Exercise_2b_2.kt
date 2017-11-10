import org.chocosolver.solver.Model
import org.chocosolver.solver.constraints.Constraint
import org.chocosolver.solver.constraints.IIntConstraintFactory
import org.chocosolver.solver.variables.IntVar

object Exercise_2b_2 {
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
  private fun IIntConstraintFactory.arithm(variable: IntVar, op: String, cste: Enum<*>): Constraint {
    return this.arithm(variable, op, cste.ordinal)
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val model = Model("pc problem")

    val numCPUs = 4

    val usageType = model.intVar(UsageType.values().toIntValues())
    val clockRates = model.intVarArray(numCPUs, ClockRate.values().toIntValues())

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

    println(solutions.size)
  }
}