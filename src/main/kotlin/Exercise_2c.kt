import org.chocosolver.solver.Model

object BicycleConfigurator {
  // mandatory
  class FrameType {
    class Triathlon(
        val f2t1: Int = 300,
        val f2t2: Int = 400
    )
    class Diamond(
        val f1d: Int = 150,
        val f2d: Int = 200
    )
    class StepThrough(
        val f1s: Int = 100
    )
  }

  //mandatory
  enum class BikeType {
    MOUNTAIN,
    CITY,
    RACING,
  }


  //optional
  class Lights(val l1: Int = 10)

  //mandatory
  class Wheels(
      val w1: Int = 50,
      val w2: Int = 20,
      val w3: Int = 40
  )

  // optional
  enum class UsageType {
    OFFROAD,
    SPEED,
    NIGHT,
  }

  class CustomerNeed (
      val usageTypes: List<UsageType>,
      val size: Int
  ) {
    val tall: Boolean = this.size > 24
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val model = Model("bicycle problem")

    // 2. Create variables
    val biketype = model.intVar("BikeType", BikeType.values().map { it.ordinal }.toIntArray())
    val price = model.intVar("price", 0, 1000)

    // 3. Post constraints
    // 4. Solve the problem
    model.setObjective(Model.MINIMIZE, price)
    model.solver.solve()
    // 5. Print the solution
    println(biketype) // Prints Y = 2
  }
}