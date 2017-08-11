package br.com.battista.bgscore.repository;


import com.google.common.collect.Sets;

import android.util.Log;

import com.orm.query.Select;

import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.model.ExpansionGame;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.repository.contract.DatabaseContract;
import br.com.battista.bgscore.repository.contract.DatabaseContract.GameEntry;

public class GameRepository extends BaseRepository implements Repository<Game> {

    public static final String TAG = GameRepository.class.getSimpleName();

    @Override
    public void save(Game entity) {
        if (entity != null) {
            Log.i(TAG, MessageFormat.format("Save to Game with name: {0}.", entity.getName()));
            saveEntity(entity);
        } else {
            Log.w(TAG, "Entity can not be null!");
        }
    }

    @Override
    public void saveAll(List<Game> entities) {
        if (entities != null) {
            Log.i(TAG, MessageFormat.format("Save {0} Games.", entities.size()));
            for (Game entity : entities) {
                if (entity != null) {
                    Log.i(TAG, MessageFormat.format("Save to Game with key: {0}.", entity.getId()));
                    saveEntity(entity);
                }
            }
        } else {
            Log.w(TAG, "Entities can not be null!");
        }
    }

    public Game find(Long id) {
        Log.i(TAG, MessageFormat.format("Find the Game by key: {0}.", id));
        final Game game = Game.findById(Game.class, id);
        reload(game);
        return game;
    }

    public Game findByBoardGameId(Long boardgameId) {
        Log.i(TAG, MessageFormat.format("Find the Game by boardgameId: {0}.", boardgameId));
        final Game game = Select
                .from(Game.class)
                .where(MessageFormat.format("{0} = ?", GameEntry.COLUMN_NAME_ID_BGG),
                        new String[]{String.valueOf(boardgameId)})
                .first();
        reload(game);
        return game;
    }

    @Override
    public void delete(Game entity) {
        if (entity != null) {
            entity.reloadId();
            Log.i(TAG, MessageFormat.format("Delete to Game with id: {0}.", entity.getId()));
            Game.deleteAll(entity.getClass(),
                    MessageFormat.format("{0} = ?", DatabaseContract.BaseEntry.COLUMN_NAME_ID),
                    String.valueOf(entity.getId()));
        } else {
            Log.w(TAG, "Entity can not be null!");
        }
    }

    @Override
    public List<Game> findAll() {
        Log.i(TAG, "Find all Games.");
        final List<Game> games = Select
                .from(Game.class)
                .orderBy(MessageFormat.format("{0} DESC, {1} ASC",
                        DatabaseContract.BaseEntry.COLUMN_NAME_UPDATED_AT, GameEntry.COLUMN_NAME_NAME))
                .list();
        reload(games);
        return games;
    }

    public List<Game> findAll(String orderBy) {
        Log.i(TAG, "Find all Games.");
        final List<Game> games = Select
                .from(Game.class)
                .orderBy(orderBy)
                .list();
        reload(games);
        return games;
    }

    @Override
    public void deleteAll() {
        Log.i(TAG, "Delete all Games.");
        Game.deleteAll(Game.class);
    }

    private void reload(List<Game> entities) {
        for (Game game : entities) {
            reload(game);
        }
    }

    private void reload(Game entity) {
        Log.i(TAG, "Reload data Expansions Game.");
        if (entity != null) {
            reloadEntity(entity);
            if (entity.getId() != null) {
                List<ExpansionGame> expansionsGame =
                        new ExpansionGameRepository().findByGameId(entity.getId());
                entity.expansions(Sets.newLinkedHashSet(expansionsGame));
            }
        }
    }
}
