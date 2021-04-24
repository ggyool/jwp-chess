package chess.dto.piece;

import chess.domain.piece.Piece;

public class PieceDto {

    private final int x;
    private final int y;
    private final String color;
    private final String shape;

    public PieceDto(int x, int y, String color, String shape) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.shape = shape;
    }

    public static PieceDto from(Piece piece) {
        return new PieceDto(piece.getX(), piece.getY(),
            piece.getTeamValue(), String.valueOf(piece.getPieceTypeValue()));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getColor() {
        return color;
    }

    public String getShape() {
        return shape;
    }
}
