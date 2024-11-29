import javax.swing.ImageIcon;
import java.util.HashMap;
import java.util.Map;

public class PieceImageLoader {
    private static final Map<String, ImageIcon> imageMap = new HashMap<>();

    static {
        // Charger les images pour chaque pièce
        imageMap.put("whiteKing", new ImageIcon("resources/white_king.png"));
        imageMap.put("whiteQueen", new ImageIcon("resources/white_queen.png"));
        imageMap.put("whiteRook", new ImageIcon("resources/white_rook.png"));
        imageMap.put("whiteBishop", new ImageIcon("resources/white_bishop.png"));
        imageMap.put("whiteKnight", new ImageIcon("resources/white_knight.png"));
        imageMap.put("whitePawn", new ImageIcon("resources/white_pawn.png"));

        imageMap.put("blackKing", new ImageIcon("resources/black_king.png"));
        imageMap.put("blackQueen", new ImageIcon("resources/black_queen.png"));
        imageMap.put("blackRook", new ImageIcon("resources/black_rook.png"));
        imageMap.put("blackBishop", new ImageIcon("resources/black_bishop.png"));
        imageMap.put("blackKnight", new ImageIcon("resources/black_knight.png"));
        imageMap.put("blackPawn", new ImageIcon("resources/black_pawn.png"));
    }

    public static ImageIcon getImage(String key) {
        return imageMap.get(key);
    }
}
