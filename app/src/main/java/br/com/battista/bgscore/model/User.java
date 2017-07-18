package br.com.battista.bgscore.model;

import com.google.common.base.Objects;

import android.support.annotation.DrawableRes;

import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.Date;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.repository.contract.DatabaseContract.UserEntry;

@Table(name = UserEntry.TABLE_NAME)
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = UserEntry.COLUMN_NAME_USERNAME, notNull = true, unique = true)
    private String username;

    @Column(name = UserEntry.COLUMN_NAME_MAIL)
    private String mail = null;

    @Column(name = UserEntry.COLUMN_NAME_URL_AVATAR)
    @DrawableRes
    private int idResAvatar = R.drawable.profile;

    @Column(name = UserEntry.COLUMN_NAME_LAST_PLAY)
    private Date lastPlay = null;

    @Column(name = UserEntry.COLUMN_NAME_NUM_GAMES)
    private Integer numGames = 0;

    @Column(name = UserEntry.COLUMN_NAME_NUM_MATCHES)
    private Integer numMatches = 0;

    @Column(name = UserEntry.COLUMN_NAME_TOTAL_TIME)
    private Long totalTime = 0L;

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

    public Date getLastPlay() {
        return lastPlay;
    }

    public void setLastPlay(Date lastPlay) {
        this.lastPlay = lastPlay;
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
        return "User{" +
                "username='" + username + '\'' +
                ", mail='" + mail + '\'' +
                ", idResAvatar='" + idResAvatar + '\'' +
                ", lastPlay=" + lastPlay +
                ", numGames=" + numGames +
                ", numMatches=" + numMatches +
                ", totalTime=" + totalTime +
                '}';
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

    public User lastPlay(Date lastPlay) {
        this.lastPlay = lastPlay;
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
}
