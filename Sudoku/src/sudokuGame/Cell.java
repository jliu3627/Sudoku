package sudokuGame;

import java.util.HashSet;

// makes up the individual cells of board
public class Cell implements Cloneable {
	
	// size of the board
	protected static final int size = 3;
	// the actual value
	protected int val;
	// possible values
	protected HashSet<Integer> pval;
	// shared cells
	protected HashSet<int[]> shared;
	
	protected Cell(int val) {
		this.val = val;
		pval = new HashSet<Integer>();
		// initially makes the possible values everything if empty (0)
		if (this.val==0) {
			for (int i=1; i<size*size+1; i++) {
				pval.add(i);
			}
		}
		shared = new HashSet<>();
	}
	
	// sets num to the actual value and clears the possible values
	protected void add(int num) {
		val = num;
		pval.clear();
	}
	
	// removes num from possible values
	protected void remove(int num) {
		pval.remove(num);
	}
	
	// "link" two cells when boards overlap
	protected void setShared(int row, int col, int boardNum) {
		int[] temp = {row, col, boardNum};
		shared.add(temp);
	}
	
	// copies Cell
	protected Cell clone() {
		Cell result = null;
		try {
			result = (Cell) super.clone();
			result.pval = new HashSet<Integer>();
			for (Integer num : this.pval) {
				result.pval.add(num);
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}			
		return result;
	}						
}