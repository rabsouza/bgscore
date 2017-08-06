package br.com.battista.bgscore.service;

import br.com.battista.bgscore.service.server.GameService;

public class Inject {

    public synchronized static GameService provideGameService() {
        return new GameService();
    }

}
