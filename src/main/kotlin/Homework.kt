import org.chocosolver.solver.Model

object ElbaTrekking {

  data class Route(
      val nodes: Pair<Places, Places>,
      val time: Int
  )

  data class Place(
      val name: String,
      val timeToReachIt: Int = 0,
      val hasIceCream: Boolean = false
  ) {
    val carPark: Boolean = timeToReachIt > 0
  }

  val places = listOf<Place>(
      Place("C. Cardello"),
      Place("La Cala"),
      Place("Maciarello", 50),
      Place("C. Pizenni", 30),
      Place("Ripa Barata"),
      Place("C. Bartoli"),
      Place("C. Ludovico"),
      Place("Le Casine", 40),
      Place("Marciana M.", hasIceCream = true),
      Place("Val Cappone"),
      Place("C. Lupi"),
      Place("Uccellaio", 70),
      Place("C. Bruschi"),
      Place("Costarello"),
      Place("Cava", 50)
  )

  enum class Places(name: String, timeToReachIt: Int = 0, val hasIceCream: Boolean = false) {
    Cardello("C. Cardello"),
    Cala("La Cala"),
    Maciarello("Maciarello", 50),
    Pizenni("C. Pizenni", 30),
    Ripa_Barata("Ripa Barata"),
    Bartoli("C. Bartoli"),
    Ludovico("C. Ludovico"),
    Casine("Le Casine", 40),
    Marciana("Marciana M.", hasIceCream = true),
    Cappone("Val Cappone"),
    Lupi("C. Lupi"),
    Uccellaio("Uccellaio", 70),
    Bruschi("C. Bruschi"),
    Costarello("Costarello"),
    Cava("Cava", 50)
  }

//  enum class PLACES(placeObject: Place) {
//    CARDELLO(Place("C. Cardello")),
//    Place("La Cala"),
//    Place("Maciarello", 50),
//    Place("C. Pizenni", 30),
//    Place("Ripa Barata"),
//    Place("C. Bartoli"),
//    Place("C. Ludovico"),
//    Place("Le Casine", 40),
//    Place("Marciana M.", hasIceCream = true),
//    Place("Val Cappone"),
//    Place("C. Lupi"),
//    Place("Uccellaio", 70),
//    Place("C. Bruschi"),
//    Place("Costarello"),
//    Place("Cava", 50)
//  }

  val topology = listOf(
      Route(Places.Cardello to Places.Cala, 20),
      Route(Places.Cala to Places.Maciarello, 30),
      Route(Places.Maciarello to Places.Pizenni, 25),
      Route(Places.Cardello to Places.Ripa_Barata, 20),
      Route(Places.Cardello to Places.Bartoli, 30),
      Route(Places.Maciarello to Places.Ludovico, 15),
      Route(Places.Ripa_Barata to Places.Bartoli, 40),
      Route(Places.Ludovico to Places.Casine, 15),
      Route(Places.Ripa_Barata to Places.Marciana, 25),
      Route(Places.Bartoli to Places.Lupi, 5),
      Route(Places.Ludovico to Places.Lupi, 20),
      Route(Places.Cappone to Places.Lupi, 15),
      Route(Places.Marciana to Places.Uccellaio, 10),
      Route(Places.Cappone to Places.Uccellaio, 30),
      Route(Places.Cappone to Places.Bruschi, 5),
      Route(Places.Lupi to Places.Costarello, 15),
      Route(Places.Uccellaio to Places.Bruschi, 25),
      Route(Places.Bruschi to Places.Costarello, 15),
      Route(Places.Costarello to Places.Cava, 10)
  )

  @JvmStatic
  fun main(args: Array<String>) {
    val minutes = 300
    val minTravel = topology.minBy { it.time }!!.time

    val model = Model("hiking problem")

    // Problem no usage of same two times
//    val routesUsed = model.boolVarArray(topology.size)


//    // alternative
//    // at which step was it used
//    val wayUsed = model.intVarArray(topology.size, 0, 100)

//    val wayUsed = model.boolVarMatrix(Places.values().size, Places.values().size)


//    // Fixed max length
//    val routesUsed = model.intVarArray(60, 0, topology.size - 1)



    val directedTopology = topology + topology.map { Route(it.nodes.first to it.nodes.second, it.time) }
    val routesUsed = model.setVar(intArrayOf(), directedTopology.indices.toList().toIntArray())

    val usedMinutes = model.intVar(0, minutes)

    // Must be within max minutes
    model.sumElements(routesUsed, directedTopology.map { it.time }.toIntArray(), usedMinutes).post()

    val numRoutesUsed = model.intVar(0, minutes/minTravel)

    // Num Routes must be same as routes used
    model.sumElements(routesUsed, directedTopology.map { 1 }.toIntArray(), numRoutesUsed).post()

    val placesVisited = model.setVar(intArrayOf(Places.Marciana.ordinal), Places.values().map { it.ordinal }.toIntArray())

    topology.forEachIndexed { index, route ->
//      model.reif
//      model.if
//      routesUsed.
    }

    val solution = model.solver.findSolution()
//    val solution = model.solver.findOptimalSolution(numRoutesUsed, Model.MAXIMIZE)
    if (solution != null) {
      println(solution.toString())
    }


    // sum of all bool var < minutes

    // route connected

    // define start point
    // icecream is fix place
    // route is correct loop????
    // return to start point

  }

}