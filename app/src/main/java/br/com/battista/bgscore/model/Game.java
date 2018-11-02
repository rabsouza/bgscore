package br.com.battista.bgscore.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.Set;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.constants.EntityConstant;
import br.com.battista.bgscore.constants.LocaleConstant;
import br.com.battista.bgscore.model.enuns.BadgeGameEnum;
import br.com.battista.bgscore.repository.contract.DatabaseContract.GameEntry;
import br.com.battista.bgscore.util.LogUtils;

@Table(name = GameEntry.TABLE_NAME)
public class Game extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = GameEntry.COLUMN_NAME_NAME, notNull = true)
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
    private Long rating = null;

    @Column(name = GameEntry.COLUMN_NAME_MY_GAME)
    private Boolean myGame = Boolean.TRUE;

    @Column(name = GameEntry.COLUMN_NAME_FAVORITE)
    private Boolean favorite = Boolean.FALSE;

    @Column(name = GameEntry.COLUMN_NAME_WANT_GAME)
    private Boolean wantGame = Boolean.FALSE;

    @Column(name = GameEntry.COLUMN_NAME_BADGE_GAME)
    private BadgeGameEnum badgeGame = BadgeGameEnum.BADGE_GAME_NONE;

    @Column(name = GameEntry.COLUMN_NAME_EXPANSIONS)
    private Set<ExpansionGame> expansions = Sets.newLinkedHashSet();

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
        if (Strings.isNullOrEmpty(urlInfo)) {
            return EntityConstant.DEFAULT_URL_INFO_GAME;
        } else {
            return urlInfo;
        }
    }

    public void setUrlInfo(String urlInfo) {
        this.urlInfo = urlInfo;
    }

    public String getUrlBuy() {
        if (MainApplication.instance().getCurrentLocale().equals(LocaleConstant.DEFAULT_LOCALE)) {
            return EntityConstant.DEFAULT_URL_BUY_GAME.concat(getName());
        } else {
            return EntityConstant.DEFAULT_URL_BUY_GAME_ENG.concat(getName());
        }
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

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public Boolean isMyGame() {
        return myGame;
    }

    public void setMyGame(Boolean myGame) {
        this.myGame = myGame;
    }

    public Boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Boolean isWantGame() {
        return wantGame;
    }

    public void setWantGame(Boolean wantGame) {
        this.wantGame = wantGame;
    }

    public BadgeGameEnum getBadgeGame() {
        if (badgeGame == null) {
            badgeGame = BadgeGameEnum.BADGE_GAME_NONE;
        }
        return badgeGame;
    }

    public void setBadgeGame(BadgeGameEnum badgeGame) {
        if (badgeGame == null) {
            this.badgeGame = BadgeGameEnum.BADGE_GAME_NONE;
        } else {
            this.badgeGame = badgeGame;
        }
    }

    public Set<ExpansionGame> getExpansions() {
        return expansions;
    }

    public void setExpansions(Set<ExpansionGame> expansions) {
        this.expansions = expansions;
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
        if (!LogUtils.isLoggable()) {
            return EntityConstant.EMPTY_STRING;
        }

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
                .add("myGame", myGame)
                .add("favorite", favorite)
                .add("wantGame", wantGame)
                .add("badgeGame", badgeGame)
                .add("expansions", expansions)
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

    public Game rating(Long rating) {
        this.rating = rating;
        return this;
    }

    public Game rating(String rating) {
        this.rating = Long.valueOf(rating);
        return this;
    }

    public Game myGame(Boolean myGame) {
        this.myGame = myGame;
        return this;
    }

    public Game favorite(Boolean favorite) {
        this.favorite = favorite;
        return this;
    }

    public Game badgeGame(BadgeGameEnum badgeGame) {
        this.badgeGame = badgeGame;
        return this;
    }

    public Game expansions(Set<ExpansionGame> expansions) {
        this.expansions = expansions;
        return this;
    }

    public Game wantGame(Boolean wantGame) {
        this.wantGame = wantGame;
        return this;
    }
}
