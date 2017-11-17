package kotlinchoco

import org.chocosolver.solver.Model
import org.chocosolver.solver.constraints.Constraint
import java.util.ArrayList

// Code ported from: https://github.com/chocoteam/choco-solver/issues/509
//QuickXPlain Algorithm	"Ulrich Junker"
//--------------------
//QuickXPlain(B, C={c1,c2,…, cm})
//IF consistent(B∪C) return "No conflict";
//IF isEmpty(C) return Φ;
//ELSE return QX(B, B, C);

//func QX(B,Δ, C={c1,c2, …, cq}): conflictSet Δ
//IF (Δ != Φ AND inconsistent(B)) return Φ; /* pruning of C */
//IF (C = {cα}) return({cα}); /* conflict element cα detected */
//k=n/2;
//C1 <-- {c1, …, ck}; C2 <-- {ck+1, …, cq}; /* B still consistent */
//D2 <-- QX(B ∪ C1, C1, C2);
//D1 <-- QX(B ∪ D2, D2, C1);
//return (D2 ∪ D1)

object QuickXplain {

  fun quickXPlain(b: MutableList<Constraint>, c: List<Constraint>, model: Model): List<Constraint> {
    val ac = ArrayList<Constraint>()
    ac.addAll(b)
    ac.addAll(c)
    //IF (is empty(C) or consistent(B ∪ C)) return Φ
    return if (c.isEmpty() || isConsistent(ac, model))
      emptyList()
    else { //ELSE return QX(B, B, C)
      qx(b, b, c, model)
    }
  }

  // func QX(B,Δ, C={c1,c2, …, cq}): conflictSet Δ
  private fun qx(b: MutableList<Constraint>, d: List<Constraint>, c: List<Constraint>, model: Model): List<Constraint> {
    val conflictSet = ArrayList<Constraint>()
    val cSize = c.size
    // IF (Δ != Φ AND inconsistent(B)) return Φ;
    if (!d.isEmpty() && !isConsistent(b, model))
      return emptyList()
    // if singleton(C) return C;
    if (cSize == 1)
      return c
    val k = cSize / 2  // k = q/2;
    // C1 = {c1..ck}; C2 = {ck+1..cq};
    val c1 = ArrayList<Constraint>()
    c1.addAll(c.subList(0, k))
    val c2 = ArrayList<Constraint>()
    c2.addAll(c.subList(k, cSize))
    //Saving B of the parent node
    val prevB = ArrayList<Constraint>()
    prevB.addAll(b)
    // D2 = QX(B ∪ C1, C1, C2);  // D1 = QX(B ∪ D2, D2, C1);
    val d2 = qx(constrsUnion(b, c1), c1, c2, model)
    conflictSet.addAll(d2)
    val d1 = qx(constrsUnion(prevB, d2), d2, c1, model)
    for (i in d1.indices)
      if (!conflictSet.contains(d1[i]))
        conflictSet.add(d1[i])
    return conflictSet
  }

  // Check if set of constraint is consistent
  private fun isConsistent(constrs: List<Constraint>, model: Model): Boolean {
    model.solver.reset()
    model.unpost(*model.cstrs)
    for (i in constrs.indices) {
      model.post(constrs[i])
    }
    return model.solver.solve()
  }

  //Calculate c1 ∪ c2
  fun constrsUnion(c1: MutableList<Constraint>, c2: List<Constraint>): MutableList<Constraint> {
    c1.addAll(c2)
    return c1
  }
}