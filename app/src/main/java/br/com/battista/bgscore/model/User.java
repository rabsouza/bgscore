package br.com.battista.bgscore.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import android.support.annotation.DrawableRes;

import com.orm.dsl.Column;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import br.com.battista.bgscore.BuildConfig;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.dto.FriendDto;
import br.com.battista.bgscore.model.dto.RankingGamesDto;
import br.com.battista.bgscore.repository.contract.DatabaseContract.UserEntry;

@Table(name = UserEntry.TABLE_NAME)
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = UserEntry.COLUMN_NAME_USERNAME, notNull = true)
    private String username;

    @Column(name = UserEntry.COLUMN_NAME_MAIL)
    private String mail = null;

    @Column(name = UserEntry.COLUMN_NAME_URL_AVATAR)
    @DrawableRes
    private int idResAvatar = R.drawable.avatar_profile;

    @Column(name = UserEntry.COLUMN_NAME_LAST_PLAY)
    private Date lastPlayed = null;

    @Column(name = UserEntry.COLUMN_NAME_NUM_GAMES)
    private Integer numGames = 0;

    @Column(name = UserEntry.COLUMN_NAME_NUM_MATCHES)
    private Integer numMatches = 0;

    @Column(name = UserEntry.COLUMN_NAME_TOTAL_TIME)
    private Long totalTime = 0L;

    @Column(name = UserEntry.COLUMN_NAME_FRIENDS)
    private Set<FriendDto> friends = Sets.newTreeSet();

    @Column(name = UserEntry.COLUMN_NAME_RANKING_GAMES)
    private Set<RankingGamesDto> rankingGames = Sets.newTreeSet();

    @Column(name = UserEntry.COLUMN_NAME_WELCOME)
    private Boolean welcome = Boolean.TRUE;

    @Column(name = UserEntry.COLUMN_NAME_LAST_BUILD_VERSION)
    private Integer lastBuildVersion = BuildConfig.VERSION_CODE;

    @Ignore
    private String orderByGames = null;

    @Ignore
    private String orderByMatches = null;

    public String getOrderByGames() {
        return orderByGames;
    }

    public void setOrderByGames(String orderByGames) {
        this.orderByGames = orderByGames;
    }

    public String getOrderByMatches() {
        return orderByMatches;
    }

    public void setOrderByMatches(String orderByMatches) {
        this.orderByMatches = orderByMatches;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @DrawableRes
    public int getIdResAvatar() {
        return idResAvatar;
    }

    public void setIdResAvatar(@DrawableRes int idResAvatar) {
        this.idResAvatar = idResAvatar;
    }

    public Date getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public Integer getNumGames() {
        return numGames;
    }

    public void setNumGames(Integer numGames) {
        this.numGames = numGames;
    }

    public Integer getNumMatches() {
        return numMatches;
    }

    public void setNumMatches(Integer numMatches) {
        this.numMatches = numMatches;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public Set<FriendDto> getFriends() {
        return friends;
    }

    public void setFriends(Set<FriendDto> friends) {
        this.friends = friends;
    }

    public Set<RankingGamesDto> getRankingGames() {
        return rankingGames;
    }

    public void setRankingGames(Set<RankingGamesDto> rankingGames) {
        this.rankingGames = rankingGames;
    }

    public Boolean isWelcome() {
        return welcome;
    }

    public void setWelcome(Boolean welcome) {
        this.welcome = welcome;
    }

    public Integer getLastBuildVersion() {
        return lastBuildVersion;
    }

    public void setLastBuildVersion(Integer lastBuildVersion) {
        this.lastBuildVersion = lastBuildVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equal(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getUsername());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("mail", mail)
                .add("idResAvatar", idResAvatar)
                .add("lastPlayed", lastPlayed)
                .add("numGames", numGames)
                .add("numMatches", numMatches)
                .add("totalTime", totalTime)
                .add("friends", friends)
                .add("rankingGames", rankingGames)
                .add("welcome", welcome)
                .add("lastBuildVersion", lastBuildVersion)
                .addValue(super.toString())
                .toString();
    }

    public User username(String username) {
        this.username = username;
        return this;
    }

    public User mail(String mail) {
        this.mail = mail;
        return this;
    }

    public User idResAvatar(@DrawableRes int idResAvatar) {
        this.idResAvatar = idResAvatar;
        return this;
    }

    public User lastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
        return this;
    }

    public User numGames(Integer numGames) {
        this.numGames = numGames;
        return this;
    }

    public User numMatches(Integer numMatches) {
        this.numMatches = numMatches;
        return this;
    }

    public User totalTime(Long totalTime) {
        this.totalTime = totalTime;
        return this;
    }

    public User friends(Set<FriendDto> friendsDto) {
        this.friends = friendsDto;
        return this;
    }

    public User rankingGames(Set<RankingGamesDto> rankingGames) {
        this.rankingGames = rankingGames;
        return this;
    }

    public User welcome(Boolean welcome) {
        this.welcome = welcome;
        return this;
    }

    public User lastBuildVersion(Integer lastBuildVersion) {
        this.lastBuildVersion = lastBuildVersion;
        return this;
    }

    public boolean addFriend(FriendDto friendDto) {
        return friends.add(friendDto);
    }

    public boolean removeFriend(FriendDto friendDto) {
        return friends.remove(friendDto);
    }

    public void clearFriends() {
        friends.clear();
    }

    public boolean addRankingGames(RankingGamesDto rankingGamesDto) {
        return rankingGames.add(rankingGamesDto);
    }

    public boolean removeRankingGames(RankingGamesDto rankingGamesDto) {
        return rankingGames.remove(rankingGamesDto);
    }

    public void clearRankingGamess() {
        rankingGames.clear();
    }

    public FriendDto getMyFriendDTO() {
        FriendDto userCurrent = new FriendDto()
                .username(getUsername())
                .mail(getMail())
                .idResAvatar(getIdResAvatar())
                .selected(Boolean.TRUE);
        return userCurrent;
    }

    public User orderByGames(String orderByGames) {
        this.orderByGames = orderByGames;
        return this;
    }

    public User orderByMatches(String orderByMatches) {
        this.orderByMatches = orderByMatches;
        return this;
    }

}
