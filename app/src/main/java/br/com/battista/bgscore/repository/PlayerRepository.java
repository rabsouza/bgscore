package br.com.battista.bgscore.repository;


import android.util.Log;

import com.orm.query.Select;

import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.model.Player;
import br.com.battista.bgscore.repository.contract.DatabaseContract;
import br.com.battista.bgscore.repository.contract.DatabaseContract.PlayerEntry;

public class PlayerRepository extends BaseRepository implements Repository<Player> {

    public static final String TAG = PlayerRepository.class.getSimpleName();

    @Override
    public void save(Player entity) {
        if (entity != null) {
            Log.i(TAG, MessageFormat.format("Save to Player with name: {0}.", entity.getName()));
            saveEntity(entity);
        } else {
            Log.w(TAG, "Entity can not be null!");
        }
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
        final Player player = Player.findById(Player.class, id);
        reloadEntity(player);
        return player;
    }

    @Override
    public void delete(Player entity) {
        if (entity != null) {
            entity.reloadId();
            Log.i(TAG, MessageFormat.format("Delete to Player with id: {0}.", entity.getId()));
            Player.deleteAll(entity.getClass(),
                    MessageFormat.format("{0} = ?", DatabaseContract.BaseEntry.COLUMN_NAME_ID),
                    String.valueOf(entity.getId()));
        } else {
            Log.w(TAG, "Entity can not be null!");
        }
    }

    @Override
    public List<Player> findAll() {
        Log.i(TAG, "Find all Players.");
        final List<Player> players = Select
                .from(Player.class)
                .orderBy(MessageFormat.format("{0} DESC, {1} ASC",
                        DatabaseContract.BaseEntry.COLUMN_NAME_UPDATED_AT, PlayerEntry.COLUMN_NAME_NAME))
                .list();
        reloadEntity(players);
        return players;
    }

    public List<Player> findByMatchId(Long idMatch) {
        Log.i(TAG, MessageFormat.format("Find Players by Id Match: {0}.", idMatch));
        final List<Player> players = Select
                .from(Player.class)
                .where(MessageFormat.format("{0} = ?", PlayerEntry.FK_MATCH_ID),
                        new String[]{String.valueOf(idMatch)})
                .orderBy(MessageFormat.format("{0} DESC, {1} ASC",
                        DatabaseContract.BaseEntry.COLUMN_NAME_UPDATED_AT, PlayerEntry.COLUMN_NAME_NAME))
                .list();
        reloadEntity(players);
        return players;
    }

    @Override
    public void deleteAll() {
        Log.i(TAG, "Delete all Players.");
        Player.deleteAll(Player.class);
    }
}
