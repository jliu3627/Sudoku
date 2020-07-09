package sudokuGame;

public interface SudokuBoard {
	public void trySolve();
	public boolean isSolved();
	public boolean isSolvable();
	public Object clone();
	public int[] findFirstNonEmpty();
	public Cell[][] getBoard(int boardNum);
	public void add(int row, int col, int num, int boardNum);	
}
