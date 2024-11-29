public class Rook extends Piece {
    public Rook(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
    }

    @Override
    public String getImageKey() {
        return isWhite ? "♖" : "♜";
    }

    @Override
    public boolean canMoveTo(int x, int y, Piece[][] board) {
        // Mouvement en ligne droite (horizontale ou verticale)
        if (this.x == x || this.y == y) {
            // Vérifier s'il n'y a pas d'obstacles entre la position actuelle et la destination
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
