package sudokuGame;

// solves a single sudoku board
public class SingleSudokuSolver {
	SingleBoard Sudokuboard;
	
	public SingleSudokuSolver() {
		Sudokuboard = new SingleBoard("EnterBoard1");
	}
	
	public boolean solve() {
		return solve(Sudokuboard);
	}
	private boolean solve(SingleBoard testBoard) {
		// solves all the cells with one possible values
		testBoard.trySolve();
		// if board is solved, done
		if (testBoard.isSolved()) {
			Sudokuboard = testBoard;
			return true;
		}
		// if board is still solvable
		if (testBoard.isSolvable()) {
			// make a duplicate board
			SingleBoard newBoard = testBoard.clone();
			// find cell to guess value
			int[] coord = newBoard.findFirstNonEmpty();
			int row=coord[0], col=coord[1];			
			int testValue = (Integer)newBoard.board[row][col].pval.toArray()[0];
			// guess the value for the duplicate board
			newBoard.add(row, col, testValue);
							
			// if duplicate board is not solvable
			if (!solve(newBoard)) {
				// remove guessed value from possible values of original board
				testBoard.board[row][col].remove(testValue);
				// continue to solve original
				return solve(testBoard);
			} else {
				// guessed value is correct
				return true;
			}
		} else {
			// not solvable
			return false;
		}
	}
	
	public String toString() {
		return Sudokuboard.toString();
	}
	
	public static void main (String args[]) {
		SingleSudokuSolver test = new SingleSudokuSolver();
		System.out.println("Original: \n" + test);
		test.solve();
		System.out.println("Solved: \n" + test);
	}
}