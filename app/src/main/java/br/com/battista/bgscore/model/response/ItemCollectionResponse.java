package br.com.battista.bgscore.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "item", strict = false)
public class ItemCollectionResponse implements Serializable, Parcelable {


    private static final long serialVersionUID = 1L;

    @Attribute(name = "objectid")
    private Long boardGameId;
    @Attribute(name = "subtype")
    private String subtype;
    @Element(name = "name")
    private String name;
    @Element(name = "yearpublished")
    private String yearPublished;
    @Element(name = "thumbnail", required = false)
    private String thumbnail;
    @Element(name = "image", required = false)
    private String image;
    @Element(name = "stats")
    private ItemStatsCollectionResponse stats;

    public ItemCollectionResponse() {
    }

    public Long getBoardGameId() {
        return boardGameId;
    }

    public void setBoardGameId(Long boardGameId) {
        this.boardGameId = boardGameId;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(String yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ItemStatsCollectionResponse getStats() {
        return stats;
    }

    public void setStats(ItemStatsCollectionResponse stats) {
        this.stats = stats;
    }

    public String getMinPlayers() {
        return stats.getMinPlayers();
    }

    public String getMaxPlayers() {
        return stats.getMaxPlayers();
    }

    public String getMinPlayTime() {
        return stats.getMinPlayTime();
    }

    public String getMaxPlayTime() {
        return stats.getMaxPlayTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCollectionResponse that = (ItemCollectionResponse) o;
        return Objects.equal(getBoardGameId(), that.getBoardGameId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getBoardGameId());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("boardGameId", boardGameId)
                .add("subtype", subtype)
                .add("name", name)
                .add("yearPublished", yearPublished)
                .add("thumbnail", thumbnail)
                .add("image", image)
                .add("stats", stats)
                .toString();
    }

    public ItemCollectionResponse boardgameId(final Long boardgameId) {
        this.boardGameId = boardgameId;
        return this;
    }

    public ItemCollectionResponse subtype(final String subtype) {
        this.subtype = subtype;
        return this;
    }

    public ItemCollectionResponse name(final String name) {
        this.name = name;
        return this;
    }

    public ItemCollectionResponse yearpublished(final String yearpublished) {
        this.yearPublished = yearpublished;
        return this;
    }

    public ItemCollectionResponse thumbnail(final String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public ItemCollectionResponse image(final String image) {
        this.image = image;
        return this;
    }

    public ItemCollectionResponse stats(final ItemStatsCollectionResponse stats) {
        this.stats = stats;
        return this;
    }

    public enum ItemCollectionType{
        boardgame
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.boardGameId);
        dest.writeString(this.subtype);
        dest.writeString(this.name);
        dest.writeString(this.yearPublished);
        dest.writeString(this.thumbnail);
        dest.writeString(this.image);
        dest.writeParcelable(this.stats, flags);
    }

    protected ItemCollectionResponse(Parcel in) {
        this.boardGameId = (Long) in.readValue(Long.class.getClassLoader());
        this.subtype = in.readString();
        this.name = in.readString();
        this.yearPublished = in.readString();
        this.thumbnail = in.readString();
        this.image = in.readString();
        this.stats = in.readParcelable(ItemStatsCollectionResponse.class.getClassLoader());
    }

    public static final Creator<ItemCollectionResponse> CREATOR = new Creator<ItemCollectionResponse>() {
        @Override
        public ItemCollectionResponse createFromParcel(Parcel source) {
            return new ItemCollectionResponse(source);
        }

        @Override
        public ItemCollectionResponse[] newArray(int size) {
            return new ItemCollectionResponse[size];
        }
    };
}
