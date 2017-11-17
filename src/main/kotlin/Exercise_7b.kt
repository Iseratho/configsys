import org.chocosolver.solver.Model

object PidgeonHolePrinciple {
  @JvmStatic
  fun main(args: Array<String>) {
    val model = Model("pidgeon problem")
    val numPidgeons = 10
    val sumOfPidgeonValues = ((1+numPidgeons)*numPidgeons)/2
    val numHoles = 9
    val holes = model.setVar(numHoles, 1, numPidgeons)
    // constraint nuber to numHoles

    val intvar = model.intVar(sumOfPidgeonValues)

    model.sum(holes, intvar)

    val solution = model.solver.findSolution()

    println(sumOfPidgeonValues)
    println(solution)
  }
}