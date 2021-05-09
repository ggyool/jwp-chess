package chess.domain.game.room;

import chess.exception.GameParticipationFailureException;
import java.util.Objects;

public class Room {

    private final long id;
    private final String name;
    private final long hostId;
    private final Long guestId;

    public Room(final long id, final String name, final long hostId, final Long guestId) {

        this.id = id;
        this.name = name;
        this.hostId = hostId;
        this.guestId = guestId;
    }

    public void validateFull() {
        if (isFull()) {
            throw new GameParticipationFailureException("이미 방이 가득 찼습니다.");
        }
    }

    private boolean isFull() {
        return Objects.nonNull(guestId);
    }

    public void validateAlreadyJoined(final long userId) {
        if (hostId == userId) {
            throw new GameParticipationFailureException("이미 참여한 방입니다.");
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getHostId() {
        return hostId;
    }

    public Long getGuestId() {
        return guestId;
    }

}
