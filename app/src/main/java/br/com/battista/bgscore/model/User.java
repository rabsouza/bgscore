package br.com.battista.bgscore.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import br.com.battista.bgscore.BuildConfig;
import br.com.battista.bgscore.constants.EntityConstant;
import br.com.battista.bgscore.model.dto.FilterByDto;
import br.com.battista.bgscore.model.dto.FriendDto;
import br.com.battista.bgscore.model.dto.OrderByDto;
import br.com.battista.bgscore.model.dto.RankingGamesDto;
import br.com.battista.bgscore.model.dto.StatisticDto;
import br.com.battista.bgscore.model.enuns.AvatarEnum;
import br.com.battista.bgscore.model.enuns.TypePlayerEnum;
import br.com.battista.bgscore.repository.contract.DatabaseContract.UserEntry;
import br.com.battista.bgscore.util.LogUtils;

@Table(name = UserEntry.TABLE_NAME)
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = UserEntry.COLUMN_NAME_USERNAME, notNull = true)
    private String username;

    @Column(name = UserEntry.COLUMN_NAME_MAIL)
    private String mail = null;

    @Column(name = UserEntry.COLUMN_NAME_AVATAR)
    private AvatarEnum avatar = AvatarEnum.AVATAR_PROFILE;

    @Column(name = UserEntry.COLUMN_NAME_URL_AVATAR)
    private String urlAvatar;

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

    @Column(name = UserEntry.COLUMN_NAME_CUSTOM_FONT)
    private Boolean customFont = Boolean.TRUE;

    @Column(name = UserEntry.COLUMN_NAME_LAST_BUILD_VERSION)
    private Integer lastBuildVersion = BuildConfig.VERSION_CODE;

    @Column(name = UserEntry.COLUMN_NAME_ORDER_BY)
    private Map<String, OrderByDto> orderBy = Maps.newLinkedHashMap();

    @Column(name = UserEntry.COLUMN_NAME_ORDER_BY)
    private Map<String, FilterByDto> filterBy = Maps.newLinkedHashMap();

    @Column(name = UserEntry.COLUMN_NAME_AUTOMATIC_BACKUP)
    private Boolean automaticBackup = Boolean.FALSE;

    @Column(name = UserEntry.COLUMN_NAME_SIGNED_SUCCESSFULLY)
    private Boolean signedSuccessfully = Boolean.FALSE;

    @Column(name = UserEntry.COLUMN_NAME_STATISTIC)
    private StatisticDto statisticDto = new StatisticDto();

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

    public AvatarEnum getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarEnum avatar) {
        this.avatar = avatar;
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
        if (rankingGames == null) {
            rankingGames = Sets.newHashSet();
        }
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

    public Boolean isCustomFont() {
        return customFont;
    }

    public void setCustomFont(Boolean customFont) {
        this.customFont = customFont;
    }

    public Map<String, OrderByDto> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Map<String, OrderByDto> orderBy) {
        this.orderBy = orderBy;
    }

    public Boolean isAutomaticBackup() {
        return automaticBackup;
    }

    public void setAutomaticBackup(Boolean automaticBackup) {
        this.automaticBackup = automaticBackup;
    }

    public StatisticDto getStatisticDto() {
        return statisticDto;
    }

    public void setStatisticDto(StatisticDto statisticDto) {
        this.statisticDto = statisticDto;
    }

    public Boolean isSignedSuccessfully() {
        return signedSuccessfully;
    }

    public void setSignedSuccessfully(Boolean signedSuccessfully) {
        this.signedSuccessfully = signedSuccessfully;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public Map<String, FilterByDto> getFilterBy() {
        return filterBy;
    }

    public void setFilterBy(Map<String, FilterByDto> filterBy) {
        this.filterBy = filterBy;
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
        if (!LogUtils.isLoggable()) {
            return EntityConstant.EMPTY_STRING;
        }

        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("mail", mail)
                .add("avatar", avatar)
                .add("urlAvatar", avatar)
                .add("lastPlayed", lastPlayed)
                .add("numGames", numGames)
                .add("numMatches", numMatches)
                .add("totalTime", totalTime)
                .add("friends", friends)
                .add("rankingGames", rankingGames)
                .add("welcome", welcome)
                .add("customFont", customFont)
                .add("lastBuildVersion", lastBuildVersion)
                .add("orderBy", orderBy)
                .add("filterBy", filterBy)
                .add("automaticBackup", automaticBackup)
                .add("signedSuccessfully", signedSuccessfully)
                .add("statisticDto", statisticDto)
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

    public User avatar(AvatarEnum avatar) {
        this.avatar = avatar;
        return this;
    }

    public User customFont(Boolean customFont) {
        this.customFont = customFont;
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

    public void clearRankingGames() {
        rankingGames.clear();
    }

    public Player getMyPlayer() {
        Player player = new Player()
                .name(getUsername())
                .typePlayer(TypePlayerEnum.USER);
        player.initEntity();
        return player;
    }

    public OrderByDto getOrderBy(String orderBy) {
        return this.orderBy.get(orderBy);
    }

    public OrderByDto putOrderBy(String key, OrderByDto orderBy) {
        return this.orderBy.put(key, orderBy);
    }

    public void clearOrderBy() {
        orderBy.clear();
    }

    public boolean containsOrderBy(String key) {
        return orderBy.containsKey(key);
    }

    public User orderBy(Map<String, OrderByDto> orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public FilterByDto getFilterBy(String filterBy) {
        return this.filterBy.get(filterBy);
    }

    public FilterByDto putFilterBy(String key, FilterByDto filterByDto) {
        return this.filterBy.put(key, filterByDto);
    }

    public void clearFilterBy() {
        filterBy.clear();
    }

    public boolean containsFilterBy(String key) {
        return filterBy.containsKey(key);
    }

    public User filterBy(final Map<String, FilterByDto> filterBy) {
        this.filterBy = filterBy;
        return this;
    }

    public User automaticBackup(Boolean automaticBackup) {
        this.automaticBackup = automaticBackup;
        return this;
    }

    public User signedSuccessfully(Boolean signedSuccessfully) {
        this.signedSuccessfully = signedSuccessfully;
        return this;
    }

    public User statisticDto(StatisticDto statisticDto) {
        this.statisticDto = statisticDto;
        return this;
    }
}
