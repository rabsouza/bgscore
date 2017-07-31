package br.com.battista.bgscore.model.dto;

import android.support.annotation.NonNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Date;

import br.com.battista.bgscore.model.Game;

public class RankingGamesDto implements Serializable, Comparable<RankingGamesDto> {
    private static final long serialVersionUID = 1L;

    private Game game;
    private int count = 0;
    private Date lastPlayed;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RankingGamesDto)) return false;
        RankingGamesDto that = (RankingGamesDto) o;
        return Objects.equal(getGame(), that.getGame());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getGame());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("game", game)
                .add("count", count)
                .add("lastPlayed", lastPlayed)
                .toString();
    }

    public RankingGamesDto game(Game game) {
        this.game = game;
        return this;
    }

    public RankingGamesDto count(int count) {
        this.count = count;
        return this;
    }

    public RankingGamesDto lastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
        return this;
    }

    @Override
    public int compareTo(@NonNull RankingGamesDto rankingGamesDto) {
        if (rankingGamesDto == null) {
            return -1;
        }
        return Integer.compare(getCount(), rankingGamesDto.getCount());
    }
}
