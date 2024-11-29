public abstract class Piece {
    protected boolean isWhite;
    protected int x, y;

    public Piece(boolean isWhite, int x, int y) {
        this.isWhite = isWhite;
        this.x = x;
        this.y = y;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public abstract String getImageKey();  // Pour récupérer l'emoji de la pièce

    // Vérifier si la pièce peut se déplacer à la position (x, y) donnée sur le plateau
    public abstract boolean canMoveTo(int x, int y, Piece[][] board);

    // Méthode utilitaire pour vérifier si une case est vide
    protected boolean isEmpty(int x, int y, Piece[][] board) {
        return board[x][y] == null;
    }

    // Méthode utilitaire pour vérifier si une pièce ennemie occupe la case
    protected boolean isEnemy(int x, int y, Piece[][] board) {
        return board[x][y] != null && board[x][y].isWhite != this.isWhite;
    }
}
