package br.com.battista.bgscore.repository;


import android.util.Log;

import com.orm.query.Select;

import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.model.BaseEntity;
import br.com.battista.bgscore.model.Player;
import br.com.battista.bgscore.repository.contract.DatabaseContract;
import br.com.battista.bgscore.repository.contract.DatabaseContract.PlayerEntry;

public class PlayerRepository implements Repository<Player> {

    public static final String TAG = PlayerRepository.class.getSimpleName();

    @Override
    public void save(Player entity) {
        if (entity != null) {
            Log.i(TAG, MessageFormat.format("Save to Player with id: {0}.", entity.getId()));
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
    public void saveAll(List<Player> entities) {
        if (entities != null) {
            Log.i(TAG, MessageFormat.format("Save {0} Players.", entities.size()));
            for (Player entity : entities) {
                if (entity != null) {
                    Log.i(TAG, MessageFormat.format("Save to Player with key: {0}.", entity.getId()));
                    saveEntity(entity);
                }
            }
        } else {
            Log.w(TAG, "Entities can not be null!");
        }
    }

    public Player find(Long id) {
        Log.i(TAG, MessageFormat.format("Find the Player by key: {0}.", id));
        return Player.findById(Player.class, id);
    }

    @Override
    public void delete(Player entity) {
        if (entity != null) {
            Log.i(TAG, MessageFormat.format("Delete to Player with id: {0}.", entity.getId()));
            Player.delete(entity);
        } else {
            Log.w(TAG, "Entity can not be null!");
        }
    }

    @Override
    public List<Player> findAll() {
        Log.i(TAG, "Find all Players.");
        return Select
                .from(Player.class)
                .orderBy(MessageFormat.format("{0} DESC, {1} ASC",
                        DatabaseContract.BaseEntry.COLUMN_NAME_UPDATED_AT, PlayerEntry.COLUMN_NAME_NAME))
                .list();
    }

    @Override
    public void deleteAll() {
        Log.i(TAG, "Delete all Players.");
        Player.deleteAll(Player.class);
    }
}
