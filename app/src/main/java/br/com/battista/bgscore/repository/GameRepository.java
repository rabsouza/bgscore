package br.com.battista.bgscore.repository;


import android.util.Log;

import com.orm.query.Select;

import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.model.BaseEntity;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.repository.contract.DatabaseContract;
import br.com.battista.bgscore.repository.contract.DatabaseContract.GameEntry;

public class GameRepository implements Repository<Game> {

    public static final String TAG = GameRepository.class.getSimpleName();

    @Override
    public void save(Game entity) {
        if (entity != null) {
            Log.i(TAG, MessageFormat.format("Save to Game with id: {0}.", entity.getId()));
            saveEntity(entity);
        } else {
            Log.w(TAG, "Entity can not be null!");
        }
    }

    private void saveEntity(BaseEntity entity) {
        entity.synchronize();
        entity.save();
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
        return Game.findById(Game.class, id);
    }

    @Override
    public void delete(Game entity) {
        if (entity != null) {
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
        return Select
                .from(Game.class)
                .orderBy(MessageFormat.format("{0} DESC, {1} ASC",
                        DatabaseContract.BaseEntry.COLUMN_NAME_UPDATED_AT, GameEntry.COLUMN_NAME_NAME))
                .list();
    }

    @Override
    public void deleteAll() {
        Log.i(TAG, "Delete all Games.");
        Game.deleteAll(Game.class);
    }
}
