package sudokuGame;

public class GeneralSudokuSolver {
	SudokuBoard Sudokuboard;
	
	public GeneralSudokuSolver() {
		// change here for different boards
		Sudokuboard = new SamuraiBoard();
	}
	
	public boolean solve() {
		return solve(Sudokuboard);
	}
	
	private boolean solve(SudokuBoard testBoard) {
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
			SudokuBoard newBoard = (SudokuBoard)testBoard.clone();
			// find cell to guess value
			int[] coord = newBoard.findFirstNonEmpty();
			int row=coord[0], col=coord[1], boardNum=1;
			if (!(newBoard instanceof SingleBoard)) {
				boardNum=coord[2];
			} 
			int testValue = (Integer)newBoard.getBoard(boardNum)[row][col].pval.toArray()[0];
			// guess the value for the duplicate board
			newBoard.add(row, col, testValue, boardNum);
			// if duplicate board is not solvable
			if (!solve(newBoard)) {
				// remove guessed value from possible values of original board
				testBoard.getBoard(boardNum)[row][col].remove(testValue);
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
		GeneralSudokuSolver test = new GeneralSudokuSolver();
		System.out.println("Original: \n" + test);
		test.solve();
		//test.test();
		System.out.println("Solved: \n" + test);
	}
}