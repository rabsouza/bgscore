package br.com.battista.bgscore.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.List;

import br.com.battista.bgscore.repository.contract.DatabaseContract.MatchEntry;

@Table(name = MatchEntry.TABLE_NAME)
public class Match extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = MatchEntry.COLUMN_NAME_ALIAS, notNull = true)
    private String alias;

    @Column(name = MatchEntry.COLUMN_NAME_GAME)
    private Game game = null;

    @Column(name = MatchEntry.COLUMN_NAME_PLAYERS)
    private List<Player> players = Lists.newArrayList();

    @Column(name = MatchEntry.COLUMN_NAME_FINISH)
    private Boolean finish = Boolean.FALSE;

    @Column(name = MatchEntry.COLUMN_NAME_DURATION)
    private Long duration = 0L;

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
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("alias", alias)
                .add("game", game)
                .add("players", players)
                .add("finish", finish)
                .add("duration", duration)
                .addValue(super.toString())
                .toString();
    }

    public Match alias(String alias) {
        this.alias = alias;
        return this;
    }

    public Match game(Game game) {
        this.game = game;
        return this;
    }

    public Match players(List<Player> players) {
        this.players = players;
        return this;
    }

    public Match finish(Boolean finish) {
        this.finish = finish;
        return this;
    }

    public Match duration(Long duration) {
        this.duration = duration;
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
