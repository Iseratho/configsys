import org.chocosolver.solver.Model
import org.chocosolver.solver.variables.IntVar

object NQueensKotlin {
  @JvmStatic
  fun main(args: Array<String>) {
    val n = 8
    val model = Model(n.toString() + "-queens problem")
    val vars = arrayOfNulls<IntVar>(n)
    for (q in 0 until n) {
      vars[q] = model.intVar("Q_" + q, 1, n)
    }
    for (i in 0 until n - 1) {
      for (j in i + 1 until n) {
        model.arithm(vars[i], "!=", vars[j]).post()
        model.arithm(vars[i], "!=", vars[j], "-", j - i).post()
        model.arithm(vars[i], "!=", vars[j], "+", j - i).post()
      }
    }
    val solver = model.solver

    val solution = solver.findAllSolutions()
    if (solution != null) {
      println(solution.toString())
    }
    println(solution.size)
  }
}