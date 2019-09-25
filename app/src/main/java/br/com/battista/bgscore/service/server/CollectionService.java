package br.com.battista.bgscore.service.server;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.constants.HttpStatus;
import br.com.battista.bgscore.constants.RestConstant;
import br.com.battista.bgscore.listener.CollectionListener;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.response.CollectionResponse;
import br.com.battista.bgscore.model.response.ItemCollectionResponse;
import br.com.battista.bgscore.model.response.ItemCollectionResponse.ItemCollectionType;
import br.com.battista.bgscore.service.BaseService;
import br.com.battista.bgscore.util.LogUtils;
import retrofit2.Response;

public class CollectionService extends BaseService {

    private static final String TAG = CollectionService.class.getSimpleName();
    private static final int RETRY_BGG_API = 3;
    private static final int WAIT_RESPONSES_MILLIS = 1000;


    public List<Game> searchCollectionOwn(@NonNull String username) {
        LogUtils.i(TAG, MessageFormat.format("Search Collection Own by username: {0} in server url:[{1}{2}{3}]!",
                username, RestConstant.REST_API_ENDPOINT, RestConstant.REST_API_VERSION,
                CollectionListener.URI_COLLECTION_OWN));

        CollectionListener listener = builder.create(CollectionListener.class);
        List<Game> games = Lists.newArrayList();
        int countRetry = 0;
        do {
            try {
                Response<CollectionResponse> response = listener.searchCollectionOwn(username).execute();
                searchCollectionWithRetry(games, response, true, false, false);
            } catch (Exception e) {
                LogUtils.e(TAG, e.getLocalizedMessage(), e);
                break;
            }
        } while (games.isEmpty() && countRetry++ <= RETRY_BGG_API);

        return games;
    }

    public List<Game> searchCollectionWishlist(@NonNull String username) {
        LogUtils.i(TAG, MessageFormat.format("Search Collection Wishlist by username: {0} in server url:[{1}{2}{3}]!",
                username, RestConstant.REST_API_ENDPOINT, RestConstant.REST_API_VERSION,
                CollectionListener.URI_COLLECTION_OWN));

        CollectionListener listener = builder.create(CollectionListener.class);
        List<Game> games = Lists.newArrayList();
        int countRetry = 0;
        do {
            try {
                Response<CollectionResponse> response = listener.searchCollectionWishlist(username).execute();
                searchCollectionWithRetry(games, response, false, true, false);
            } catch (Exception e) {
                LogUtils.e(TAG, e.getLocalizedMessage(), e);
                break;
            }
        } while (games.isEmpty() && countRetry++ <= RETRY_BGG_API);

        return games;
    }

    public List<Game> searchCollectionPlayed(@NonNull String username) {
        LogUtils.i(TAG, MessageFormat.format("Search Collection Played by username: {0} in server url:[{1}{2}{3}]!",
                username, RestConstant.REST_API_ENDPOINT, RestConstant.REST_API_VERSION,
                CollectionListener.URI_COLLECTION_OWN));

        CollectionListener listener = builder.create(CollectionListener.class);
        List<Game> games = Lists.newArrayList();
        int countRetry = 0;
        do {
            try {
                final Response<CollectionResponse> response = listener.searchCollectionPlayed(username).execute();
                searchCollectionWithRetry(games, response, false, false, true);
            } catch (Exception e) {
                LogUtils.e(TAG, e.getLocalizedMessage(), e);
                break;
            }
        } while (games.isEmpty() && countRetry++ <= RETRY_BGG_API);

        return games;
    }

    private void searchCollectionWithRetry(List<Game> games, Response<CollectionResponse> response,
                                           boolean own, boolean wishlist, boolean played) throws IOException {

        if (response.code() == HttpStatus.NO_CONTENT.value()) {
            LogUtils.i(TAG, "Empty collection!");

        } else if (response.code() == HttpStatus.ACCEPTED.value()) {
            try {
                wait(WAIT_RESPONSES_MILLIS);
            } catch (InterruptedException e) {
                LogUtils.i(TAG, "Wait for collection!");
            }
        } else if (response.code() == HttpStatus.OK.value() && response.body() != null) {

            final List<ItemCollectionResponse> items = response.body().getItems();
            for (ItemCollectionResponse item : items) {
                if (!ItemCollectionType.boardgame.name().equals(item.getSubtype())) {
                    break;
                }

                Game game = new Game()
                        .idBGG(item.getBoardgameId())
                        .myGame(own)
                        .wantGame(wishlist)
                        .maxPlayers(item.getMaxPlayers())
                        .minPlayers(item.getMinPlayers())
                        .maxPlayTime(item.getMaxPlayTime())
                        .minPlayTime(item.getMinPlayTime())
                        .urlImage(item.getImage())
                        .urlThumbnail(item.getThumbnail());
                game.initEntity();
                games.add(game);
            }
            LogUtils.i(TAG, MessageFormat.format("Found {0} games!", games.size()));
        } else {
            String errorMessage = MessageFormat.format(
                    "Not found games! Return the code status: {0}.",
                    HttpStatus.valueOf(response.code()));
            validateErrorResponse(response, errorMessage);
        }

    }

}
