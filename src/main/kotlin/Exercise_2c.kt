import kotlinchoco.arithm
import kotlinchoco.toIntValues
import org.chocosolver.solver.Model

object BicycleConfigurator {
  // mandatory
//  class FrameType {
//    class Triathlon(
//        val f2t1: Int = 300,
//        val f2t2: Int = 400
//    )
//    class Diamond(
//        val f1d: Int = 150,
//        val f2d: Int = 200
//    )
//    class StepThrough(
//        val f1s: Int = 100
//    )
//  }

  enum class FrameType {
    TRIATHLON,
    DIAMOND,
    STEP_THROUGH
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
      val isTall: Boolean
  )
//  {
//    val tall: Boolean = this.size > 24
//  }

  @JvmStatic
  fun main(args: Array<String>) {
    val model = Model("bicycle problem")

    // 2. Create variables
    val biketype = model.intVar("BikeType", BikeType.values().toIntValues())
    val price = model.intVar("price", 0, 1000)

//    val customerNeed = CustomerNeed(listOf(UsageType.OFFROAD), 22)

    val frame = model.intVar(FrameType.values().toIntValues())

    val needsOffroad = model.boolVar()
    val needsSpeed = model.boolVar()
    val needsNight = model.boolVar()

    val isMountainBike = model.arithm(biketype, "=", BikeType.MOUNTAIN)
    model.ifThen(needsOffroad, isMountainBike)
    val isRacingBike = model.arithm(biketype, "=", BikeType.RACING)
    model.ifThen(needsSpeed, isRacingBike)


    model.ifThen(isMountainBike, model.arithm(frame, "=", FrameType.DIAMOND))
//    model.ifThen()

    // 3. Post constraints
    // 4. Solve the problem
    model.setObjective(Model.MINIMIZE, price)
    val solutions = model.solver.findOptimalSolution(price, Model.MINIMIZE)

    // 5. Print the solution
    println(solutions) // Prints Y = 2
  }
}