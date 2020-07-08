package sudokuGame;

public class GeneralSudokuSolver {
	SudokuBoard Sudokuboard;
	
	public GeneralSudokuSolver() {
		// change here for different boards
		Sudokuboard = new SingleBoard();
	}
	
	public boolean solve() {
		return solve(Sudokuboard);
	}
	
	private boolean solve(SudokuBoard testBoard) {
		testBoard.trySolve();
		if (testBoard.isSolved()) {
			Sudokuboard = testBoard;
			return true;
		}
		if (testBoard.isSolvable()) {
			//SudokuBoard newBoard = new SudokuBoard(testBoard);
			SudokuBoard newBoard = (SudokuBoard)testBoard.clone();
			int[] coord = newBoard.findFirstNonEmpty();
			int row, col, boardNum;
			if (newBoard instanceof SingleBoard) {
				row=coord[0];
				col=coord[1];
				boardNum=1;
			} else {
				row=coord[1];
				col=coord[2];
				boardNum=coord[0];
			}	
				
			//int testValue = newBoard.boards.get(boardNum).board[row][col].pval.get(0);
			int testValue = (Integer)newBoard.getBoard(boardNum)[row][col].pval.toArray()[0];
			newBoard.add(row, col, testValue, boardNum);
			if (!solve(newBoard)) {
				//testBoard.boards.get(boardNum).board[row][col].remove(testValue);
				testBoard.getBoard(boardNum)[row][col].remove(testValue);
				return solve(testBoard);
			} else {
				return true;
			}
		} else {
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
		System.out.println("Solved: \n" + test);
	}
}