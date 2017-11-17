import org.chocosolver.solver.Model

object BoardProblem {
  @JvmStatic
  fun main(args: Array<String>) {
    val boardsize = 8

    val model = Model("board problem")
    val board = model.intVarMatrix(boardsize, boardsize, 0, 4)

    model.arithm(board[0][0], "=", 0).post()
    model.arithm(board[boardsize-1][boardsize-1], "=", 0).post()

    for (i in 0 until boardsize) {
      for (j in 0 until boardsize) {
        if (!((i == 0 && j == 0) || (i == boardsize-1 && j == boardsize-1))) {
          model.arithm(board[i][j], "!=", 0).post()
        }
        if (j < boardsize - 1) {
          model.ifOnlyIf(model.arithm(board[i][j], "=", 1), model.arithm(board[i][j + 1], "=", 2))
        }
//        if (j > 0) {
//          model.ifThen(model.arithm(board[i][j], "=", 2), model.arithm(board[i][j - 1], "=", 1))
//        }
        if (i < boardsize - 1) {
          model.ifOnlyIf(model.arithm(board[i][j], "=", 3), model.arithm(board[i + 1][j], "=", 4))
        }
//        if (i > 0 ) {
//          model.ifThen(model.arithm(board[i][j], "=", 4), model.arithm(board[i - 1][j], "=", 3))
//        }

        if (i == 0) {
          model.arithm(board[i][j], "!=", 4).post()
        }
        if (i == boardsize - 1) {
          model.arithm(board[i][j], "!=", 3).post()
        }

        if (j == 0) {
          model.arithm(board[i][j], "!=", 2).post()
        }
        if (j == boardsize - 1) {
          model.arithm(board[i][j], "!=", 1).post()
        }
      }
    }

    val solution = model.solver.solve()

    if (solution) {
      for (i in 0 until boardsize) {
        for (j in 0 until boardsize) {
          print(board[i][j].value)
        }
        println()
      }
    } else {
      println(solution)
    }
  }
}