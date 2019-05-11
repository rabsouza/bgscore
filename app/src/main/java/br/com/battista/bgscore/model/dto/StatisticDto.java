package br.com.battista.bgscore.model.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

import java.io.Serializable;

import br.com.battista.bgscore.constants.EntityConstant;
import br.com.battista.bgscore.util.LogUtils;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer countMatchVeryDissatisfied = 0;
    private Integer countMatchDissatisfied = 0;
    private Integer countMatchNeutral = 0;
    private Integer countMatchSatisfied = 0;
    private Integer countMatchVerySatisfied = 0;

    private Integer countGameMyGame = 0;
    private Integer countGameFavorite = 0;
    private Integer countGameWantGame = 0;

    public Integer getCountMatchVeryDissatisfied() {
        return countMatchVeryDissatisfied;
    }

    public void setCountMatchVeryDissatisfied(Integer countMatchVeryDissatisfied) {
        this.countMatchVeryDissatisfied = countMatchVeryDissatisfied;
    }

    public Integer getCountMatchDissatisfied() {
        return countMatchDissatisfied;
    }

    public void setCountMatchDissatisfied(Integer countMatchDissatisfied) {
        this.countMatchDissatisfied = countMatchDissatisfied;
    }

    public Integer getCountMatchNeutral() {
        return countMatchNeutral;
    }

    public void setCountMatchNeutral(Integer countMatchNeutral) {
        this.countMatchNeutral = countMatchNeutral;
    }

    public Integer getCountMatchSatisfied() {
        return countMatchSatisfied;
    }

    public void setCountMatchSatisfied(Integer countMatchSatisfied) {
        this.countMatchSatisfied = countMatchSatisfied;
    }

    public Integer getCountMatchVerySatisfied() {
        return countMatchVerySatisfied;
    }

    public void setCountMatchVerySatisfied(Integer countMatchVerySatisfied) {
        this.countMatchVerySatisfied = countMatchVerySatisfied;
    }

    public Integer getCountGameMyGame() {
        return countGameMyGame;
    }

    public void setCountGameMyGame(Integer countGameMyGame) {
        this.countGameMyGame = countGameMyGame;
    }

    public Integer getCountGameFavorite() {
        return countGameFavorite;
    }

    public void setCountGameFavorite(Integer countGameFavorite) {
        this.countGameFavorite = countGameFavorite;
    }

    public Integer getCountGameWantGame() {
        return countGameWantGame;
    }

    public void setCountGameWantGame(Integer countGameWantGame) {
        this.countGameWantGame = countGameWantGame;
    }

    public StatisticDto countMatchVeryDissatisfied(Integer countMatchVeryDissatisfied) {
        this.countMatchVeryDissatisfied = countMatchVeryDissatisfied;
        return this;
    }

    public StatisticDto countMatchDissatisfied(Integer countMatchDissatisfied) {
        this.countMatchDissatisfied = countMatchDissatisfied;
        return this;
    }

    public StatisticDto countMatchNeutral(Integer countMatchNeutral) {
        this.countMatchNeutral = countMatchNeutral;
        return this;
    }

    public StatisticDto countMatchSatisfied(Integer countMatchSatisfied) {
        this.countMatchSatisfied = countMatchSatisfied;
        return this;
    }

    public StatisticDto countMatchVerySatisfied(Integer countMatchVerySatisfied) {
        this.countMatchVerySatisfied = countMatchVerySatisfied;
        return this;
    }

    public StatisticDto countGameMyGame(Integer countGameMyGame) {
        this.countGameMyGame = countGameMyGame;
        return this;
    }

    public StatisticDto countGameFavorite(Integer countGameFavorite) {
        this.countGameFavorite = countGameFavorite;
        return this;
    }

    public StatisticDto countGameWantGame(Integer countGameWantGame) {
        this.countGameWantGame = countGameWantGame;
        return this;
    }

    @Override
    public String toString() {
        if (!LogUtils.isLoggable()) {
            return EntityConstant.EMPTY_STRING;
        }

        return MoreObjects.toStringHelper(this)
                .add("countMatchVeryDissatisfied", countMatchVeryDissatisfied)
                .add("countMatchDissatisfied", countMatchDissatisfied)
                .add("countMatchNeutral", countMatchNeutral)
                .add("countMatchSatisfied", countMatchSatisfied)
                .add("countMatchVerySatisfied", countMatchVerySatisfied)
                .add("countGameMyGame", countGameMyGame)
                .add("countGameFavorite", countGameFavorite)
                .add("countGameWantGame", countGameWantGame)
                .toString();
    }
}
