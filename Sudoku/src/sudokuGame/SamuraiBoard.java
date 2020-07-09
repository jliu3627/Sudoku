package sudokuGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// represents a samurai sudoku board
public class SamuraiBoard implements Cloneable, SudokuBoard {
	// length of overlapping boxes
	private static int overlap = 1;
	// size of the board
	private final int size = Cell.size;
	// the boards
	protected HashMap<Integer, SingleBoard> boards;
	
	public SamuraiBoard() {
		boards = new HashMap<>();
		for (int i=1; i<=5; i++) {
			boards.put(i, new SingleBoard("EnterBoard"+i));
		}
				
		// sets the shared cells for the boards		
		int newRow=0, newCol=0;
		// start at size*(size-overlap)
		for (int r=size*(size-overlap); r<size*size; r++) {			
			for (int c=size*(size-overlap); c<size*size; c++) {
				// cells shared for top left and center board
				boards.get(1).board[r][c].setShared(newRow, newCol, 3);
				boards.get(3).board[newRow][newCol].setShared(r, c, 1);
				if (boards.get(1).board[r][c].val!=boards.get(3).board[newRow][newCol].val) {
					throw new IllegalArgumentException("Cells sharing do not match");
				}
				// cells shared for the center and bottom right board
				boards.get(3).board[r][c].setShared(newRow, newCol, 5);
				boards.get(5).board[newRow][newCol].setShared(r, c, 3);
				if (boards.get(3).board[r][c].val!=boards.get(5).board[newRow][newCol].val) {
					throw new IllegalArgumentException("Cells sharing do not match");
				}
				newCol++;
			}
			newRow++;
			newCol = 0;
		}
		
		newRow=0;
		newCol=size*(size-overlap);
		for (int r=size*(size-overlap); r<size*size; r++) {
			for (int c=0/*size*overlap*/; c<size*overlap/*size*size*/; c++) {
				// cells shared for the top right and center board
				boards.get(2).board[r][c].setShared(newRow, newCol, 3);
				boards.get(3).board[newRow][newCol].setShared(r, c, 2);
				if (boards.get(2).board[r][c].val!=boards.get(3).board[newRow][newCol].val) {
					throw new IllegalArgumentException("Cells sharing do not match");
				}
				// cells shared for the center and bottom left board
				boards.get(3).board[r][c].setShared(newRow, newCol, 4);
				boards.get(4).board[newRow][newCol].setShared(r, c, 3);
				if (boards.get(3).board[r][c].val!=boards.get(4).board[newRow][newCol].val) {
					throw new IllegalArgumentException("Cells sharing do not match");
				}
				newCol++;
			}
			newRow++;
			newCol=size*(size-overlap);
		}
	}
	// set correct possible values
	public void setValues(int row, int col, int boardNum) {
		boards.get(boardNum).setValues(row, col);
	}
	
	
	
	
	
	// clones board
	public SamuraiBoard clone() {
		SamuraiBoard result = null;
		try {
			result = (SamuraiBoard) super.clone();
			boards = new HashMap<>();
			for (Integer boardNum : result.boards.keySet()) {
				boards.put(boardNum, result.boards.get(boardNum).clone());
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return result;
	}

	
	
	
	
	// adds num to the row and col for specified board
	public void add(int row, int col, int num, int boardNum) {
		boards.get(boardNum).board[row][col].add(num);
		removeOtherValues(row, col, num, boardNum);
		
		// the shared part
		HashSet<int[]> shared = boards.get(boardNum).board[row][col].shared;
		if (!shared.isEmpty()) {
			for (int[] sharedCell : shared) {
				int r=sharedCell[0], c=sharedCell[1], sharedNum=sharedCell[2];
				boards.get(sharedNum).board[r][c].add(num);
				removeOtherValues(r, c, num, sharedNum);
			}
		}
	}
	public void removeOtherValues(int row, int col, int num, int boardNum) {
		boards.get(boardNum).removeOtherValues(row, col, num);
	}

	
	
	
	
	
	public Cell[] getRow(int num, int boardNum) {
		return boards.get(boardNum).getRow(num);
	}
	public Cell[] getCol(int num, int boardNum) {
		return boards.get(boardNum).getCol(num);
	}
	public Cell[] getBox(int num, int boardNum) {
		return boards.get(boardNum).getBox(num);
	}
	public boolean inRow(int find, int row, int boardNum) {
		for (Cell cell : getRow(row, boardNum)) {
			if (cell.val == find) {
				return true;
			}
		}
		return false;
	}
	// determines if find is in a row/col/box for specified board
	public boolean inCol(int find, int col, int boardNum) {
		for (Cell cell : getCol(col, boardNum)) {
			if (cell.val == find) {
				return true;
			}
		}
		return false;
	}
	public boolean inBox(int find, int box, int boardNum) {
		for (Cell cell : getBox(box, boardNum)) {
			if (cell.val == find) {
				return true;
			}
		}
		return false;
	}
	// returns board
	public Cell[][] getBoard(int boardNum) {
		return boards.get(boardNum).board;
	}
	
	
	
	
	
	// finds the first nonempty cell where index 0=row, 1=col, 2=boardNum
	public int[] findFirstNonEmpty() {
		for (Integer boardNum : boards.keySet()) {
			int[] coord = new int[3];
			for (int r=0; r<size*size; r++) {
				for (int c=0; c<size*size; c++) {
					if (boards.get(boardNum).board[r][c].val==0) {
						coord[0]=r;
						coord[1]=c;
						coord[2]=boardNum;
						return coord;
					}
				}
			}	
		}
		return null;
	}
	
	
	
	
	
	// solves all the cells with one possible value
	public void trySolve() {
		boolean change = true;
		while (change) {
			change = boardSolve();
		}
	}
	public boolean boardSolve() {
		for (Integer boardNum : boards.keySet()) {
			for (int r=0; r<size*size; r++) {
				for (int c=0; c<size*size; c++) {
					if (boards.get(boardNum).board[r][c].val==0 && boards.get(boardNum).board[r][c].pval.size()==1) {
						add(r, c, (Integer)boards.get(boardNum).board[r][c].pval.toArray()[0], boardNum);
						boards.get(boardNum).board[r][c].pval.clear();
						return true;
					}
				}
			}
		}
		return false;
	}

	
	
	
	
	// checks is the board is solved
	public boolean isSolved() {
		for (SingleBoard Board : boards.values()) {
			if (!Board.isSolved()) {
				return false;
			}
		}
		return true;
	}
	// checks if the board is solvable
	public boolean isSolvable() {
		for (SingleBoard Board : boards.values()) {
			for (Cell[] cellRow : Board.board) {
				for (Cell cell : cellRow) {
					// not solvable if empty with no possible values
					if (cell.val==0 && cell.pval.size()==0) {
						return false;
					}
				}
			}
		}		
		return true;
	}
	
	public String toString() {
		String result = "";
		for (Integer boardNum : boards.keySet()) {
			result += boardNum + "\n" + boards.get(boardNum).toString()+ "\n";
		}
		return result;
	}
}