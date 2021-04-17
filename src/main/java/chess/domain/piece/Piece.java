package chess.domain.piece;

import chess.domain.board.Board;
import chess.domain.piece.condition.BishopMoveCondition;
import chess.domain.piece.condition.CatchingPieceBlackPawnMoveCondition;
import chess.domain.piece.condition.CatchingPieceWhitePawnMoveCondition;
import chess.domain.piece.condition.FirstTurnBlackPawnMoveCondition;
import chess.domain.piece.condition.FirstTurnWhitePawnMoveCondition;
import chess.domain.piece.condition.KingMoveCondition;
import chess.domain.piece.condition.KnightMoveCondition;
import chess.domain.piece.condition.MoveCondition;
import chess.domain.piece.condition.NormalBlackPawnMoveCondition;
import chess.domain.piece.condition.NormalWhitePawnMoveCondition;
import chess.domain.piece.condition.QueenMoveCondition;
import chess.domain.piece.condition.RookMoveCondition;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Piece {

    private final Color color;
    private final Shape shape;
    private final List<MoveCondition> moveConditions;
    private Position position;

    public Piece(Color color, Shape shape, Position position, List<MoveCondition> moveConditions) {
        this.color = color;
        this.shape = shape;
        this.position = position;
        this.moveConditions = moveConditions;
    }

    public static Piece createPawn(Color color, int row, int col) {
        if (Color.BLACK.equals(color)) {
            return new Piece(Color.BLACK, Shape.P, new Position(row, col),
                Arrays.asList(new FirstTurnBlackPawnMoveCondition(),
                    new NormalBlackPawnMoveCondition(), new CatchingPieceBlackPawnMoveCondition()));
        }

        return new Piece(Color.WHITE, Shape.P, new Position(row, col),
            Arrays.asList(new FirstTurnWhitePawnMoveCondition(),
                new NormalWhitePawnMoveCondition(), new CatchingPieceWhitePawnMoveCondition()));
    }

    public static Piece createKing(Color color, int row, int col) {
        return new Piece(color, Shape.K, new Position(row, col),
            Collections.singletonList(new KingMoveCondition()));
    }

    public static Piece createQueen(Color color, int row, int col) {
        return new Piece(color, Shape.Q, new Position(row, col),
            Collections.singletonList(new QueenMoveCondition()));
    }

    public static Piece createRook(Color color, int row, int col) {
        return new Piece(color, Shape.R, new Position(row, col),
            Collections.singletonList(new RookMoveCondition()));
    }

    public static Piece createKnight(Color color, int row, int col) {
        return new Piece(color, Shape.N, new Position(row, col),
            Collections.singletonList(new KnightMoveCondition()));
    }

    public static Piece createBishop(Color color, int row, int col) {
        return new Piece(color, Shape.B, new Position(row, col),
            Collections.singletonList(new BishopMoveCondition()));
    }

    public boolean isSameColor(Color color) {
        return this.color == color;
    }

    public boolean isKing() {
        return this.shape == Shape.K;
    }

    public String getNotation() {
        return shape.getNotation(color);
    }

    public int getColumn() {
        return position.getColumn();
    }

    public int getRow() {
        return position.getRow();
    }

    public boolean isSamePosition(final Position position) {
//        System.out.println("=====================================");
//        System.out.println("=====================================");
//        System.out.println("=====================================");
//        System.out.println(position.toString());
//        System.out.println(this.position.toString());
//        System.out.println(this.position.equals(position));
//        System.out.println("=====================================");
//        System.out.println("=====================================");
//        System.out.println("=====================================");
        return this.position.equals(position);
    }

    public void move(final Position target, final Board board) {
        if (!isMovable(target, board)) {
            throw new IllegalArgumentException("해당 위치로는 이동할 수 없습니다.");
        }
        position = target;
    }

    public boolean isMovable(final Position target, final Board board) {
        for (final MoveCondition moveCondition : moveConditions) {
            if (moveCondition.isSatisfyBy(board, this, target)) {
                return true;
            }
        }
        return false;
    }

    public Color getColor() {
        return color;
    }

    public Shape getShape() {
        return shape;
    }

    public double getScore() {
        return shape.getScore();
    }

    public Position getPosition() {
        return new Position(position.getRow(), position.getColumn());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Piece)) {
            return false;
        }

        final Piece piece = (Piece) o;

        if (color != piece.color) {
            return false;
        }
        if (shape != piece.shape) {
            return false;
        }
        return Objects.equals(position, piece.position);
    }

    public boolean isPawn() {
        return shape == Shape.P;
    }

    @Override
    public int hashCode() {
        int result = color != null ? color.hashCode() : 0;
        result = 31 * result + (shape != null ? shape.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.join(":",
            shape.getNotation(color),
            String.valueOf(position.getRow()),
            String.valueOf(position.getColumn()));
    }

    public String getColorValue() {
        return this.color.getValue();
    }

    public String getShapeValue() {
        return this.shape.getNotation();
    }
}
