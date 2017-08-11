package br.com.battista.bgscore.model.response;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Root(name = "boardgame", strict = false)
public class GameResponse implements Serializable, Parcelable {
    private static final long serialVersionUID = 1L;

    @Attribute(name = "objectid")
    private Long boardgameId;
    @ElementMap(entry = "name", key = "primary", attribute = true, inline = true)
    private Map<Boolean, String> nameMap;
    @Element(name = "yearpublished", required = false)
    private String yearPublished;
    @Element(name = "minplayers", required = false)
    private String minPlayers;
    @Element(name = "maxplayers", required = false)
    private String maxPlayers;
    @Element(name = "minplaytime", required = false)
    private String minPlayTime;
    @Element(name = "maxplaytime", required = false)
    private String maxPlayTime;
    @Element(name = "age", required = false)
    private String age;
    @Element(name = "thumbnail", required = false)
    private String thumbnail;
    @Element(name = "image", required = false)
    private String image;
    @ElementMap(entry = "boardgameexpansion", key = "objectid", attribute = true, inline = true, required = false)
    private Map<Long, String> boardgameExpansionMap;

    public static final Creator<GameResponse> CREATOR = new Creator<GameResponse>() {
        @Override
        public GameResponse createFromParcel(Parcel source) {
            return new GameResponse(source);
        }

        @Override
        public GameResponse[] newArray(int size) {
            return new GameResponse[size];
        }
    };

    public GameResponse() {
    }

    protected GameResponse(Parcel in) {
        this.boardgameId = (Long) in.readValue(Long.class.getClassLoader());
        int nameMapSize = in.readInt();
        this.nameMap = new HashMap<Boolean, String>(nameMapSize);
        for (int i = 0; i < nameMapSize; i++) {
            Boolean key = (Boolean) in.readValue(Boolean.class.getClassLoader());
            String value = in.readString();
            this.nameMap.put(key, value);
        }
        this.yearPublished = in.readString();
        this.minPlayers = in.readString();
        this.maxPlayers = in.readString();
        this.minPlayTime = in.readString();
        this.maxPlayTime = in.readString();
        this.age = in.readString();
        this.thumbnail = in.readString();
        this.image = in.readString();
        int boardgameExpansionMapSize = in.readInt();
        this.boardgameExpansionMap = new HashMap<Long, String>(boardgameExpansionMapSize);
        for (int i = 0; i < boardgameExpansionMapSize; i++) {
            Long key = (Long) in.readValue(Long.class.getClassLoader());
            String value = in.readString();
            this.boardgameExpansionMap.put(key, value);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.boardgameId);
        dest.writeInt(this.nameMap.size());
        for (Map.Entry<Boolean, String> entry : this.nameMap.entrySet()) {
            dest.writeValue(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeString(this.yearPublished);
        dest.writeString(this.minPlayers);
        dest.writeString(this.maxPlayers);
        dest.writeString(this.minPlayTime);
        dest.writeString(this.maxPlayTime);
        dest.writeString(this.age);
        dest.writeString(this.thumbnail);
        dest.writeString(this.image);
        dest.writeInt(this.boardgameExpansionMap.size());
        for (Map.Entry<Long, String> entry : this.boardgameExpansionMap.entrySet()) {
            dest.writeValue(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    public String getName() {
        if (nameMap != null) {
            return nameMap.get(Boolean.TRUE);
        }
        return null;
    }

    public void setName(String name) {
        if (nameMap != null) {
            nameMap.put(Boolean.TRUE, name);
        }
    }

    public GameResponse name(String name) {
        setName(name);
        return this;
    }

    public Long getBoardgameId() {
        return boardgameId;
    }

    public void setBoardgameId(Long boardgameId) {
        this.boardgameId = boardgameId;
    }

    public Map<Boolean, String> getNameMap() {
        return nameMap;
    }

    public void setNameMap(Map<Boolean, String> nameMap) {
        this.nameMap = nameMap;
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

    public Map<Long, String> getBoardgameExpansionMap() {
        return boardgameExpansionMap;
    }

    public void setBoardgameExpansionMap(Map<Long, String> boardgameExpansionMap) {
        this.boardgameExpansionMap = boardgameExpansionMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameResponse that = (GameResponse) o;
        return Objects.equal(getBoardgameId(), that.getBoardgameId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getBoardgameId());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("boardgameId", boardgameId)
                .add("nameMap", nameMap)
                .add("yearPublished", yearPublished)
                .add("minPlayers", minPlayers)
                .add("maxPlayers", maxPlayers)
                .add("minPlayTime", minPlayTime)
                .add("maxPlayTime", maxPlayTime)
                .add("age", age)
                .add("thumbnail", thumbnail)
                .add("image", image)
                .add("boardgameExpansionMap", boardgameExpansionMap)
                .toString();
    }

    public GameResponse boardgameId(Long boardgameId) {
        this.boardgameId = boardgameId;
        return this;
    }

    public GameResponse nameMap(Map<Boolean, String> nameMap) {
        this.nameMap = nameMap;
        return this;
    }

    public GameResponse yearPublished(String yearPublished) {
        this.yearPublished = yearPublished;
        return this;
    }

    public GameResponse minPlayers(String minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public GameResponse maxPlayers(String maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public GameResponse minPlayTime(String minPlayTime) {
        this.minPlayTime = minPlayTime;
        return this;
    }

    public GameResponse maxPlayTime(String maxPlayTime) {
        this.maxPlayTime = maxPlayTime;
        return this;
    }

    public GameResponse age(String age) {
        this.age = age;
        return this;
    }

    public GameResponse thumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public GameResponse image(String image) {
        this.image = image;
        return this;
    }

    public GameResponse boardgameExpansionMap(Map<Long, String> boardgameExpansionMap) {
        this.boardgameExpansionMap = boardgameExpansionMap;
        return this;
    }

}
