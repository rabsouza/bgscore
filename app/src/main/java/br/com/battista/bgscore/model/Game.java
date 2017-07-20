package br.com.battista.bgscore.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.io.Serializable;

import br.com.battista.bgscore.repository.contract.DatabaseContract.GameEntry;

@Table(name = GameEntry.TABLE_NAME)
public class Game extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = GameEntry.COLUMN_NAME_NAME, notNull = true, unique = true)
    private String name;

    @Column(name = GameEntry.COLUMN_NAME_ID_BGG)
    private Long idBGG = null;

    @Column(name = GameEntry.COLUMN_NAME_URL_THUMBNAIL)
    private String urlThumbnail = null;

    @Column(name = GameEntry.COLUMN_NAME_URL_IMAGE)
    private String urlImage = null;

    @Column(name = GameEntry.COLUMN_NAME_URL_INFO)
    private String urlInfo = null;

    @Column(name = GameEntry.COLUMN_NAME_YEAR_PUBLISHED)
    private String yearPublished = null;

    @Column(name = GameEntry.COLUMN_NAME_MIN_PLAYERS)
    private String minPlayers = null;

    @Column(name = GameEntry.COLUMN_NAME_MAX_PLAYERS)
    private String maxPlayers = null;

    @Column(name = GameEntry.COLUMN_NAME_MIN_PLAY_TIME)
    private String minPlayTime = null;

    @Column(name = GameEntry.COLUMN_NAME_MAX_PLAY_TIME)
    private String maxPlayTime = null;

    @Column(name = GameEntry.COLUMN_NAME_AGE)
    private String age = null;

    @Column(name = GameEntry.COLUMN_NAME_RATING)
    private String rating = null;

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

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrlInfo() {
        return urlInfo;
    }

    public void setUrlInfo(String urlInfo) {
        this.urlInfo = urlInfo;
    }

    public String getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(String yearPublished) {
        this.yearPublished = yearPublished;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        if (!super.equals(o)) return false;
        Game game = (Game) o;
        return Objects.equal(getName(), game.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getName());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("idBGG", idBGG)
                .add("urlThumbnail", urlThumbnail)
                .add("urlImage", urlImage)
                .add("urlInfo", urlInfo)
                .add("yearPublished", yearPublished)
                .add("minPlayers", minPlayers)
                .add("maxPlayers", maxPlayers)
                .add("minPlayTime", minPlayTime)
                .add("maxPlayTime", maxPlayTime)
                .add("age", age)
                .add("rating", rating)
                .addValue(super.toString())
                .toString();
    }

    public Game name(String name) {
        this.name = name;
        return this;
    }

    public Game idBGG(Long idBGG) {
        this.idBGG = idBGG;
        return this;
    }

    public Game urlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
        return this;
    }

    public Game urlImage(String urlImage) {
        this.urlImage = urlImage;
        return this;
    }

    public Game urlInfo(String urlInfo) {
        this.urlInfo = urlInfo;
        return this;
    }

    public Game yearPublished(String yearPublished) {
        this.yearPublished = yearPublished;
        return this;
    }

    public Game minPlayers(String minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public Game maxPlayers(String maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public Game minPlayTime(String minPlayTime) {
        this.minPlayTime = minPlayTime;
        return this;
    }

    public Game maxPlayTime(String maxPlayTime) {
        this.maxPlayTime = maxPlayTime;
        return this;
    }

    public Game age(String age) {
        this.age = age;
        return this;
    }

    public Game rating(String rating) {
        this.rating = rating;
        return this;
    }
}
