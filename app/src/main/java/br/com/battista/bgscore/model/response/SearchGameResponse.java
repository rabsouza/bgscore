package br.com.battista.bgscore.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "boardgames", strict = false)
public class SearchGameResponse implements Serializable, Parcelable {

    public static final Parcelable.Creator<SearchGameResponse> CREATOR = new Parcelable.Creator<SearchGameResponse>() {
        @Override
        public SearchGameResponse createFromParcel(Parcel source) {
            return new SearchGameResponse(source);
        }

        @Override
        public SearchGameResponse[] newArray(int size) {
            return new SearchGameResponse[size];
        }
    };
    private static final long serialVersionUID = 1L;
    @ElementList(name = "boardgame", inline = true, required = false)
    private List<GameResponse> boardgames = Lists.newArrayList();

    public SearchGameResponse() {
    }

    protected SearchGameResponse(Parcel in) {
        this.boardgames = in.createTypedArrayList(GameResponse.CREATOR);
    }

    public List<GameResponse> getBoardgames() {
        return boardgames;
    }

    public void setBoardgames(List<GameResponse> boardgames) {
        this.boardgames = boardgames;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("boardgames", boardgames)
                .toString();
    }

    public SearchGameResponse boardgames(List<GameResponse> boardgames) {
        this.boardgames = boardgames;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.boardgames);
    }

}
