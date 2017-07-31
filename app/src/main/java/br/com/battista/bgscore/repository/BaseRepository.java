package br.com.battista.bgscore.repository;

import android.support.annotation.NonNull;

import java.util.List;

import br.com.battista.bgscore.model.BaseEntity;

public abstract class BaseRepository {

    protected void saveEntity(@NonNull BaseEntity entity) {
        entity.synchronize();
        if (entity.getId() != null && entity.getId() > 0 && entity.getVersion() != null) {
            entity.updateEntity();
            entity.update();
        } else {
            entity.setId(entity.save());
            entity.update();
        }
    }

    protected void reloadEntity(@NonNull BaseEntity entity) {
        entity.reloadId();
    }

    protected void reloadEntity(@NonNull List<? extends BaseEntity> entities) {
        for (BaseEntity entity : entities) {
            entity.reloadId();
        }
    }
}
