public class King extends Piece {
    public King(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
    }

    @Override
    public String getImageKey() {
        return isWhite ? "♔" : "♚";
    }

    @Override
    public boolean canMoveTo(int x, int y, Piece[][] board) {
        int dx = Math.abs(this.x - x);
        int dy = Math.abs(this.y - y);
        return (dx <= 1 && dy <= 1);
    }
}
