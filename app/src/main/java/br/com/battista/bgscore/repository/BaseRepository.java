package br.com.battista.bgscore.repository;

import br.com.battista.bgscore.model.BaseEntity;

public abstract class BaseRepository {

    protected void saveEntity(BaseEntity entity) {
        entity.synchronize();
        if (entity.getId() != null && entity.getId() > 0 && entity.getVersion() != null) {
            entity.updateEntity();
            entity.setId(entity.update());
        } else {
            entity.setId(entity.save());
        }
        entity.update();
    }
}
