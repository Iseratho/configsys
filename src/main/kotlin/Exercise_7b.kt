import kotlinchoco.intValues
import org.chocosolver.solver.Model

object PidgeonHolePrinciple {
  @JvmStatic
  fun main(args: Array<String>) {
    val model = Model("pidgeon problem")
    val numPidgeons = 10
    val sumOfPidgeonValues = ((1+numPidgeons)*numPidgeons)/2
    val numHoles = 9
    val holes = model.intVarArray(numHoles, 0, numPidgeons)

    model.sum(holes, "=", sumOfPidgeonValues).post()
    model.allDifferent(*holes).post()

    val solution = model.solver.findSolution()

    println(sumOfPidgeonValues)
    println(solution)
  }
}