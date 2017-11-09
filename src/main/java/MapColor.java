//package src;
/**
 * General solver for mapcoloring.
 * Input: table of neighbourships supplied by method calls
 *        as pairs of country names (second is null if first has no neighbour)
 */

//import static choco.Choco.*;

import java.util.ArrayList;
import java.util.List;

public class MapColor {

  public static void main(String[] args) {
    int nrColors = 3; // may be changed for trying different numbers of colors
    String[][] nb = getAustralia(); // or Africa or ...
    List<String> countries = getCountries(nb);

    // TODO: add code for solution here

  }

  private static String[][] getAustralia() {
    return new String[][]{
        {"WA","NT"},{"WA","SA"},{"NT","SA"},{"NT","Q"},{"SA","Q"},
        {"SA","NSW"},{"SA","V"},{"Q","NSW"},{"NSW","V"},{"T",null}};
  }

  private static String[][] getAustria() {
    return new String[][]{
        {"V","T"},{"T","S"},{"T","K"},{"S","K"},{"S","St"},
        {"S","O"},{"K","St"},{"St","O"},{"St","N"},{"St","B"},
        {"O","N"},{"N","W"},{"N","B"}};
  }

  private static String[][] getAfrica() {
    return new String[][]{
        {"SAF","LES"},{"SAF","SWL"},{"SAF","NAM"},{"SAF","BOT"},{"SAF","ZIM"},
        {"SAF","MOZ"},{"SWL","MOZ"},{"NAM","BOT"},{"NAM","ZIM"},{"NAM","ZAM"},
        {"NAM","ANG"},{"BOT","ZIM"},{"BOT","ZAM"},{"ZIM","MOZ"},{"ZIM","ZAM"},
        {"MOZ","MAL"},{"MOZ","ZAM"},{"MOZ","TAN"},{"MAL","ZAM"},{"MAL","TAN"},
        {"ZAM","ANG"},{"ZAM","RDK"},{"ZAM","TAN"},{"ANG","KON"},{"ANG","RDK"},
        {"KON","GAB"},{"KON","RDK"},{"RDK","UGA"},{"RDK","RWA"},{"RDK","BUR"},
        {"RDK","TAN"},{"UGA","KEN"},{"UGA","RWA"},{"KEN","RWA"},{"KEN","SOM"},
        {"RWA","BUR"},{"BUR","TAN"},{"MDG",null}};
  }

  private static List<String> getCountries(String[][] nb) {
    List<String> countries = new ArrayList<String>();
    for (String[] pair : nb) {
      for (String country : pair) {
        if (country == null) continue;
        if (countries.contains(country)) continue;
        countries.add(country);
      }
    }
    return countries;
  }

}

