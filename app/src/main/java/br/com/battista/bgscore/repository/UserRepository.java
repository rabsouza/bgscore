package br.com.battista.bgscore.repository;


import android.util.Log;

import com.orm.query.Select;

import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.model.BaseEntity;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.repository.contract.DatabaseContract;
import br.com.battista.bgscore.repository.contract.DatabaseContract.UserEntry;

public class UserRepository implements Repository<User> {

    public static final String TAG = UserRepository.class.getSimpleName();

    @Override
    public void save(User entity) {
        if (entity != null) {
            Log.i(TAG, MessageFormat.format("Save to User with id: {0}.", entity.getId()));
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
    public void saveAll(List<User> entities) {
        if (entities != null) {
            Log.i(TAG, MessageFormat.format("Save {0} Users.", entities.size()));
            for (User entity : entities) {
                if (entity != null) {
                    Log.i(TAG, MessageFormat.format("Save to User with key: {0}.", entity.getId()));
                    saveEntity(entity);
                }
            }
        } else {
            Log.w(TAG, "Entities can not be null!");
        }
    }

    public User find(Long id) {
        Log.i(TAG, MessageFormat.format("Find the User by key: {0}.", id));
        return User.findById(User.class, id);
    }

    @Override
    public void delete(User entity) {
        if (entity != null) {
            Log.i(TAG, MessageFormat.format("Delete to User with id: {0}.", entity.getId()));
            User.delete(entity);
        } else {
            Log.w(TAG, "Entity can not be null!");
        }
    }

    @Override
    public List<User> findAll() {
        Log.i(TAG, "Find all Users.");
        return Select
                .from(User.class)
                .orderBy(MessageFormat.format("{0} ASC, {1} ASC",
                        DatabaseContract.BaseEntry.COLUMN_NAME_UPDATED_AT, UserEntry.COLUMN_NAME_USERNAME))
                .list();
    }

    @Override
    public void deleteAll() {
        Log.i(TAG, "Delete all Users.");
        User.deleteAll(User.class);
    }
}
