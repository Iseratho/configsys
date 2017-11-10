import org.chocosolver.solver.Model
import org.chocosolver.solver.constraints.Constraint
import org.chocosolver.solver.constraints.IIntConstraintFactory
import org.chocosolver.solver.variables.IntVar

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
  private fun IIntConstraintFactory.arithm(variable: IntVar, op: String, cste: Enum<*>): Constraint {
    return this.arithm(variable, op, cste.ordinal)
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

    println(solutions.size)
  }
}