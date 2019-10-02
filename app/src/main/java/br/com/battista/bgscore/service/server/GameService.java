package br.com.battista.bgscore.service.server;

import android.support.annotation.NonNull;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.constants.EntityConstant;
import br.com.battista.bgscore.constants.HttpStatus;
import br.com.battista.bgscore.constants.RestConstant;
import br.com.battista.bgscore.listener.GameListener;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.response.GameResponse;
import br.com.battista.bgscore.model.response.LoadGameResponse;
import br.com.battista.bgscore.model.response.SearchGameResponse;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.service.BaseService;
import br.com.battista.bgscore.util.LogUtils;
import retrofit2.Response;

public class GameService extends BaseService {

    private static final String TAG = GameService.class.getSimpleName();

    public List<GameResponse> searchGame(@NonNull String name) {
        LogUtils.i(TAG, MessageFormat.format("Search BoardGame by name: {0} in server url:[{1}{2}{3}]!",
                name, RestConstant.REST_API_ENDPOINT, RestConstant.REST_API_VERSION,
                GameListener.URI_SEARCH_GAME));

        GameListener listener = builder.create(GameListener.class);
        List<GameResponse> games = Lists.newArrayList();
        try {
            Response<SearchGameResponse> response = listener.search(name).execute();
            if (response.code() == HttpStatus.NO_CONTENT.value()) {
                LogUtils.i(TAG, "Found 0 games!");
            } else if (response.code() == HttpStatus.OK.value() && response.body() != null) {
                final List<GameResponse> boardgames = response.body().getBoardgames();
                for (GameResponse gameResponse : boardgames) {
                    if (!Strings.isNullOrEmpty(gameResponse.getName()) &&
                            gameResponse.getBoardgameId() != 0 &&
                            !Strings.isNullOrEmpty(gameResponse.getYearPublished())) {
                        games.add(gameResponse);
                    }
                }
                LogUtils.i(TAG, MessageFormat.format("Found {0} games!", games.size()));
                return games;
            } else {
                String errorMessage = MessageFormat.format(
                        "Not found games! Return the code status: {0}.",
                        HttpStatus.valueOf(response.code()));
                validateErrorResponse(response, errorMessage);
            }
        } catch (IOException e) {
            LogUtils.e(TAG, e.getLocalizedMessage(), e);
        }

        return games;
    }

    public Game loadGame(@NonNull Long boardgameId) {
        LogUtils.i(TAG, MessageFormat.format("Load BoardGame by id: {0} in server url:[{1}{2}{3}]!",
                boardgameId, RestConstant.REST_API_ENDPOINT, RestConstant.REST_API_VERSION,
                GameListener.URI_LOAD_GAME));

        GameListener listener = builder.create(GameListener.class);
        Game game = null;
        try {
            Response<LoadGameResponse> response = listener.load(boardgameId).execute();
            if (response.code() == HttpStatus.NO_CONTENT.value()) {
                LogUtils.i(TAG, "Found 0 games!");
            } else if (response.code() == HttpStatus.OK.value() && response.body() != null) {
                GameResponse gameResponse = response.body().getBoardgame();
                LogUtils.i(TAG, MessageFormat.format("Found the game {0}!", gameResponse.getName()));

                final GameRepository gameRepository = new GameRepository();
                final Game gameBD = gameRepository.findByBoardGameId(boardgameId);
                if (gameBD == null || gameBD.getVersion() == null || gameBD.getPk() == null) {
                    game = new Game();
                    game.initEntity();
                } else {
                    game = gameBD;
                }
                game.name(gameResponse.getName());
                game.idBGG(gameResponse.getBoardgameId());
                game.urlInfo(EntityConstant.DEFAULT_URL_INFO_GAME + gameResponse.getBoardgameId());
                game.urlThumbnail(gameResponse.getThumbnail());
                game.urlImage(gameResponse.getImage());
                game.age(gameResponse.getAge());
                game.minPlayTime(gameResponse.getMinPlayTime());
                game.maxPlayTime(gameResponse.getMaxPlayTime());
                game.minPlayers(gameResponse.getMinPlayers());
                game.maxPlayers(gameResponse.getMaxPlayers());
                game.yearPublished(gameResponse.getYearPublished());

                LogUtils.i(TAG, "loadGame: Merge the entity with server and save in BD!");
                gameRepository.save(game);

                return game;
            } else {
                String errorMessage = MessageFormat.format(
                        "Not found games! Return the code status: {0}.",
                        HttpStatus.valueOf(response.code()));
                validateErrorResponse(response, errorMessage);
            }
        } catch (IOException e) {
            LogUtils.e(TAG, e.getLocalizedMessage(), e);
        }

        return game;
    }

    public Game reloadImageGame(@NonNull Game game) {
        LogUtils.i(TAG, MessageFormat.format("Load BoardGame by id: {0} in server url:[{1}{2}{3}]!",
                game.getIdBGG(), RestConstant.REST_API_ENDPOINT, RestConstant.REST_API_VERSION,
                GameListener.URI_LOAD_GAME));

        GameListener listener = builder.create(GameListener.class);
        try {
            Response<LoadGameResponse> response = listener.load(game.getIdBGG()).execute();
            if (response.code() == HttpStatus.NO_CONTENT.value()) {
                LogUtils.i(TAG, "Found 0 games!");
            } else if (response.code() == HttpStatus.OK.value() && response.body() != null) {
                GameResponse gameResponse = response.body().getBoardgame();
                LogUtils.i(TAG, MessageFormat.format("Found the game {0}!", gameResponse.getName()));

                final GameRepository gameRepository = new GameRepository();
                game.urlThumbnail(gameResponse.getThumbnail());
                game.urlImage(gameResponse.getImage());
                game.updateEntity();

                LogUtils.i(TAG, "loadGame: Merge the entity with server and save in BD!");
                gameRepository.save(game);

                return game;
            } else {
                String errorMessage = MessageFormat.format(
                        "Not found games! Return the code status: {0}.",
                        HttpStatus.valueOf(response.code()));
                validateErrorResponse(response, errorMessage);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getLocalizedMessage(), e);
        }

        return game;
    }
}
