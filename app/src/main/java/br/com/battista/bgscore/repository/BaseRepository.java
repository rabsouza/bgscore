package br.com.battista.bgscore.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import br.com.battista.bgscore.model.BaseEntity;

public abstract class BaseRepository {

    private static final String TAG = BaseRepository.class.getSimpleName();

    protected void saveEntity(@NonNull BaseEntity entity) {
        entity.reloadId();
        entity.synchronize();
        if (entity.getId() != null && entity.getId() > 0 && entity.getVersion() != null) {
            Log.i(TAG, "saveEntity: Update the entity!");
            entity.updateEntity();
            entity.update();
        } else {
            Log.i(TAG, "saveEntity: Save the entity!");
            entity.setId(entity.save());
            entity.update();
        }
    }

    protected void reloadEntity(@NonNull BaseEntity entity) {
        if(entity != null) {
            entity.reloadId();
        }
    }

    protected void reloadEntity(@NonNull List<? extends BaseEntity> entities) {
        for (BaseEntity entity : entities) {
            entity.reloadId();
        }
    }
}
