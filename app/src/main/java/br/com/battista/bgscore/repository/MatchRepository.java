package br.com.battista.bgscore.repository;


import com.orm.query.Select;

import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.Player;
import br.com.battista.bgscore.repository.contract.DatabaseContract.BaseEntry;
import br.com.battista.bgscore.repository.contract.DatabaseContract.MatchEntry;
import br.com.battista.bgscore.util.LogUtils;

import static br.com.battista.bgscore.constants.EntityConstant.SYNTAX_LIMIT_SQL_LITE;

public class MatchRepository extends BaseRepository implements Repository<Match> {

    private static final String TAG = MatchRepository.class.getSimpleName();

    @Override
    public void save(Match entity) {
        if (entity != null) {
            LogUtils.i(TAG, MessageFormat.format("Save to Match with alias: {0}.", entity.getAlias()));
            saveEntity(entity);
            for (Player player : entity.getPlayers()) {
                player.matchId(entity.getId());
                new PlayerRepository().save(player);
            }
        } else {
            LogUtils.w(TAG, "Entity can not be null!");
        }
    }

    @Override
    public void saveAll(List<Match> entities) {
        if (entities != null) {
            LogUtils.i(TAG, MessageFormat.format("Save {0} Matches.", entities.size()));
            for (Match entity : entities) {
                if (entity != null) {
                    LogUtils.i(TAG, MessageFormat.format("Save to Match with key: {0}.", entity.getId()));
                    saveEntity(entity);
                }
            }
        } else {
            LogUtils.w(TAG, "Entities can not be null!");
        }
    }

    public Match find(Long id) {
        LogUtils.i(TAG, MessageFormat.format("Find the Match by key: {0}.", id));
        final Match match = Match.findById(Match.class, id);
        reload(match);
        return match;
    }

    @Override
    public void delete(Match entity) {
        if (entity != null) {
            entity.reloadId();
            LogUtils.i(TAG, MessageFormat.format("Delete to Match with id: {0}.", entity.getId()));
            Match.deleteAll(entity.getClass(),
                    MessageFormat.format("{0} = ?", BaseEntry.COLUMN_NAME_ID),
                    String.valueOf(entity.getId()));
        } else {
            LogUtils.w(TAG, "Entity can not be null!");
        }
    }

    @Override
    public List<Match> findAll() {
        LogUtils.i(TAG, "Find all Matches.");
        final List<Match> matches = Select
                .from(Match.class)
                .orderBy(MessageFormat.format("{0} DESC, {1} ASC",
                        BaseEntry.COLUMN_NAME_CREATED_AT, MatchEntry.COLUMN_NAME_ALIAS))
                .list();
        if (matches != null) {
            for (Match match : matches) {
                reload(match);
            }
        }
        return matches;
    }

    public List<Match> findAll(String orderBy) {
        LogUtils.i(TAG, "Find all Matches.");
        final List<Match> matches = Select
                .from(Match.class)
                .orderBy(orderBy)
                .list();
        if (matches != null) {
            for (Match match : matches) {
                reload(match);
            }
        }
        return matches;
    }

    public List<Match> findAll(String orderBy, int offset, int limit) {
        LogUtils.i(TAG, String.format("Find all Matches orderly with offset[%s] and limit[%s].", offset, limit));
        final List<Match> matches = Select
                .from(Match.class)
                .orderBy(orderBy)
                .limit(String.format(SYNTAX_LIMIT_SQL_LITE, offset, limit))
                .list();
        if (matches != null) {
            for (Match match : matches) {
                reload(match);
            }
        }
        return matches;
    }

    public List<Match> findAll(int offset, int limit) {
        LogUtils.i(TAG, String.format("Find all Matches with offset[%s] and limit[%s].", offset, limit));
        final List<Match> matches = Select
                .from(Match.class)
                .orderBy(MessageFormat.format("{0} DESC, {1} ASC",
                        BaseEntry.COLUMN_NAME_CREATED_AT, MatchEntry.COLUMN_NAME_ALIAS))
                .limit(String.format(SYNTAX_LIMIT_SQL_LITE, offset, limit))
                .list();
        if (matches != null) {
            for (Match match : matches) {
                reload(match);
            }
        }
        return matches;
    }

    public List<Match> findByGameId(Long idGame) {
        LogUtils.i(TAG, MessageFormat.format("Find Matches by Id Game: {0}.", idGame));
        final List<Match> matches = Select
                .from(Match.class)
                .where(MessageFormat.format("{0} = ?", MatchEntry.FK_GAME_ID),
                        new String[]{String.valueOf(idGame)})
                .orderBy(MessageFormat.format("{0} DESC, {1} ASC",
                        BaseEntry.COLUMN_NAME_CREATED_AT, MatchEntry.COLUMN_NAME_ALIAS))
                .list();
        if (matches != null) {
            for (Match match : matches) {
                reload(match);
            }
        }
        return matches;
    }

    @Override
    public void deleteAll() {
        LogUtils.i(TAG, "Delete all Matches.");
        Match.deleteAll(Match.class);
    }

    private void reload(Match entity) {
        LogUtils.i(TAG, "Reload data Matches.");
        if (entity != null) {
            reloadEntity(entity);
            if (entity.getGame() == null && entity.getGameId() != null) {
                final Game game = new GameRepository().find(entity.getGameId());
                entity.game(game);
            }
            if (entity.getPlayers() == null || entity.getPlayers().isEmpty()) {
                final List<Player> players = new PlayerRepository().findByMatchId(entity.getId());
                entity.players(players);
            }
        }
    }
}
