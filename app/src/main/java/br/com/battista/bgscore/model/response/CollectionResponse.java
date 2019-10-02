package br.com.battista.bgscore.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "items", strict = false)
public class CollectionResponse implements Serializable, Parcelable {

    public static final Creator<CollectionResponse> CREATOR = new Creator<CollectionResponse>() {
        @Override
        public CollectionResponse createFromParcel(Parcel source) {
            return new CollectionResponse(source);
        }

        @Override
        public CollectionResponse[] newArray(int size) {
            return new CollectionResponse[size];
        }
    };

    private static final long serialVersionUID = 1L;

    @ElementList(name = "item", inline = true, required = false)
    private List<ItemCollectionResponse> items = Lists.newArrayList();

    public CollectionResponse() {
    }

    protected CollectionResponse(Parcel in) {
        this.items = in.createTypedArrayList(ItemCollectionResponse.CREATOR);
    }

    public List<ItemCollectionResponse> getItems() {
        return items;
    }

    public void setItems(List<ItemCollectionResponse> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionResponse that = (CollectionResponse) o;
        return Objects.equal(getItems(), that.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getItems());
    }

    public CollectionResponse items(final List<ItemCollectionResponse> items) {
        this.items = items;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.items);
    }
}
