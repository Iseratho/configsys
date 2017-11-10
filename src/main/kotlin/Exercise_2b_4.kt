import org.chocosolver.solver.Model
import org.chocosolver.solver.constraints.Constraint
import org.chocosolver.solver.constraints.IIntConstraintFactory
import org.chocosolver.solver.variables.IntVar

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
  private inline fun <reified T : Enum<T>> intValues(): IntArray {
    return enumValues<T>().map { it.ordinal }.toIntArray()
  }
  private fun IIntConstraintFactory.arithm(variable: IntVar, op: String, cste: Enum<*>): Constraint {
    return this.arithm(variable, op, cste.ordinal)
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