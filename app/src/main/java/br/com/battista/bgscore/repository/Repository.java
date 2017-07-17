package br.com.battista.bgscore.repository;


import java.util.List;

import br.com.battista.bgscore.model.BaseEntity;

public interface Repository<Entity extends BaseEntity> {

    void saveAll(List<Entity> entities);

    void save(Entity entity);

    List<Entity> findAll();

    void deleteAll();
}
