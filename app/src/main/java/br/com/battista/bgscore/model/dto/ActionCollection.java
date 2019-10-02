package br.com.battista.bgscore.model.dto;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;

import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.enuns.ActionCollectionEnum;

public class ActionCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    private ActionCollectionEnum action;
    private List<Game> gamesOwn;
    private List<Game> gamesWishlist;
    private List<Game> gamesPlayed;

    public ActionCollectionEnum getAction() {
        return action;
    }

    public void setAction(ActionCollectionEnum action) {
        this.action = action;
    }

    public List<Game> getGamesOwn() {
        return gamesOwn;
    }

    public void setGamesOwn(List<Game> gamesOwn) {
        this.gamesOwn = gamesOwn;
    }

    public List<Game> getGamesWishlist() {
        return gamesWishlist;
    }

    public void setGamesWishlist(List<Game> gamesWishlist) {
        this.gamesWishlist = gamesWishlist;
    }

    public List<Game> getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(List<Game> gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public ActionCollection action(final ActionCollectionEnum action) {
        this.action = action;
        return this;
    }

    public ActionCollection gamesOwn(final List<Game> gamesOwn) {
        this.gamesOwn = gamesOwn;
        return this;
    }

    public ActionCollection gamesWishlist(final List<Game> gamesWishlist) {
        this.gamesWishlist = gamesWishlist;
        return this;
    }

    public ActionCollection gamesPlayed(final List<Game> gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionCollection that = (ActionCollection) o;
        return getAction() == that.getAction();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getAction());
    }
}
