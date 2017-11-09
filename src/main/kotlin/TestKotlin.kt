import org.chocosolver.solver.Model
import org.chocosolver.solver.variables.IntVar


object TestKotlin {
  private operator fun IntVar.times(y: IntVar) {
    model.times(this, y, 4).post()
  }

  @JvmStatic
  fun main(args: Array<String>) {
    // 1. Create a Model
    val model = Model("my first problem")


    // 2. Create variables
    val x = model.intVar("X", 0, 5)                 // x in [0,5]
    val y = model.intVar("Y", intArrayOf(2, 3, 8))   // y in {2, 3, 8}
    // 3. Post constraints
    model.arithm(x, "+", y, "<", 5).post() // x + y < 5
    x * y             // x * y = 4
    // 4. Solve the problem
    model.solver.solve()
    // 5. Print the solution
    println(x) // Prints X = 2
    println(y) // Prints Y = 2
  }
}