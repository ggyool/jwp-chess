package chess.domain.piece;

import chess.domain.location.Location;
import chess.domain.team.Team;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Piece implements Movable {

    private static final int WHITE_INITIAL_Y = 1;
    private static final int BLACK_INITIAL_Y = 8;

    protected final Team team;
    protected Location location;

    protected Piece(final Location location, final Team team) {
        this.location = location;
        this.team = team;
    }

    protected static int getInitialY(final Team team) {
        if (team.isBlack()) {
            return BLACK_INITIAL_Y;
        }
        return WHITE_INITIAL_Y;
    }

    public final void move(final Location target) {
        if (isMovable(target)) {
            location = target;
        }
    }

    public final boolean isHere(final Location location) {
        return this.location.equals(location);
    }

    public List<Location> findPath(final Location target) {
        List<Location> path = new ArrayList<>();
        int subX = location.subtractX(target);
        int subY = location.subtractY(target);
        int dx = subX == 0 ? 0 : subX / Math.abs(subX);
        int dy = subY == 0 ? 0 : subY / Math.abs(subY);

        Location next = location.moveByStep(dx, dy);
        while (!next.equals(target)) {
            path.add(next);
            next = next.moveByStep(dx, dy);
        }
        return path;
    }

    public boolean isSameTeam(final Piece other) {
        return (isBlackTeam() && other.isBlackTeam())
            || (isWhiteTeam() && other.isWhiteTeam());
    }

    public boolean isSameTeam(final Team team) {
        return this.team.equals(team);
    }

    private boolean isBlackTeam() {
        return team.isBlack();
    }

    private boolean isWhiteTeam() {
        return team.isWhite();
    }

    public boolean isPawn() {
        return false;
    }

    public boolean isKing() {
        return false;
    }

    public int getX() {
        return location.getX();
    }

    public int getY() {
        return location.getY();
    }

    public Team getTeam() {
        return team;
    }

    public String getTeamValue() {
        return team.getValue();
    }

    public char getPieceTypeValue() {
        return getPieceType().getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Piece piece = (Piece) o;
        return team == piece.team && Objects.equals(location, piece.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, location);
    }

    public abstract PieceType getPieceType();

}