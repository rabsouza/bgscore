package br.com.battista.bgscore.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "boardgames", strict = false)
public class LoadGameResponse implements Serializable, Parcelable {

    private static final long serialVersionUID = 1L;

    @Element(name = "boardgame", required = false)
    private GameResponse boardgame;

    public LoadGameResponse() {
    }

    protected LoadGameResponse(Parcel in) {
        this.boardgame = in.readParcelable(GameResponse.class.getClassLoader());
    }

    public static final Creator<LoadGameResponse> CREATOR = new Creator<LoadGameResponse>() {
        @Override
        public LoadGameResponse createFromParcel(Parcel source) {
            return new LoadGameResponse(source);
        }

        @Override
        public LoadGameResponse[] newArray(int size) {
            return new LoadGameResponse[size];
        }
    };

    public GameResponse getBoardgame() {
        return boardgame;
    }

    public void setBoardgame(GameResponse boardgame) {
        this.boardgame = boardgame;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoadGameResponse)) return false;
        LoadGameResponse that = (LoadGameResponse) o;
        return Objects.equal(getBoardgame(), that.getBoardgame());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getBoardgame());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("boardgame", boardgame)
                .toString();
    }


    public LoadGameResponse boardgame(GameResponse boardgame) {
        this.boardgame = boardgame;
        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.boardgame, flags);
    }


}
