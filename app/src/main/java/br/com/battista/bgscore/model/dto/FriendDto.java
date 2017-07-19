package br.com.battista.bgscore.model.dto;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;

import android.support.annotation.DrawableRes;

import java.io.Serializable;

import br.com.battista.bgscore.R;

public class FriendDto implements Serializable, Comparable<FriendDto> {

    private static final long serialVersionUID = 1L;

    private String username;

    private String mail;

    @DrawableRes
    private int idResAvatar = R.drawable.avatar_friend;

    private boolean selected = Boolean.FALSE;

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

    public int getIdResAvatar() {
        return idResAvatar;
    }

    public void setIdResAvatar(int idResAvatar) {
        this.idResAvatar = idResAvatar;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendDto friendDto = (FriendDto) o;
        return Objects.equal(getUsername(), friendDto.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUsername());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("mail", mail)
                .add("idResAvatar", idResAvatar)
                .add("selected", selected)
                .toString();
    }

    @Override
    public int compareTo(FriendDto friendDto) {
        if (friendDto == null) {
            return -1;
        }
        if (Strings.isNullOrEmpty(getUsername())) {
            return 1;
        }
        return this.getUsername().compareTo(friendDto.getUsername());
    }

    public FriendDto username(String username) {
        this.username = username;
        return this;
    }

    public FriendDto mail(String mail) {
        this.mail = mail;
        return this;
    }

    public FriendDto idResAvatar(int idResAvatar) {
        this.idResAvatar = idResAvatar;
        return this;
    }

    public FriendDto selected(boolean selected) {
        this.selected = selected;
        return this;
    }
}
