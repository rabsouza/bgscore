package br.com.battista.bgscore.repository;


import android.util.Log;

import com.orm.query.Select;

import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.model.BaseEntity;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.repository.contract.DatabaseContract;
import br.com.battista.bgscore.repository.contract.DatabaseContract.MatchEntry;

public class MatchRepository implements Repository<Match> {

    public static final String TAG = MatchRepository.class.getSimpleName();

    @Override
    public void save(Match entity) {
        if (entity != null) {
            Log.i(TAG, MessageFormat.format("Save to Match with id: {0}.", entity.getId()));
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
    public void saveAll(List<Match> entities) {
        if (entities != null) {
            Log.i(TAG, MessageFormat.format("Save {0} Matches.", entities.size()));
            for (Match entity : entities) {
                if (entity != null) {
                    Log.i(TAG, MessageFormat.format("Save to Match with key: {0}.", entity.getId()));
                    saveEntity(entity);
                }
            }
        } else {
            Log.w(TAG, "Entities can not be null!");
        }
    }

    public Match find(Long id) {
        Log.i(TAG, MessageFormat.format("Find the Match by key: {0}.", id));
        return Match.findById(Match.class, id);
    }

    @Override
    public void delete(Match entity) {
        if (entity != null) {
            Log.i(TAG, MessageFormat.format("Delete to Match with id: {0}.", entity.getId()));
            Match.deleteAll(entity.getClass(),
                    MessageFormat.format("{0} = ?", DatabaseContract.BaseEntry.COLUMN_NAME_ID),
                    String.valueOf(entity.getId()));
        } else {
            Log.w(TAG, "Entity can not be null!");
        }
    }

    @Override
    public List<Match> findAll() {
        Log.i(TAG, "Find all Matches.");
        return Select
                .from(Match.class)
                .orderBy(MessageFormat.format("{0} DESC, {1} ASC",
                        DatabaseContract.BaseEntry.COLUMN_NAME_UPDATED_AT, MatchEntry.COLUMN_NAME_ALIAS))
                .list();
    }

    @Override
    public void deleteAll() {
        Log.i(TAG, "Delete all Matches.");
        Match.deleteAll(Match.class);
    }
}
