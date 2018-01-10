package br.com.battista.bgscore.model.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.orm.dsl.Ignore;

import java.io.Serializable;

import br.com.battista.bgscore.model.enuns.AvatarEnum;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendDto implements Serializable, Comparable<FriendDto> {

    private static final long serialVersionUID = 1L;

    private String username;

    private String mail;

    private AvatarEnum avatar = AvatarEnum.AVATAR_FRIEND;

    @Ignore
    @JsonIgnore
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

    public AvatarEnum getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarEnum avatar) {
        this.avatar = avatar;
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
                .add("avatar", avatar)
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

    public FriendDto selected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public FriendDto avatar(AvatarEnum avatar) {
        this.avatar = avatar;
        return this;
    }
}
