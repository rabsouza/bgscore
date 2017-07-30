package br.com.battista.bgscore.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.io.Serializable;

import br.com.battista.bgscore.repository.contract.DatabaseContract.PlayerEntry;

@Table(name = PlayerEntry.TABLE_NAME)
public class Player extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = PlayerEntry.COLUMN_NAME_NAME, notNull = true)
    private String name;

    @Column(name = PlayerEntry.FK_MATCH_ID)
    private Long matchId;

    @Column(name = PlayerEntry.COLUMN_NAME_PUNCTUATION)
    private String punctuation = null;

    @Column(name = PlayerEntry.COLUMN_NAME_WINNER)
    private Boolean winner = Boolean.FALSE;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getPunctuation() {
        return punctuation;
    }

    public void setPunctuation(String punctuation) {
        this.punctuation = punctuation;
    }

    public Boolean getWinner() {
        return winner;
    }

    public void setWinner(Boolean winner) {
        this.winner = winner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        if (!super.equals(o)) return false;
        Player player = (Player) o;
        return Objects.equal(getName(), player.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getName());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("matchId", matchId)
                .add("punctuation", punctuation)
                .add("winner", winner)
                .addValue(super.toString())
                .toString();
    }

    public Player name(String name) {
        this.name = name;
        return this;
    }

    public Player matchId(Long matchId) {
        this.matchId = matchId;
        return this;
    }

    public Player punctuation(String punctuation) {
        this.punctuation = punctuation;
        return this;
    }

    public Player winner(Boolean winner) {
        this.winner = winner;
        return this;
    }
}
