package shared;

public class Board {
	public final int sideSize = 10;
	public final int boardSize = sideSize * sideSize;
	public final int EMPTY = 0;
	public final int RED_PAWN = 1;
	public final int RED_KING = 2;
	public final int BLACK_PAWN = 3;
	public final int BLACK_KING = 4;

	public int[] board = new int[boardSize];

	public Board() {
		int startLine = 0;
		FillRange(0, 40, BLACK_PAWN); // assume 10 * 10
		FillRange(60, 100, RED_PAWN);
	}

	private void FillRange(int start, int end, int piece) {
		for (int i = start; i < end; i++) {
			int row = i / sideSize;
			int col = i % sideSize;
			// (row + col) % 2 results in 1 for dark squares and 0 for light squares
			board[i] = ((row + col) % 2) * piece;
		}
	}

	public void PrintBoard() {
		for (int j = 0; j < sideSize; j++) {
			for (int i = 0; i < sideSize; i++) {
				System.out.printf("%d ", board[j * sideSize + i]);
			}
			System.out.printf("\n");
		}
	}

	public int getX(int board_index) {
		return board_index % Board.this.sideSize;
	}

	public int getY(int board_index) {
		return board_index / Board.this.sideSize;
	}

	public int getIndex(int x, int y) {
		return x + y * Board.this.sideSize;
	}
}
