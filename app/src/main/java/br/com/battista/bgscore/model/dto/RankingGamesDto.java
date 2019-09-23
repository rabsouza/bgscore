package br.com.battista.bgscore.model.dto;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Date;

import br.com.battista.bgscore.constants.EntityConstant;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.util.LogUtils;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class RankingGamesDto implements Serializable, Comparable<RankingGamesDto> {

    private static final long serialVersionUID = 1L;

    private Game game;
    private int countMatches = 0;
    private int countWinner = 0;
    private Date lastPlayed;
    private Long duration;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getCountMatches() {
        return countMatches;
    }

    public void setCountMatches(int countMatches) {
        this.countMatches = countMatches;
    }

    public int getCountWinners() {
        return countWinner;
    }

    public void setCountWinner(int countWinner) {
        this.countWinner = countWinner;
    }

    public Date getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
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
        if (!LogUtils.isLoggable()) {
            return EntityConstant.EMPTY_STRING;
        }

        return MoreObjects.toStringHelper(this)
                .add("game", game)
                .add("countMatches", countMatches)
                .add("countWinners", countWinner)
                .add("lastPlayed", lastPlayed)
                .add("duration", duration)
                .toString();
    }

    public RankingGamesDto game(Game game) {
        this.game = game;
        return this;
    }

    public RankingGamesDto countMatches(int count) {
        this.countMatches = count;
        return this;
    }

    public RankingGamesDto countWinners(int countWinner) {
        this.countWinner = countWinner;
        return this;
    }

    public RankingGamesDto lastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
        return this;
    }

    public RankingGamesDto duration(Long duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public int compareTo(@NonNull RankingGamesDto rankingGamesDto) {
        if (rankingGamesDto == null) {
            return -1;
        }
        int compare = Integer.compare(getCountMatches(), rankingGamesDto.getCountMatches());
        if (compare != 0) {
            return compare;
        }

        if (getLastPlayed() == null) {
            return 1;
        }
        return getLastPlayed().compareTo(rankingGamesDto.getLastPlayed());
    }
}
