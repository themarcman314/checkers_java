package shared;

public class board {
	private final int side_size = 10;
	private final int board_size = side_size * side_size;
	private final int EMPTY = 0;
	private final int RED_PAWN = 1;
	private final int RED_KING = 2;
	private final int BLACK_PAWN = 3;
	private final int BLACK_KING = 4;

	private int[] board = new int[board_size];

	public board() {
		int start_line = 0;
		fill_range(0, 40, BLACK_PAWN); // assume 10 * 10
		fill_range(60, 100, RED_PAWN);
	}

	private void fill_range(int start, int end, int piece) {
		for (int i = start; i < end; i++) {
			int row = i / side_size;
			int col = i % side_size;
			// (row + col) % 2 results in 1 for dark squares and 0 for light squares
			board[i] = ((row + col) % 2) * piece;
		}
	}

	public void print_board() {
		for (int j = 0; j < side_size; j++) {
			for (int i = 0; i < side_size; i++) {
				System.out.printf("%d ", board[j * side_size + i]);
			}
			System.out.printf("\n");
		}
	}
}
