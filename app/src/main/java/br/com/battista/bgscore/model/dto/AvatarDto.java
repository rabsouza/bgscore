package br.com.battista.bgscore.model.dto;

import android.support.annotation.DrawableRes;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class AvatarDto {

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
}
