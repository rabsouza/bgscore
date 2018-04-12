package br.com.battista.bgscore.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.io.Serializable;

import br.com.battista.bgscore.constants.EntityConstant;
import br.com.battista.bgscore.model.enuns.TypePlayerEnum;
import br.com.battista.bgscore.repository.contract.DatabaseContract.PlayerEntry;
import br.com.battista.bgscore.util.LogUtils;

@Table(name = PlayerEntry.TABLE_NAME)
public class Player extends BaseEntity implements Serializable, Comparable<Player> {

    private static final long serialVersionUID = 1L;

    @Column(name = PlayerEntry.COLUMN_NAME_NAME, notNull = true)
    private String name;

    @Column(name = PlayerEntry.FK_MATCH_ID)
    private Long matchId;

    @Column(name = PlayerEntry.COLUMN_NAME_PUNCTUATION)
    private String punctuation = null;

    @Column(name = PlayerEntry.COLUMN_NAME_WINNER)
    private Boolean winner = Boolean.FALSE;

    @Column(name = PlayerEntry.COLUMN_NAME_TYPE_PLAYER)
    private TypePlayerEnum typePlayer = TypePlayerEnum.PLAYER;

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

    public Boolean isWinner() {
        return winner;
    }

    public void setWinner(Boolean winner) {
        this.winner = winner;
    }

    public TypePlayerEnum getTypePlayer() {
        return typePlayer;
    }

    public void setTypePlayer(TypePlayerEnum typePlayer) {
        this.typePlayer = typePlayer;
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
    public int compareTo(Player player) {
        if (player == null) {
            return -1;
        }
        if (Strings.isNullOrEmpty(getName())) {
            return 1;
        }
        return this.getName().compareTo(player.getName());
    }

    @Override
    public String toString() {
        if(!LogUtils.isLoggable()){
            return EntityConstant.EMPTY_STRING;
        }

        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("matchId", matchId)
                .add("punctuation", punctuation)
                .add("winner", winner)
                .add("typePlayer", typePlayer)
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

    public Player typePlayer(TypePlayerEnum typePlayer) {
        this.typePlayer = typePlayer;
        return this;
    }
}
