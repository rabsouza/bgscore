package br.com.battista.bgscore.model;

import android.support.annotation.IdRes;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.repository.contract.DatabaseContract.MatchEntry;

@Table(name = MatchEntry.TABLE_NAME)
public class Match extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = MatchEntry.COLUMN_NAME_ALIAS, notNull = true)
    private String alias;

    @Column(name = MatchEntry.COLUMN_NAME_GAME)
    private Game game = null;

    @Column(name = MatchEntry.FK_GAME_ID)
    private Long gameId;

    @Column(name = MatchEntry.COLUMN_NAME_PLAYERS)
    private List<Player> players = Lists.newArrayList();

    @Column(name = MatchEntry.COLUMN_NAME_I_PLAYING)
    private Boolean iPlaying = Boolean.TRUE;

    @Column(name = MatchEntry.COLUMN_NAME_FINISHED)
    private Boolean finished = Boolean.FALSE;

    @Column(name = MatchEntry.COLUMN_NAME_DURATION)
    private Long duration = 0L;

    @IdRes
    @Column(name = MatchEntry.COLUMN_NAME_FEEDBACK)
    private int feedbackIdRes = R.drawable.ic_feedback_neutral;

    @Column(name = MatchEntry.COLUMN_NAME_OBS)
    private String obs;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        if (game != null && game.getId() != null) {
            gameId(game.getId());
        }
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Boolean isFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public int getFeedbackIdRes() {
        return feedbackIdRes;
    }

    public void setFeedbackIdRes(@IdRes int feedbackIdRes) {
        this.feedbackIdRes = feedbackIdRes;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Boolean isIPlaying() {
        return iPlaying;
    }

    public void setiPlaying(Boolean iPlaying) {
        this.iPlaying = iPlaying;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("alias", alias)
                .add("game", game)
                .add("gameId", gameId)
                .add("players", players)
                .add("iPlaying", iPlaying)
                .add("finished", finished)
                .add("duration", duration)
                .add("feedbackIdRes", feedbackIdRes)
                .add("obs", obs)
                .addValue(super.toString())
                .toString();
    }

    public Match alias(String alias) {
        this.alias = alias;
        return this;
    }

    public Match game(Game game) {
        this.game = game;
        if (game != null && game.getId() != null) {
            gameId(game.getId());
        }
        return this;
    }

    public Match gameId(Long gameId) {
        this.gameId = gameId;
        return this;
    }

    public Match players(List<Player> players) {
        this.players = players;
        return this;
    }

    public Match finished(Boolean finished) {
        this.finished = finished;
        return this;
    }

    public Match duration(Long duration) {
        this.duration = duration;
        return this;
    }

    public Match feedbackIdRes(@IdRes int feedbackIdRes) {
        this.feedbackIdRes = feedbackIdRes;
        return this;
    }

    public Match obs(String obs) {
        this.obs = obs;
        return this;
    }

    public Match iPlaying(Boolean iPlaying) {
        this.iPlaying = iPlaying;
        return this;
    }

    public boolean addPlayer(Player player) {
        return players.add(player);
    }

    public boolean removePlayer(Object o) {
        return players.remove(o);
    }

    public void clearPlayers() {
        players.clear();
    }

}
