package br.com.battista.bgscore.listener;

import br.com.battista.bgscore.model.response.LoadGameResponse;
import br.com.battista.bgscore.model.response.SearchGameResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GameListener {

    String URI_SEARCH_GAME = "search";
    String URI_LOAD_GAME = "boardgame/{boardgameId}";

    @GET(URI_SEARCH_GAME)
    Call<SearchGameResponse> search(@Query("search") String name);

    @GET(URI_LOAD_GAME)
    Call<LoadGameResponse> load(@Path("boardgameId") Long boardgameId);

}
