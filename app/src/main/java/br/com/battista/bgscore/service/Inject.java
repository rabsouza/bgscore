package br.com.battista.bgscore.service;

import br.com.battista.bgscore.service.server.CollectionService;
import br.com.battista.bgscore.service.server.GameService;

public class Inject {

    public synchronized static GameService provideGameService() {
        return new GameService();
    }

    public synchronized static CacheManageService provideCacheManageService() {
        return CacheManageService.getInstance();
    }

    public synchronized static CollectionService provideCollectionService() {
        return new CollectionService();
    }

}
