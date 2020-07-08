package sudokuGame;

import java.io.File;
import java.util.Scanner;
import sudokuGame.Cell;

// represents the standard single 9x9 sudoku board
public class SingleBoard implements Cloneable, SudokuBoard {
	
	// size of the board
	private final int size = Cell.size;
	// single board
	protected Cell[][] board = new Cell[size*size][size*size];
	
	public SingleBoard() {
		// default board name
		this("EnterBoard1");
	}
	public SingleBoard(String fileName) {
		File file = new File(fileName);
		
		try { 
			// uses values from file to make board
			Scanner scan = new Scanner(file);
			
			for (int i=0; i<board.length; i++) {
				for (int j=0; j<board[i].length; j++) {
					board[i][j] = new Cell(scan.nextInt());
				}
				if (i != board.length - 1) {
					scan.nextLine();
				}
			}
			
			scan.close();
		} catch (Exception e) {
			System.out.println("Not correct format");
		}
		
		// sets correct possible values
		for (int r=0; r<size*size; r++) {
			for (int c=0; c<size*size; c++) {
				if (board[r][c].val==0) {
					setValues(r, c);
				}
			}
		}
	}
	// sets possible values
	// used for the constructor to set the correct possible values
	public void setValues(int row, int col) {
		int box = (row)/size*size+1 + (col)/size;
		for (int i=1; i<size*size+1; i++) {
			if (inRow(i, row) || inCol(i, col) || inBox(i, box)) {
				board[row][col].remove(i);
			}
		}
	}
	
	
	
	
	
	// copies board
	public SingleBoard clone() {
		SingleBoard result = null;
		try {
			result = (SingleBoard) super.clone();
			result.board = new Cell[size*size][size*size];
			for (int r=0; r<size*size; r++) {
				for (int c=0; c<size*size; c++) {
					result.board[r][c] = board[r][c].clone();
				}
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	
	// adds num to the row and col
	public void add(int row, int col, int num, int boardNum) {
		add(row, col, num);
	}
	public void add(int row, int col, int num) {
		board[row][col].add(num);
		removeOtherValues(row, col, num);
	}
	public void removeOtherValues(int row, int col, int num) {
		// removes num from possible values in the row
		for (Cell cell : getRow(row)) {
			cell.remove(num);
		}		
		// removes num from possible values in the col
		for (Cell cell : getCol(col)) {
			cell.remove(num);
		}
		int box = (row)/size*size+1 + (col)/size;
		// removes num from possible values in the box
		for (Cell cell : getBox(box)) {
			cell.remove(num);
		}
	}	
	
	
	
	
	
	// returns array of the cells of the row/col/box
	public Cell[] getRow(int num) {
		Cell[] row = new Cell[size*size];
		for (int i=0; i<board[num].length; i++) {
			row[i] = board[num][i];
		}
		return row;
	}
	public Cell[] getCol(int num) {
		Cell[] col = new Cell[size*size];
		for (int i=0; i<board.length; i++) {
			col[i] = board[i][num];
		}
		return col;
	}
	public Cell[] getBox(int num) {
		Cell[] box = new Cell[size*size];
		for (int i=0; i<board.length; i++) {
			box[i] = board[i/size + (num-1)/size*size][i%size + (num+(size-1))%size*size];
		}
		return box;
	}	
	// determines if find is in a row/col/box
	public boolean inRow(int find, int row) {
		for (Cell cell : getRow(row)) {
			if (cell.val == find) {
				return true;
			}
		}
		return false;
	}
	public boolean inCol(int find, int col) {
		for (Cell cell : getCol(col)) {
			if (cell.val == find) {
				return true;
			}
		}
		return false;
	}
	public boolean inBox(int find, int box) {
		for (Cell cell : getBox(box)) {
			if (cell.val == find) {
				return true;
			}
		}
		return false;
	}
	// returns board
	public Cell[][] getBoard(int boardNum) {
		return board;
	}
	
	
	
	
	
	// finds the first nonempty cell where index 0=row, 1=col
	public int[] findFirstNonEmpty() {
		int[] coord = new int[2];
		for (int r=0; r<size*size; r++) {
			for (int c=0; c<size*size; c++) {
				if (board[r][c].val==0) {
					coord[0]=r;
					coord[1]=c;
					return coord;
				}
			}
		}	
		return null;
	}
	public int[] findRandomEmpty() {
		int[] coord = new int[2];
		while (true) {
			int r=(int)(Math.random()*size*size), c=(int)(Math.random()*size*size);
			int randomValue = board[r][c].val;
			if (randomValue==0) {
				coord[0]=r;
				coord[1]=c;
				return coord;
			}
		}
	}
	
	
	
	
	
	// solves all the cells with one possible value
	public void trySolve() {
		boolean change = true;
		while (change) {
			change = boardSolve();
		}
	}
	public boolean boardSolve() {
		for (int r=0; r<size*size; r++) {
			for (int c=0; c<size*size; c++) {
				if (board[r][c].val==0 && board[r][c].pval.size()==1) {
					add(r, c, (Integer)board[r][c].pval.toArray()[0]);
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	
	
	// checks is the board is solved
	public boolean isSolved() {
		for (int i=0; i<size*size; i++) {
			if (inRow(0, i) || inCol(0,i) || inBox(0,i)) {
				return false;
			}
		}
		return true;
	}	
	// checks if the board is solvable
	public boolean isSolvable() {
		for (Cell[] cellRow : board) {
			for (Cell cell : cellRow) {
				// not solvable if empty with no possible values
				if (cell.val==0 && cell.pval.size()==0) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	
	
	
	// the display for the board
	@Override
	public String toString() {
		String result = "-------------------\n";
		for (Cell[] cellRow : board) {
			result += "|";
			for (Cell cell : cellRow) {
				result += (cell.val == 0 ? " " : cell.val) + "|";
			}
			result += "\n-------------------\n";
		}
		return result += "\n" + toTest();
	}
	
	// gives information about the possible values for the board
	public String toTest() {
		String result = "Possible values: \n";
		for (int r=0; r<size*size; r++) {
			for (int c=0; c<size*size; c++) {
				if (board[r][c].val==0) {
					result += "Row: " + (r+1) + ", Col: " + (c+1) + ", Value: " + board[r][c].val + ", Possible values: " + board[r][c].pval + "\n";
				}
			}
		}
		return result;
	}	
}