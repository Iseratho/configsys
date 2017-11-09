import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.util.List;

public class NQueens {
  public static void main(String[] args) {
    int n = 8;
    Model model = new Model(n + "-queens problem");
    IntVar[] vars = new IntVar[n];
    for(int q = 0; q < n; q++){
      vars[q] = model.intVar("Q_"+q, 1, n);
    }
    for(int i  = 0; i < n-1; i++) {
      for (int j = i + 1; j < n; j++) {
        model.arithm(vars[i], "!=", vars[j]).post();
        model.arithm(vars[i], "!=", vars[j], "-", j - i).post();
        model.arithm(vars[i], "!=", vars[j], "+", j - i).post();
      }
    }
    Solver solver = model.getSolver();

    // Solve All and print manually
//    while (solver.solve()) {
//      for(int q = 0; q < n; q++) {
//        System.out.println(vars[q]);
//      }
//    }

    // Solve one
//    Solution solution = solver.findSolution();
//    if(solution != null){
//      System.out.println(solution.toString());
//    }

    List<Solution> solution = solver.findAllSolutions();
    if(solution != null){
      System.out.println(solution.toString());
    }
  }
}
