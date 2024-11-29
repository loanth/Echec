public class Pawn extends Piece {
    public Pawn(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
    }

    @Override
    public String getImageKey() {
        return isWhite ? "♙" : "♟";
    }

    @Override
    public boolean canMoveTo(int x, int y, Piece[][] board) {
        int direction = isWhite ? 1 : -1;  // Les pions blancs avancent vers le bas, les noirs vers le haut
        int startRow = isWhite ? 1 : 6;    // Ligne de départ pour vérifier le double mouvement

        // Mouvement simple vers l'avant
        if (this.x + direction == x && this.y == y && isEmpty(x, y, board)) {
            return true;
        }

        // Mouvement de deux cases depuis la ligne de départ
        if (this.x == startRow && this.x + 2 * direction == x && this.y == y && isEmpty(x, y, board) && isEmpty(this.x + direction, y, board)) {
            return true;
        }

        // Capture diagonale
        if (this.x + direction == x && Math.abs(this.y - y) == 1 && isEnemy(x, y, board)) {
            return true;
        }


        return false;
    }
}
