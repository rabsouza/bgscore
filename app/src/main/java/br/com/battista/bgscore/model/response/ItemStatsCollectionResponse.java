package br.com.battista.bgscore.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "stats", strict = false)
public class ItemStatsCollectionResponse implements Serializable, Parcelable {

    public static final Creator<ItemStatsCollectionResponse> CREATOR = new Creator<ItemStatsCollectionResponse>() {
        @Override
        public ItemStatsCollectionResponse createFromParcel(Parcel source) {
            return new ItemStatsCollectionResponse(source);
        }

        @Override
        public ItemStatsCollectionResponse[] newArray(int size) {
            return new ItemStatsCollectionResponse[size];
        }
    };

    private static final long serialVersionUID = 1L;

    @Attribute(name = "minplayers", required = false)
    private String minPlayers;
    @Attribute(name = "maxplayers", required = false)
    private String maxPlayers;
    @Attribute(name = "minplaytime", required = false)
    private String minPlayTime;
    @Attribute(name = "maxplaytime", required = false)
    private String maxPlayTime;

    public ItemStatsCollectionResponse() {
    }

    protected ItemStatsCollectionResponse(Parcel in) {
        this.minPlayers = in.readString();
        this.maxPlayers = in.readString();
        this.minPlayTime = in.readString();
        this.maxPlayTime = in.readString();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemStatsCollectionResponse that = (ItemStatsCollectionResponse) o;
        return Objects.equal(getMinPlayers(), that.getMinPlayers()) &&
                Objects.equal(getMaxPlayers(), that.getMaxPlayers()) &&
                Objects.equal(getMinPlayTime(), that.getMinPlayTime()) &&
                Objects.equal(getMaxPlayTime(), that.getMaxPlayTime());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMinPlayers(), getMaxPlayers(), getMinPlayTime(), getMaxPlayTime());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("minPlayers", minPlayers)
                .add("maxPlayers", maxPlayers)
                .add("minPlayTime", minPlayTime)
                .add("maxPlayTime", maxPlayTime)
                .toString();
    }

    public ItemStatsCollectionResponse minPlayers(final String minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public ItemStatsCollectionResponse maxPlayers(final String maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public ItemStatsCollectionResponse minPlayTime(final String minPlayTime) {
        this.minPlayTime = minPlayTime;
        return this;
    }

    public ItemStatsCollectionResponse maxPlayTime(final String maxPlayTime) {
        this.maxPlayTime = maxPlayTime;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.minPlayers);
        dest.writeString(this.maxPlayers);
        dest.writeString(this.minPlayTime);
        dest.writeString(this.maxPlayTime);
    }
}
