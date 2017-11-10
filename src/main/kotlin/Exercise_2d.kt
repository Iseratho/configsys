import kotlinchoco.arithm
import kotlinchoco.toIntValues
import org.chocosolver.solver.Model


object BusConfiguration {
  enum class BusType {
    TOURIST,
    NORMAL
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val maxPower = 2000
    val maxSeats = 80

    val model = Model("bus problem")
    val numSeats = model.intVar("#seats", 0, maxSeats)
    val numDeluxeSeats = model.intVar("#deluxe", 0, maxSeats)
    val numLCDs = model.intVar("#lcd", 0, maxSeats)
    val busType = model.intVar("type", BusType.values().toIntValues())
    val numDVDplayers = model.intVar("#dvd", 0, 4)
    val powerConsumption = model.intVar("power", 0, maxPower)
    val powerConsumptionDVD = model.intVar("pDVD", 0, maxPower)
    val powerConsumptionLamps = model.intVar("pLamps", 0, maxPower)
    val powerConsumptionLCDs = model.intVar("pLCD", 0, maxPower)
    val powerConsumptionSeats = model.intVar("pSeat", 0, maxPower)

    model.arithm(powerConsumptionSeats, "=", powerConsumptionLCDs, "+", powerConsumptionLamps).post()
    model.arithm(powerConsumption, "=", powerConsumptionSeats, "+", powerConsumptionDVD).post()

    val isTourist = model.arithm(busType, "=", BusType.TOURIST)
    model.ifThen(isTourist, model.arithm(numSeats, "=", numDeluxeSeats))

    model.arithm(numDeluxeSeats, "<=", numSeats).post()

    model.ifThen(model.arithm(numLCDs, ">", 0), model.arithm(numDVDplayers, ">", 0))
    model.ifThen(model.arithm(numLCDs, ">", 20), model.arithm(numDVDplayers, ">", 1))

    model.ifThen(isTourist, model.arithm(numLCDs, "=", numSeats))

    val seatC = model.and(model.arithm(numSeats, ">", 20), isTourist)
    model.ifThen(seatC, model.arithm(numDVDplayers, ">=", 2))

    model.times(numDVDplayers, 50, powerConsumptionDVD).post()
    model.times(numSeats, 25, powerConsumptionLamps).post()
    model.times(numDeluxeSeats, 15, powerConsumptionLCDs).post()

    model.arithm(numLCDs, "=", numDeluxeSeats).post()

    // task 1
    model.arithm(numSeats, ">", 30).post()
    model.arithm(numLCDs, "<", 5).post()

    // task 2
//    model.arithm(numSeats, ">", 30).post()
//    model.arithm(busType, "=", BusType.TOURIST).post()


    // task 3
//    model.arithm(busType, "=", BusType.TOURIST).post()
//
//    // task 3 if 4 DVDs required
//    model.arithm(numDVDplayers, "=", 4).post()
//    val solutions = model.solver.findAllOptimalSolutions(numSeats, Model.MAXIMIZE)

    val solutions = model.solver.findAllSolutions()
    println(solutions.size)

    solutions.takeLast(5).forEach {
      println(it)
    }

  }
}