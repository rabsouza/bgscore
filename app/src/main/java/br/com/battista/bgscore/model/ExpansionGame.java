package br.com.battista.bgscore.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.io.Serializable;

import br.com.battista.bgscore.constants.EntityConstant;
import br.com.battista.bgscore.repository.contract.DatabaseContract.ExpansionGameEntry;
import br.com.battista.bgscore.util.LogUtils;

@Table(name = ExpansionGameEntry.TABLE_NAME)
public class ExpansionGame extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = ExpansionGameEntry.COLUMN_NAME_NAME, notNull = true)
    private String name;

    @Column(name = ExpansionGameEntry.COLUMN_NAME_ID_BGG)
    private Long idBGG = null;

    @Column(name = ExpansionGameEntry.FK_GAME_ID)
    private Long gameId;

    @Column(name = ExpansionGameEntry.COLUMN_NAME_YEAR_PUBLISHED)
    private String yearPublished = null;

    @Column(name = ExpansionGameEntry.COLUMN_NAME_URL_THUMBNAIL)
    private String urlThumbnail = null;

    @Column(name = ExpansionGameEntry.COLUMN_NAME_MIN_PLAYERS)
    private String minPlayers = null;

    @Column(name = ExpansionGameEntry.COLUMN_NAME_MAX_PLAYERS)
    private String maxPlayers = null;

    @Column(name = ExpansionGameEntry.COLUMN_NAME_MIN_PLAY_TIME)
    private String minPlayTime = null;

    @Column(name = ExpansionGameEntry.COLUMN_NAME_MAX_PLAY_TIME)
    private String maxPlayTime = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIdBGG() {
        return idBGG;
    }

    public void setIdBGG(Long idBGG) {
        this.idBGG = idBGG;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
    }

    public String getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(String minPlayers) {
        this.minPlayers = minPlayers;
    }

    public String getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(String maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getMinPlayTime() {
        return minPlayTime;
    }

    public void setMinPlayTime(String minPlayTime) {
        this.minPlayTime = minPlayTime;
    }

    public String getMaxPlayTime() {
        return maxPlayTime;
    }

    public void setMaxPlayTime(String maxPlayTime) {
        this.maxPlayTime = maxPlayTime;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(String yearPublished) {
        this.yearPublished = yearPublished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpansionGame)) return false;
        if (!super.equals(o)) return false;
        ExpansionGame game = (ExpansionGame) o;
        return Objects.equal(getName(), game.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getName());
    }

    @Override
    public String toString() {
        if(!LogUtils.isLoggable()){
            return EntityConstant.EMPTY_STRING;
        }

        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("idBGG", idBGG)
                .add("gameId", gameId)
                .add("yearPublished", yearPublished)
                .add("urlThumbnail", urlThumbnail)
                .add("minPlayers", minPlayers)
                .add("maxPlayers", maxPlayers)
                .add("minPlayTime", minPlayTime)
                .add("maxPlayTime", maxPlayTime)
                .addValue(super.toString())
                .toString();
    }

    public ExpansionGame name(String name) {
        this.name = name;
        return this;
    }

    public ExpansionGame idBGG(Long idBGG) {
        this.idBGG = idBGG;
        return this;
    }

    public ExpansionGame urlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
        return this;
    }

    public ExpansionGame minPlayers(String minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public ExpansionGame maxPlayers(String maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public ExpansionGame minPlayTime(String minPlayTime) {
        this.minPlayTime = minPlayTime;
        return this;
    }

    public ExpansionGame maxPlayTime(String maxPlayTime) {
        this.maxPlayTime = maxPlayTime;
        return this;
    }

    public ExpansionGame gameId(Long gameId) {
        this.gameId = gameId;
        return this;
    }

    public ExpansionGame yearPublished(String yearPublished) {
        this.yearPublished = yearPublished;
        return this;
    }

}
