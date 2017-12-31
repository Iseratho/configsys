import org.chocosolver.graphsolver.GraphModel
import org.chocosolver.graphsolver.variables.DirectedGraphVar
import org.chocosolver.solver.Model
import org.chocosolver.util.objects.graphs.DirectedGraph
import org.chocosolver.util.objects.setDataStructures.SetType

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

  val places = listOf(
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

  enum class Places(name: String, val timeToReachIt: Int = 0, val hasIceCream: Boolean = false) {
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
    var maxMinutes = 300
    var haveToBeVisited = intArrayOf(Places.Marciana.ordinal)
    var startPoints = Places.values().filter { it.timeToReachIt > 0 }

    println("Questions 1 & 2")
    calculateRoute(maxMinutes, haveToBeVisited, startPoints)
    println("\n\n=======================\n\n")

    println("Question 3")
    maxMinutes = 260 // since 40 minutes are lost at shopping
    haveToBeVisited = intArrayOf(Places.Cava.ordinal,
        Places.Costarello.ordinal,
        Places.Bruschi.ordinal,
        Places.Uccellaio.ordinal,
        Places.Marciana.ordinal)
    startPoints = listOf(Places.Cava)

    calculateRoute(maxMinutes, haveToBeVisited, startPoints)
  }

  fun calculateRoute(maxMinutes: Int, haveToBeVisited: IntArray, startPoints: List<Places>) {
    val n = Places.values().size

    val model = GraphModel("hiking problem")
    val usedMinutes = model.intVar("usedMinutes", 0, maxMinutes)

    val GLB = DirectedGraph(model, n, SetType.BITSET, false)
    val GUB = DirectedGraph(model, n, SetType.BITSET, false)

    val startPoint = model.intVar("startpoint", 0, startPoints.size-1)

    // construct graph
    places.forEachIndexed { index1, place1 ->
      GUB.addNode(index1)
      places.forEachIndexed { index2, place2 ->
        val from = Places.values()[index1]
        val to = Places.values()[index2]
        val exists = topology.firstOrNull {
          it.nodes.first == from && it.nodes.second == to ||
              it.nodes.first == to && it.nodes.second == from
        }
        if (exists != null) {
          GUB.addArc(index1, index2)
          GUB.addArc(index2, index1)
        }
      }
    }

    // must be in solution
    haveToBeVisited.forEach {
      GLB.addNode(it)
    }

    val g = model.digraphVar("G", GLB, GUB)
    model.circuit(g).post()

    val numVisitedPlaces = model.nbNodes(g)

    val boolVarMatrix = model.adjacencyMatrix(g)

    // Cost constraints
    val routesUsed = model.setVar(intArrayOf(), topology.mapIndexed { index, route -> index }.toIntArray())
    val routeCosts = topology.mapIndexed { index, route -> route.time }.toIntArray()

//    topology.forEachIndexed { index, route ->
//      val from = route.nodes.first
//      val to = route.nodes.second
//      val isArc = model.boolVar()
//      model.arcChanneling(g, isArc, from.ordinal, to.ordinal).post()
//      val inSetConstraint = model.member(index, routesUsed)
//      model.ifThen(isArc, inSetConstraint)
//    }

    topology.forEachIndexed { index, route ->
      val from = route.nodes.first.ordinal
      val to = route.nodes.second.ordinal
      val inSetConstraint = model.member(index, routesUsed)
      model.ifThen(boolVarMatrix[from][to], inSetConstraint)
    }
    model.sumElements(routesUsed, routeCosts, usedMinutes)

    // Start point constraints
    startPoints.forEachIndexed { index, place ->
      val selectedStartPoint = model.arithm(startPoint, "=", index)

      val isStartPointVar = model.boolVar(true)
      val nodeInGraphConstraint = model.nodeChanneling(g, isStartPointVar, place.ordinal)

      model.ifThen(selectedStartPoint, nodeInGraphConstraint)

      val adjustedMaxMinutes = maxMinutes - place.timeToReachIt
      val maxMinutesConstraint = model.arithm(usedMinutes, "<=", adjustedMaxMinutes)

      model.ifThen(selectedStartPoint, maxMinutesConstraint)
    }

    model.setObjective(Model.MAXIMIZE, numVisitedPlaces)
//    model.setObjective(Model.MINIMIZE, usedMinutes)

    val solver = model.solver

    var bestSolutionValue = -1
    while (solver.solve()){
      println("Solution Found:")
      val startPointPlace = startPoints[startPoint.value]
      val totalMinutes = usedMinutes.value + startPointPlace.timeToReachIt
      println("time: $totalMinutes minutes")
      println("# nodes: $numVisitedPlaces")
      println("startpoint: ${startPointPlace.name}")
      printGraph(g)
      testSolutionIsUnderMaxTime(g, startPointPlace, maxMinutes)
      testSolutionIsConnected(g)
      testSolutionContainsStartAndIceCreamPlace(g, startPointPlace, haveToBeVisited)
      bestSolutionValue = usedMinutes.value
      println("\n=================\n")
    }
    println(bestSolutionValue)

    val export = g.graphVizExport()
    println(export)
  }

  fun printGraph(g: DirectedGraphVar) {
    var graphString = g.toString().replace("{", "").replace(" }", "").replace("}", "")
    for (i in places.size-1 downTo 0) {
      val place = places[i]
      graphString = graphString.replace("$i", place.name)
    }
    println(graphString)
  }

  fun testSolutionContainsStartAndIceCreamPlace(g: DirectedGraphVar, startPoint: Places, mandatoryPlaces: IntArray) {
    val visited = mutableListOf<Int>()
    val graphStringRows = g.toString().replace("{", "").replace(" }", "").split("\n")
    graphStringRows.forEach {
      if (it.contains("->")) {
        val values = it.split(" -> ")
        visited.add(values[0].toInt())
        visited.add(values[1].toInt())
      }
    }
    var bool = true
    assert(visited.contains(startPoint.ordinal))
    if (!visited.contains(startPoint.ordinal)) {
      bool = false
    }
    mandatoryPlaces.forEach {
      assert(visited.contains(it))
      if (!visited.contains(it)) {
        bool = false
      }
    }
    // Used when assertations are disabled
    if (bool == false) {
      println("Mandatory places constraint violated")
    }
  }

  fun testSolutionIsConnected(g: DirectedGraphVar) {
    val fromPlaces = mutableListOf<Int>()
    val toPlaces = mutableListOf<Int>()
    val graphStringRows = g.toString().replace("{", "").replace(" }", "").split("\n")
    graphStringRows.forEach {
      if (it.contains("->")) {
        val values = it.split(" -> ")
        val from = Places.values()[values[0].toInt()]
        val to = Places.values()[values[1].toInt()]
        fromPlaces.add(from.ordinal)
        toPlaces.add(to.ordinal)
      }
    }
    val intersection = fromPlaces.intersect(toPlaces)
    assert(intersection.size == fromPlaces.size)
    // Used when assertations are disabled
    if (!(intersection.size == fromPlaces.size)) {
      println("Connection constraint violated")
    }
  }

  fun testSolutionIsUnderMaxTime(g: DirectedGraphVar, startPoint: Places, maxMinutes: Int) {
    var usedMinutes = 0
    val graphStringRows = g.toString().replace("{", "").replace(" }", "").split("\n")
    graphStringRows.forEach {
      if (it.contains("->")) {
        val values = it.split(" -> ")
        val from = Places.values()[values[0].toInt()]
        val to = Places.values()[values[1].toInt()]
        val time = topology.find { (it.nodes.first == from && it.nodes.second == to) ||
            (it.nodes.second == from && it.nodes.first == to)
        }!!.time
        usedMinutes += time
      }
    }
    val remainingMinutes = maxMinutes - startPoint.timeToReachIt
    assert(usedMinutes <= remainingMinutes)
    // Used when assertations are disabled
    if (!(usedMinutes <= remainingMinutes)) {
      println("Time constraint violated")
    }
  }
}