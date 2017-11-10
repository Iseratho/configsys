package kotlinchoco

import org.chocosolver.solver.constraints.Constraint
import org.chocosolver.solver.constraints.IIntConstraintFactory
import org.chocosolver.solver.variables.IntVar


fun Array<out Enum<*>>.toIntValues(): IntArray = this.map { it.ordinal }.toIntArray()
inline fun <reified T : Enum<T>> intValues(): IntArray {
    return enumValues<T>().map { it.ordinal }.toIntArray()
}

fun IIntConstraintFactory.arithm(variable: IntVar, op: String, cste: Enum<*>): Constraint {
    return this.arithm(variable, op, cste.ordinal)
}
