package br.com.battista.bgscore.service;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.RankingGamesDto;
import br.com.battista.bgscore.model.dto.StatisticDto;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.model.enuns.ActionDatabaseEnum;
import br.com.battista.bgscore.model.enuns.FeedbackEnum;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.repository.MatchRepository;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.LogUtils;

public class CacheManageService {

    private static final String TAG = CacheManageService.class.getSimpleName();
    private static CacheManageService instance;

    public static CacheManageService getInstance() {
        if (instance == null) {
            instance = new CacheManageService();
        }
        return instance;
    }

    public void onCreate() {
        LogUtils.i(TAG, "onCreate: Register event bus to Action!");
        EventBus.getDefault().register(this);
    }

    public void reloadSyncAllDataCache() {
        LogUtils.i(TAG, "reloadSyncAllDataCache: Reload all data cache!!!");
        final MainApplication instance = MainApplication.instance();
        User user = instance.getUser();
        loadAllDataMatchAddToCache(user);
        loadAllDataRankingGamesAddToCache(user);
        loadAllDataGameAddToCache(user);
        loadAllDataStatisticGamesAddToCache(user);
        instance.setUser(user);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onActionCache(ActionCacheEnum action) {
        LogUtils.i(TAG, MessageFormat.format("onActionCache: Process to action: {0}.", action));
        final MainApplication instance = MainApplication.instance();
        User user = instance.getUser();

        if (ActionCacheEnum.LOAD_DATA_GAME.equals(action)) {
            loadAllDataRankingGamesAddToCache(user);
            loadAllDataGameAddToCache(user);
            loadAllDataStatisticGamesAddToCache(user);
            if (user.isAutomaticBackup()) {
                AndroidUtils.postAction(ActionDatabaseEnum.BACKUP_ALL_DATA);
            }
        } else if (ActionCacheEnum.LOAD_DATA_MATCHES.equals(action)) {
            loadAllDataMatchAddToCache(user);
            loadAllDataRankingGamesAddToCache(user);
            loadAllDataGameAddToCache(user);
            if (user.isAutomaticBackup()) {
                AndroidUtils.postAction(ActionDatabaseEnum.BACKUP_ALL_DATA);
            }
        } else if (ActionCacheEnum.LOAD_DATA_RANKING_GAMES.equals(action)) {
            loadAllDataRankingGamesAddToCache(user);
        } else if (ActionCacheEnum.LOAD_ALL_DATA.equals(action)) {
            loadAllDataMatchAddToCache(user);
            loadAllDataRankingGamesAddToCache(user);
            loadAllDataGameAddToCache(user);
        } else if (ActionCacheEnum.RELOAD_ALL_GAME_IMAGES.equals(action)) {
            reloadAllGameImages(user);
            if (user.isAutomaticBackup()) {
                AndroidUtils.postAction(ActionDatabaseEnum.BACKUP_ALL_DATA);
            }
        }

        instance.setUser(user);
    }

    private void reloadAllGameImages(User user) {
        LogUtils.i(TAG, "reloadAllGameImages: Reload all game images!");
        final List<Game> games = new GameRepository().findAll();
        for (Game game : games) {
            final long currentThreadTimeMillis = Calendar.getInstance().getTimeInMillis();
            final long gameUpdated = game.getUpdatedAt().getTime();
            if (game.getIdBGG() != null && game.getIdBGG() > 0 &&
                    currentThreadTimeMillis - gameUpdated >= TimeUnit.HOURS.toMillis(1)) {
                Inject.provideGameService().reloadImageGame(game);
            }
        }
    }

    private synchronized void loadAllDataMatchAddToCache(User user) {
        LogUtils.i(TAG, "loadAllDataMatchAddToCache: Update data match user!");
        final MatchRepository matchRepository = new MatchRepository();
        final List<Match> matches = matchRepository.findAll();
        user.setNumMatches(matches.size());
        if (matches.size() == 0) {
            user.lastPlayed(null);
        } else {
            Match lastMatch = matches.get(0);
            user.lastPlayed(lastMatch.getCreatedAt());
        }

        long totalTime = 0;
        for (Match match : matches) {
            totalTime += match.getDuration();
        }
        user.setTotalTime(totalTime);

        LogUtils.i(TAG, "loadAllDataMatchAddToCache: Update the scores to matches!");
        int countVeryDissatisfied = 0;
        int countDissatisfied = 0;
        int countNeutral = 0;
        int countSatisfied = 0;
        int countVerySatisfied = 0;
        for (Match match : matches) {
            if (FeedbackEnum.FEEDBACK_VERY_DISSATISFIED.equals(match.getFeedback())) {
                countVeryDissatisfied++;
            } else if (FeedbackEnum.FEEDBACK_DISSATISFIED.equals(match.getFeedback())) {
                countDissatisfied++;
            } else if (FeedbackEnum.FEEDBACK_SATISFIED.equals(match.getFeedback())) {
                countSatisfied++;
            } else if (FeedbackEnum.FEEDBACK_VERY_SATISFIED.equals(match.getFeedback())) {
                countVerySatisfied++;
            } else if (FeedbackEnum.FEEDBACK_NEUTRAL.equals(match.getFeedback())) {
                countNeutral++;
            }
        }
        final StatisticDto statistic = user.getStatisticDto();
        statistic.setCountMatchVeryDissatisfied(countVeryDissatisfied);
        statistic.setCountMatchDissatisfied(countDissatisfied);
        statistic.setCountMatchNeutral(countNeutral);
        statistic.setCountMatchSatisfied(countSatisfied);
        statistic.setCountMatchVerySatisfied(countVerySatisfied);
    }

    private synchronized void loadAllDataRankingGamesAddToCache(User user) {
        LogUtils.i(TAG, "loadAllDataRankingGamesAddToCache: Update data racking user!");
        final List<Game> games = new GameRepository().findAll();
        user.clearRankingGames();
        for (Game game : games) {
            final List<Match> matches = new MatchRepository().findByGameId(game.getId());
            if (!matches.isEmpty()) {

                Match lastMatch = matches.get(0);
                long durationTotal = 0;
                for (Match match : matches) {
                    durationTotal += match.getDuration();
                }
                final RankingGamesDto rankingGames = new RankingGamesDto()
                        .count(matches.size())
                        .lastPlayed(lastMatch.getCreatedAt())
                        .game(game)
                        .duration(durationTotal);
                user.addRankingGames(rankingGames);
            }
        }
    }

    private synchronized void loadAllDataStatisticGamesAddToCache(User user) {
        LogUtils.i(TAG, "loadAllDataStatisticGamesAddToCache: Update the scores to games!");
        final List<Game> games = new GameRepository().findAll();
        int countMyGame = 0;
        int countFavorite = 0;
        int countWantGame = 0;
        for (Game game : games) {
            if (game.isMyGame()) {
                countMyGame++;
            }
            if (game.isFavorite()) {
                countFavorite++;
            }
            if (game.isWantGame()) {
                countWantGame++;
            }
        }
        final StatisticDto statistic = user.getStatisticDto();
        statistic.countGameMyGame(countMyGame);
        statistic.countGameFavorite(countFavorite);
        statistic.countGameWantGame(countWantGame);

    }

    private synchronized void loadAllDataGameAddToCache(User user) {
        LogUtils.i(TAG, "loadAllDataGameAddToCache: Update data game user!");
        user.setNumGames(user.getRankingGames().size());
    }

    public void onDestroy() {
        LogUtils.i(TAG, "onCreate: Unregister event bus to Action!");
        EventBus.getDefault().unregister(this);
    }
}
