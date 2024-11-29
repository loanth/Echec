public class Board {
    private Piece[][] board = new Piece[8][8];  // Plateau 8x8

    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    public void setPiece(int x, int y, Piece piece) {
        board[x][y] = piece;
    }
}
