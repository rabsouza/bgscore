package br.com.battista.bgscore.model.dto;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.io.Serializable;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvatarDto implements Serializable, Comparable<AvatarDto> {

    private static final long serialVersionUID = 1L;

    @DrawableRes
    private int idResAvatar;

    private String nameAvatar;

    @DrawableRes
    public int getIdResAvatar() {
        return idResAvatar;
    }

    public void setIdResAvatar(@DrawableRes int idResAvatar) {
        this.idResAvatar = idResAvatar;
    }

    public String getNameAvatar() {
        return nameAvatar;
    }

    public void setNameAvatar(String nameAvatar) {
        this.nameAvatar = nameAvatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvatarDto avatarDto = (AvatarDto) o;
        return getIdResAvatar() == avatarDto.getIdResAvatar();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdResAvatar());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("idResAvatar", idResAvatar)
                .add("nameAvatar", nameAvatar)
                .toString();
    }

    public AvatarDto idResAvatar(@DrawableRes int idResAvatar) {
        this.idResAvatar = idResAvatar;
        return this;
    }

    public AvatarDto nameAvatar(String nameAvatar) {
        this.nameAvatar = nameAvatar;
        return this;
    }

    @Override
    public int compareTo(@NonNull AvatarDto avatarDto) {
        if (avatarDto == null) {
            return -1;
        }
        if (Strings.isNullOrEmpty(getNameAvatar())) {
            return 1;
        }
        return this.getNameAvatar().compareTo(avatarDto.getNameAvatar());
    }

}
