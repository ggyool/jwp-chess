package chess.domain.exception;

public class NoSuchPermittedChessPieceException extends RuntimeException {

    private static final String MESSAGE = "허용된 체스 말 을 찾을 수 없습니다.";

    public NoSuchPermittedChessPieceException() {
        super(MESSAGE);
    }
}
