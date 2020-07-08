package sudokuGame;

import java.util.HashMap;
import java.util.HashSet;

public class DoubleBoard implements Cloneable, SudokuBoard {
	// instance variables
	private static int overlap = 1;
	private final int size = Cell.size;
	
	// the boards
	protected HashMap<Integer, SingleBoard> boards;
	
	public DoubleBoard() {
		boards = new HashMap<>();
		boards.put(1, new SingleBoard("EnterBoard1"));
		boards.put(2, new SingleBoard("EnterBoard2"));
				
		// sets the shared cells for the boards
		int newRow=0, newCol=0;
		// start at size*(size-overlap)
		for (int r=size*(size-overlap); r<size*size; r++) {			
			for (int c=size*(size-overlap); c<size*size; c++) {
				boards.get(1).board[r][c].setShared(newRow, newCol, 2);
				boards.get(2).board[newRow][newCol].setShared(r, c, 1);
				if (boards.get(1).board[r][c].val!=boards.get(2).board[newRow][newCol].val) {
					throw new IllegalArgumentException("Boards sharing do not match");
				}
				newCol++;
			}
			newRow++;
			newCol = 0;
		}
	}
	public void setValues(int row, int col, int boardNum) {
		boards.get(boardNum).setValues(row, col);
	}
	
	
	
	
	
	public DoubleBoard clone() {
		DoubleBoard result = null;
		try {
			result = (DoubleBoard) super.clone();
			boards = new HashMap<>();
			for (Integer boardNum : result.boards.keySet()) {
				boards.put(boardNum, result.boards.get(boardNum).clone());
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return result;
	}

	
	
	
	
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
				//System.out.println("Adding shared " + num + " to boardNum " + boardNum + " in row " + row + " and col " + col);
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
	
	
	
	
	
	// finds the first nonempty cell where index 0=boardNumber, 1=row, 2=col
	public int[] findFirstNonEmpty() {
		for (Integer boardNum : boards.keySet()) {
			int[] coord = new int[3];
			for (int r=0; r<size*size; r++) {
				for (int c=0; c<size*size; c++) {
					if (boards.get(boardNum).board[r][c].val==0) {
						coord[0]=boardNum;
						coord[1]=r;
						coord[2]=c;
						return coord;
					}
				}
			}	
		}
		return null;
	}
	
	
	
	
	
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

	
	
	
	
	public boolean isSolved() {
		for (SingleBoard Board : boards.values()) {
			if (!Board.isSolved()) {
				return false;
			}
		}
		return true;
	}
	public boolean isSolvable() {
		for (SingleBoard Board : boards.values()) {
			for (Cell[] cellRow : Board.board) {
				for (Cell cell : cellRow) {
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
		for (int boardNum : boards.keySet()) {
			result += boardNum + "\n" + boards.get(boardNum).toString()+ "\n";
		}
		return result;
	}
}