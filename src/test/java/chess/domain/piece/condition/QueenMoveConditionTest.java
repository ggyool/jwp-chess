package chess.domain.piece.condition;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.Board;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Position;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QueenMoveConditionTest {

    @DisplayName("퀸이 조건대로 움직이는지 확인한다.")
    @Test
    void isSatisfyBy() {
        QueenMoveCondition condition = new QueenMoveCondition();
        Board board = new Board(Arrays.asList(
                Piece.createQueen(Color.WHITE, 4, 4),
                Piece.createPawn(Color.WHITE, 4, 5)
        ));
        boolean rightActual = condition.isSatisfyBy(board, Piece.createQueen(Color.WHITE, 4, 4),
                new Position(7, 4));

        boolean rightXActual = condition.isSatisfyBy(board, Piece.createRook(Color.WHITE, 4, 4),
                new Position(5, 5));

        boolean falseActual = condition.isSatisfyBy(board, Piece.createQueen(Color.WHITE, 4, 4),
                new Position(7, 5));

        assertThat(rightActual).isTrue();
        assertThat(rightXActual).isTrue();
        assertThat(falseActual).isFalse();
    }

    @DisplayName("퀸의 이동 경로에 장애물이 있으면 안된다.")
    @Test
    void isSatisfyBy_false() {
        QueenMoveCondition condition = new QueenMoveCondition();
        Board board = new Board(Arrays.asList(
                Piece.createQueen(Color.WHITE, 4, 4),
                Piece.createPawn(Color.WHITE, 5, 4),
                Piece.createPawn(Color.WHITE, 5, 5)
        ));

        boolean actualCross = condition.isSatisfyBy(board, Piece.createQueen(Color.WHITE, 4, 4),
                new Position(7, 4));
        boolean actualX = condition.isSatisfyBy(board, Piece.createQueen(Color.WHITE, 4, 4), new Position(6, 6));

        assertThat(actualCross).isFalse();
        assertThat(actualX).isFalse();
    }

}