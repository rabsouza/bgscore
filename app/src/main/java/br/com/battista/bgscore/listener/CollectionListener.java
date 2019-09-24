package br.com.battista.bgscore.listener;

import br.com.battista.bgscore.model.response.CollectionResponse;
import br.com.battista.bgscore.model.response.LoadGameResponse;
import br.com.battista.bgscore.model.response.SearchGameResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CollectionListener {

    String URI_COLLECTION_OWN = "/collection/{username}?own=1";
    String URI_COLLECTION_WISHLIST = "/collection/{username}?wishlist=1";
    String URI_COLLECTION_PLAYED = "/collection/{username}?played=1";

    @GET(URI_COLLECTION_OWN)
    Call<CollectionResponse> loadCollectionOwn(@Path("username") String username);

    @GET(URI_COLLECTION_WISHLIST)
    Call<CollectionResponse> loadCollectionWishlist(@Path("username") String username);

    @GET(URI_COLLECTION_PLAYED)
    Call<CollectionResponse> loadCollectionPlayed(@Path("username") String username);

}
