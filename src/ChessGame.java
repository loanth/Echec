import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ChessGame extends JFrame {
    private JPanel boardPanel;
    private JPanel sidePanel;
    private JLabel turnLabel;
    private Piece[][] board = new Piece[8][8];
    private Piece selectedPiece = null;
    private int selectedX = -1;
    private int selectedY = -1;
    private boolean isWhiteTurn = true;
    private List<Point> validMoves = new ArrayList<>();
    private boolean gameOver = false;


    public ChessGame() {
        initializeBoard();

        // Configuration du panneau principal
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Ajouter un fond dégradé
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(224, 242, 241),
                        getWidth(), getHeight(), new Color(95, 56, 41));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        boardPanel.setLayout(new GridLayout(8, 8));

        // Configuration du panneau latéral
        sidePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Ajout d'un fond stylisé
                g.drawImage(new ImageIcon("assets/side_panel_background.png").getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        sidePanel.setLayout(new BorderLayout());

        // Label de tour
        turnLabel = new JLabel("Tour : Blancs", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Serif", Font.BOLD, 20));
        turnLabel.setForeground(new Color(95, 56, 41));
        sidePanel.add(turnLabel, BorderLayout.CENTER);

        // Bouton Échec et Mat
        JButton checkmateButton = new JButton("Échec et Mat");
        checkmateButton.setFont(new Font("Serif", Font.BOLD, 16));
        checkmateButton.setBackground(new Color(224, 242, 241));
        checkmateButton.setForeground(new Color(95, 56, 41));
        checkmateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    gameOver = true;
                    JOptionPane.showMessageDialog(null, "La partie est terminée en Échec et Mat.");
                    // Arrête la partie sans fermer l'application
                    // Tu peux éventuellement désactiver les boutons de mouvement ou l'interface pour empêcher de jouer

                resetGame();
            }
        });
        sidePanel.add(checkmateButton, BorderLayout.SOUTH);

        updateBoard();

        // Configuration de la fenêtre
        setLayout(new BorderLayout());
        add(sidePanel, BorderLayout.WEST);
        add(boardPanel, BorderLayout.CENTER);
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public void disableGame() {
        // Désactive les éléments du jeu, comme les boutons de mouvement, ou empêche les clics.
        // Exemple pour désactiver le plateau de jeu (en fonction de ton code) :
        for (Component comp : boardPanel.getComponents()) {
            comp.setEnabled(false);
        }
    }
    public void resetGame() {
        // Réinitialiser les positions des pièces (exemple)
         // Méthode pour réinitialiser les pièces sur le plateau
        gameOver = false;   // Réactive le jeu
        boardPanel.removeAll();
        // Réactive l'interface pour permettre de rejouer
        for (Component comp : boardPanel.getComponents()) {
            comp.setEnabled(true);
        }

        // Réinitialisation de l'étiquette de tour
        turnLabel.setText("Tour : Blancs");

        // Met à jour le plateau pour afficher les nouvelles positions
        dispose();
        ChessGame cs = new ChessGame();
    }

    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;  // Supprimer toutes les pièces
            }
        }
        board[0][0] = new Rook(true, 0, 0);
        board[0][1] = new Knight(true, 0, 1);
        board[0][2] = new Bishop(true, 0, 2);
        board[0][3] = new Queen(true, 0, 3);
        board[0][4] = new King(true, 0, 4);
        board[0][5] = new Bishop(true, 0, 5);
        board[0][6] = new Knight(true, 0, 6);
        board[0][7] = new Rook(true, 0, 7);
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(true, 1, i);
        }
        board[7][0] = new Rook(false, 7, 0);
        board[7][1] = new Knight(false, 7, 1);
        board[7][2] = new Bishop(false, 7, 2);
        board[7][3] = new Queen(false, 7, 3);
        board[7][4] = new King(false, 7, 4);
        board[7][5] = new Bishop(false, 7, 5);
        board[7][6] = new Knight(false, 7, 6);
        board[7][7] = new Rook(false, 7, 7);
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(false, 6, i);
        }
    }

    private void handleSquareClick(int x, int y) {
        if (selectedPiece == null) {
            Piece piece = board[x][y];
            if (piece != null && piece.isWhite() == isWhiteTurn) {
                selectedPiece = piece;
                selectedX = x;
                selectedY = y;
                validMoves = getValidMoves(piece);
            }
        } else {
            if (isMoveValid(x, y)) {
                Piece targetPiece = board[x][y];
                board[x][y] = selectedPiece;
                board[selectedX][selectedY] = null;
                selectedPiece.x = x;
                selectedPiece.y = y;

                // Promotion du pion
                if ((selectedPiece instanceof Pawn) && (x == 0 || x == 7)) {
                    board[x][y] = new Queen(selectedPiece.isWhite(), x, y); // Promotion à une Reine
                    JOptionPane.showMessageDialog(this, "Promotion du pion en Reine!");
                }

                if (isKingInCheck(isWhiteTurn)) {
                    // Annuler le mouvement si le roi est en échec
                    board[selectedX][selectedY] = selectedPiece;
                    board[x][y] = targetPiece;
                    selectedPiece.x = selectedX;
                    selectedPiece.y = selectedY;
                    JOptionPane.showMessageDialog(this, "Le roi est en échec ! Le coup est invalide.");
                    return;
                }

                // Vérification de l'échec et mat
                if (isCheckmate()) {
                    gameOver = true;
                    String winner = isWhiteTurn ? "Les Blancs" : "Les Noirs";
                    JOptionPane.showMessageDialog(this, "Échec et mat! " + winner + " gagnent.");
                    return;
                }

                // Vérification de l'échec
                else if (isKingInCheck(isWhiteTurn)) {
                    JOptionPane.showMessageDialog(this, "Le roi est en échec !");
                }

                // Vérification du pat
                else if (isStalemate()) {
                    gameOver = true;
                    JOptionPane.showMessageDialog(this, "Pat! Match nul.");
                }

                // Passer au tour suivant
                isWhiteTurn = !isWhiteTurn;
                updateTurnLabel();

                // Réinitialiser la sélection
                selectedPiece = null;
                selectedX = -1;
                selectedY = -1;
                validMoves.clear();
                updateBoard();
            } else {
                selectedPiece = null;
                selectedX = -1;
                selectedY = -1;
                validMoves.clear();
            }
        }
        updateBoard();
    }




    private boolean isMoveValid(int x, int y) {
        if (selectedPiece == null) return false;
        Piece targetPiece = board[x][y];
        if (targetPiece != null && targetPiece.isWhite() == selectedPiece.isWhite()) {
            return false; // Ne pas manger ses propres pièces
        }
        return selectedPiece.canMoveTo(x, y, board);
    }

    private List<Point> getValidMoves(Piece piece) {
        List<Point> moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (piece.canMoveTo(i, j, board)) {
                    Piece targetPiece = board[i][j];
                    // Filtrer les cases où la pièce alliée est présente
                    if (targetPiece == null || targetPiece.isWhite() != piece.isWhite()) {
                        moves.add(new Point(i, j));
                    }
                }
            }
        }
        return moves;
    }



    String winner = isWhiteTurn ? "Les Blancs" : "Les Noirs";




    private void updateBoard() {
        boardPanel.removeAll();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                final int x = i;
                final int y = j;

                JPanel square = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);

                        // Appliquer une texture sur chaque case
                        ImageIcon texture = new ImageIcon((x + y) % 2 == 0
                                ? "assets/light_square.png"
                                : "assets/dark_square.png");
                        g.drawImage(texture.getImage(), 0, 0, getWidth(), getHeight(), null);

                        // Indicateur de déplacement valide
                        if (validMoves.contains(new Point(x, y))) {
                            g.setColor(new Color(255, 0, 0, 100));
                            g.fillOval(getWidth() / 2 - 10, getHeight() / 2 - 10, 20, 20);
                        }
                    }
                };

                square.setLayout(new BorderLayout());
                Piece piece = board[i][j];
                JLabel label = new JLabel();
                if (piece != null) {
                    label.setText(piece.getImageKey()); // Emoji ou image de la pièce
                }
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Serif", Font.BOLD, 40));
                square.add(label, BorderLayout.CENTER);

                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(x, y);
                    }
                });

                boardPanel.add(square);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }




    private boolean isCheckmate() {
        King king = findKing(isWhiteTurn);
        if (king == null) {
            // Si le roi est absent ou capturé, c'est la fin de la partie
            JOptionPane.showMessageDialog(null, "Échec et Mat ! Le joueur " + (isWhiteTurn ? "Blanc" : "Noir") + " a gagné.", "Fin de Partie", JOptionPane.INFORMATION_MESSAGE);
            gameOver = true; // Partie terminée, échec et mat
            System.exit(0); // Arrêter l'application
            return true;
        }

        // Si le roi est en échec, on vérifie s'il peut se déplacer
        if (isKingInCheck(isWhiteTurn)) {
            // Vérification des mouvements possibles du roi
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (king.canMoveTo(i, j, board)) {
                        Piece targetPiece = board[i][j];
                        board[i][j] = king;
                        board[king.x][king.y] = null;
                        if (!isKingInCheck(isWhiteTurn)) {
                            board[king.x][king.y] = king;
                            board[i][j] = targetPiece;
                            return false; // Le roi peut se déplacer sans être en échec
                        }
                        board[king.x][king.y] = king;
                        board[i][j] = targetPiece;
                    }
                }
            }

            // Si le roi ne peut pas se déplacer sans être en échec, c'est échec et mat
            JOptionPane.showMessageDialog(null, "Échec et Mat ! Le joueur " + (isWhiteTurn ? "Blanc" : "Noir") + " a gagné.", "Fin de Partie", JOptionPane.INFORMATION_MESSAGE);
            gameOver = true; // Partie terminée, échec et mat
            System.exit(0); // Arrêter l'application
            return true; // Partie terminée, échec et mat
        }
        return false; // Le roi n'est pas en échec et il peut se déplacer
    }



    private boolean isStalemate() {
        // Vérifier si le joueur actuel est en échec
        if (isKingInCheck(isWhiteTurn)) {
            return false; // Il y a un échec, donc ce n'est pas un pat
        }

        // Vérifier si le joueur n'a pas de mouvement légal
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece != null && piece.isWhite() == isWhiteTurn) {
                    List<Point> validMoves = getValidMoves(piece);
                    if (!validMoves.isEmpty()) {
                        return false; // Il y a encore des mouvements possibles
                    }
                }
            }
        }

        // Si aucun mouvement légal n'est disponible et qu'il n'y a pas d'échec, c'est un pat
        return true;
    }

    private boolean isKingInCheck(boolean isWhite) {
        King king = findKing(isWhite);
        if (king == null) {
            return false; // Pas de roi, pas d'échec
        }

        // Vérification si le roi est en échec
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece != null && piece.isWhite() != isWhite && piece.canMoveTo(king.x, king.y, board)) {
                    // Le roi est en échec, afficher un message d'alerte
                    JOptionPane.showMessageDialog(null, "Le roi est en Échec !", "Alerte", JOptionPane.WARNING_MESSAGE);
                    return true; // Le roi est en échec
                }
            }
        }
        return false; // Le roi n'est pas en échec
    }


    // Retourne une liste des mouvements valides pour un roi à une position donnée
    private List<int[]> getKingValidMoves(int x, int y) {
        List<int[]> validMoves = new ArrayList<>();
        // Vérifie les cases autour du roi (horizontale, verticale et diagonale)
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Ignore la case actuelle (le roi ne peut pas se déplacer là)
                if (i == 0 && j == 0) continue;

                int newX = x + i;
                int newY = y + j;

                // Vérifie si la case est à l'intérieur des limites de l'échiquier
                if (isInsideBoard(newX, newY)) {
                    Piece targetPiece = board[newX][newY];

                    // Le roi peut se déplacer sur une case vide ou capturer une pièce adverse
                    if (targetPiece == null || targetPiece.isWhite() != board[x][y].isWhite()) {
                        // Vérifie si après ce mouvement, le roi est toujours en sécurité
                        if (!isKingInCheckAfterMove(x, y, newX, newY)) {
                            validMoves.add(new int[]{newX, newY});
                        }
                    }
                }
            }
        }
        return validMoves;
    }
    // Vérifie si le roi est en échec après un mouvement
    private boolean isKingInCheckAfterMove(int oldX, int oldY, int newX, int newY) {
        Piece movedPiece = board[oldX][oldY];
        Piece targetPiece = board[newX][newY];

        // Effectue le déplacement du roi temporairement
        board[newX][newY] = movedPiece;
        board[oldX][oldY] = null;
        movedPiece.x = newX;
        movedPiece.y = newY;

        boolean inCheck = isKingInCheck(movedPiece.isWhite());

        // Annule le déplacement
        board[oldX][oldY] = movedPiece;
        board[newX][newY] = targetPiece;
        movedPiece.x = oldX;
        movedPiece.y = oldY;

        return inCheck;
    }

    // Vérifie si la position est à l'intérieur de l'échiquier
    private boolean isInsideBoard(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    private void updateTurnLabel() {
        if (isWhiteTurn) {
            turnLabel.setText("Tour : Blancs");
        } else {
            turnLabel.setText("Tour : Noirs");
        }
    }
    private King findKing(boolean isWhite) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece instanceof King && piece.isWhite() == isWhite) {
                    return (King) piece; // Si le roi du joueur est trouvé
                }
            }
        }
        System.out.println("Roi " + (isWhite ? "blanc" : "noir") + " non trouvé!");
        return null; // Retourne null si aucun roi n'est trouvé
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessGame::new);
    }
}