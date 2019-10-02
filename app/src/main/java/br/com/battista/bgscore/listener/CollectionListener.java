package br.com.battista.bgscore.listener;

import br.com.battista.bgscore.model.response.CollectionResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CollectionListener {

    String URI_COLLECTION_OWN = "collection/{username}?own=1";
    String URI_COLLECTION_WISHLIST = "collection/{username}?wishlist=1";
    String URI_COLLECTION_PLAYED = "collection/{username}?played=1";

    @GET(URI_COLLECTION_OWN)
    Call<CollectionResponse> searchCollectionOwn(@Path("username") String username);

    @GET(URI_COLLECTION_WISHLIST)
    Call<CollectionResponse> searchCollectionWishlist(@Path("username") String username);

    @GET(URI_COLLECTION_PLAYED)
    Call<CollectionResponse> searchCollectionPlayed(@Path("username") String username);

}
