//package src;
/**
 * General solver for mapcoloring.
 * Input: table of neighbourships supplied by method calls
 * as pairs of country names (second is null if first has no neighbour)
 */

//import static choco.Choco.*;

import org.chocosolver.solver.Model
import org.chocosolver.util.ESat
import java.util.ArrayList

object MapColorKotlin {

  private val australia: Array<Array<String>>
    get() =
      arrayOf(arrayOf("WA", "NT"), arrayOf("WA", "SA"), arrayOf("NT", "SA"), arrayOf("NT", "Q"), arrayOf("SA", "Q"), arrayOf("SA", "NSW"), arrayOf("SA", "V"), arrayOf("Q", "NSW"), arrayOf("NSW", "V"), arrayOf("T", ""))

  private val austria: Array<Array<String>>
    get() =
      arrayOf(arrayOf("V", "T"), arrayOf("T", "S"), arrayOf("T", "K"), arrayOf("S", "K"), arrayOf("S", "St"), arrayOf("S", "O"), arrayOf("K", "St"), arrayOf("St", "O"), arrayOf("St", "N"), arrayOf("St", "B"), arrayOf("O", "N"), arrayOf("N", "W"), arrayOf("N", "B"))

  private val africa: Array<Array<String>>
    get() =
      arrayOf(arrayOf("SAF", "LES"), arrayOf("SAF", "SWL"), arrayOf("SAF", "NAM"), arrayOf("SAF", "BOT"), arrayOf("SAF", "ZIM"), arrayOf("SAF", "MOZ"), arrayOf("SWL", "MOZ"), arrayOf("NAM", "BOT"), arrayOf("NAM", "ZIM"), arrayOf("NAM", "ZAM"), arrayOf("NAM", "ANG"), arrayOf("BOT", "ZIM"), arrayOf("BOT", "ZAM"), arrayOf("ZIM", "MOZ"), arrayOf("ZIM", "ZAM"), arrayOf("MOZ", "MAL"), arrayOf("MOZ", "ZAM"), arrayOf("MOZ", "TAN"), arrayOf("MAL", "ZAM"), arrayOf("MAL", "TAN"), arrayOf("ZAM", "ANG"), arrayOf("ZAM", "RDK"), arrayOf("ZAM", "TAN"), arrayOf("ANG", "KON"), arrayOf("ANG", "RDK"), arrayOf("KON", "GAB"), arrayOf("KON", "RDK"), arrayOf("RDK", "UGA"), arrayOf("RDK", "RWA"), arrayOf("RDK", "BUR"), arrayOf("RDK", "TAN"), arrayOf("UGA", "KEN"), arrayOf("UGA", "RWA"), arrayOf("KEN", "RWA"), arrayOf("KEN", "SOM"), arrayOf("RWA", "BUR"), arrayOf("BUR", "TAN"), arrayOf<String>("MDG", ""))

  private val colorMap: List<String> = listOf("Red", "Green", "Blue", "Yellow")

  @JvmStatic
  fun main(args: Array<String>) {
    val nrColors = 4 // may be changed for trying different numbers of colors
    val nb = africa // or Africa or ...
    val countries = getCountries(nb)

    // Solution code

    val model = Model("map coloring problem")
    val colors = model.intVarArray("color", countries.size, 1, nrColors)

    for (i in 0 until countries.size - 1) {
      for (j in i + 1 until countries.size - 1) {
        val country1 = countries[i]
        val country2 = countries[j]
        if (nb.any { (it[0] == country1 && it[1] == country2) || (it[0] == country2 && it[1] == country1) }) {
          model.arithm(colors[i], "!=", colors[j]).post()
        }
      }
    }

    val solver = model.solver
    solver.solve()
    if (solver.isFeasible == ESat.TRUE) {
      colors.forEachIndexed { index, intVar ->
        println("${countries[index]}: ${colorMap[intVar.value - 1]}")
      }
    } else {
      println("not SAT")
    }

    //    val solution = solver.findSolution()
//    if (solution != null) {
//      println(solution.toString())
//    }
  }

  private fun getCountries(nb: Array<Array<String>>): List<String> {
    val countries = ArrayList<String>()
    for (pair in nb) {
      for (country in pair) {
        if (country.isEmpty()) continue
        if (countries.contains(country)) continue
        countries.add(country)
      }
    }
    return countries
  }

}

