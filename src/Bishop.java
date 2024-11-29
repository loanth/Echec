public class Bishop extends Piece {
    public Bishop(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
    }

    @Override
    public String getImageKey() {
        return isWhite ? "♗" : "♝";
    }

    @Override
    public boolean canMoveTo(int x, int y, Piece[][] board) {
        if (Math.abs(this.x - x) == Math.abs(this.y - y)) {
            // Vérifier si le chemin est dégagé
            if (isPathClear(x, y, board)) {
                return true;
            }
        }
        return false;
    }

    // Vérifier si le chemin est dégagé
    private boolean isPathClear(int x, int y, Piece[][] board) {
        int stepX = Integer.compare(x, this.x);
        int stepY = Integer.compare(y, this.y);
        int currentX = this.x + stepX;
        int currentY = this.y + stepY;

        while (currentX != x || currentY != y) {
            if (!isEmpty(currentX, currentY, board)) {
                return false;
            }
            currentX += stepX;
            currentY += stepY;
        }

        return true;
    }
}
