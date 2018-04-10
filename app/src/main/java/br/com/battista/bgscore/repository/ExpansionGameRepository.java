package br.com.battista.bgscore.repository;


import android.util.Log;

import com.orm.query.Select;

import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.model.ExpansionGame;
import br.com.battista.bgscore.repository.contract.DatabaseContract.BaseEntry;
import br.com.battista.bgscore.repository.contract.DatabaseContract.ExpansionGameEntry;

public class ExpansionGameRepository extends BaseRepository implements Repository<ExpansionGame> {

    private static final String TAG = ExpansionGameRepository.class.getSimpleName();

    @Override
    public void save(ExpansionGame entity) {
        if (entity != null) {
            Log.i(TAG, MessageFormat.format("Save to Expansion Game with name: {0}.", entity.getName()));
            saveEntity(entity);
        } else {
            Log.w(TAG, "Entity can not be null!");
        }
    }

    @Override
    public void saveAll(List<ExpansionGame> entities) {
        if (entities != null) {
            Log.i(TAG, MessageFormat.format("Save {0} Expansion the Expansions Game.", entities.size()));
            for (ExpansionGame entity : entities) {
                if (entity != null) {
                    Log.i(TAG, MessageFormat.format("Save to Expansion Game with key: {0}.", entity.getId()));
                    saveEntity(entity);
                }
            }
        } else {
            Log.w(TAG, "Entities can not be null!");
        }
    }

    public ExpansionGame find(Long id) {
        Log.i(TAG, MessageFormat.format("Find the Expansion Game by key: {0}.", id));
        final ExpansionGame game = ExpansionGame.findById(ExpansionGame.class, id);
        reloadEntity(game);
        return game;
    }

    public ExpansionGame findByBoardGameId(Long boardgameId) {
        Log.i(TAG, MessageFormat.format("Find the Expansion Game by boardgameId: {0}.", boardgameId));
        final ExpansionGame game = Select
                .from(ExpansionGame.class)
                .where(MessageFormat.format("{0} = ?", ExpansionGameEntry.COLUMN_NAME_ID_BGG),
                        new String[]{String.valueOf(boardgameId)})
                .first();
        reloadEntity(game);
        return game;
    }

    public List<ExpansionGame> findByGameId(Long idGame) {
        Log.i(TAG, MessageFormat.format("Find Expansions Game by Id Match: {0}.", idGame));
        final List<ExpansionGame> expansionsGame = Select
                .from(ExpansionGame.class)
                .where(MessageFormat.format("{0} = ?", ExpansionGameEntry.FK_GAME_ID),
                        new String[]{String.valueOf(idGame)})
                .orderBy(MessageFormat.format("{0} DESC, {1} ASC",
                        BaseEntry.COLUMN_NAME_UPDATED_AT, ExpansionGameEntry.COLUMN_NAME_NAME))
                .list();
        reloadEntity(expansionsGame);
        return expansionsGame;
    }

    @Override
    public void delete(ExpansionGame entity) {
        if (entity != null) {
            entity.reloadId();
            Log.i(TAG, MessageFormat.format("Delete to Expansion Game with id: {0}.", entity.getId()));
            ExpansionGame.deleteAll(entity.getClass(),
                    MessageFormat.format("{0} = ?", BaseEntry.COLUMN_NAME_ID),
                    String.valueOf(entity.getId()));
        } else {
            Log.w(TAG, "Entity can not be null!");
        }
    }

    @Override
    public List<ExpansionGame> findAll() {
        Log.i(TAG, "Find all Expansions Game.");
        final List<ExpansionGame> games = Select
                .from(ExpansionGame.class)
                .orderBy(MessageFormat.format("{0} DESC, {1} ASC",
                        BaseEntry.COLUMN_NAME_UPDATED_AT, ExpansionGameEntry.COLUMN_NAME_NAME))
                .list();
        reloadEntity(games);
        return games;
    }

    public List<ExpansionGame> findAll(String orderBy) {
        Log.i(TAG, "Find all Expansions Game.");
        final List<ExpansionGame> games = Select
                .from(ExpansionGame.class)
                .orderBy(orderBy)
                .list();
        reloadEntity(games);
        return games;
    }

    @Override
    public void deleteAll() {
        Log.i(TAG, "Delete all Expansions Game.");
        ExpansionGame.deleteAll(ExpansionGame.class);
    }
}
