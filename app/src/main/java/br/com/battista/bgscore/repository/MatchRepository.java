package br.com.battista.bgscore.repository;


import android.util.Log;

import com.orm.query.Select;

import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.Player;
import br.com.battista.bgscore.repository.contract.DatabaseContract.BaseEntry;
import br.com.battista.bgscore.repository.contract.DatabaseContract.MatchEntry;

public class MatchRepository extends BaseRepository implements Repository<Match> {

    public static final String TAG = MatchRepository.class.getSimpleName();

    @Override
    public void save(Match entity) {
        if (entity != null) {
            Log.i(TAG, MessageFormat.format("Save to Match with alias: {0}.", entity.getAlias()));
            saveEntity(entity);
            for (Player player : entity.getPlayers()) {
                player.matchId(entity.getId());
                new PlayerRepository().save(player);
            }
        } else {
            Log.w(TAG, "Entity can not be null!");
        }
    }

    @Override
    public void saveAll(List<Match> entities) {
        if (entities != null) {
            Log.i(TAG, MessageFormat.format("Save {0} Matches.", entities.size()));
            for (Match entity : entities) {
                if (entity != null) {
                    Log.i(TAG, MessageFormat.format("Save to Match with key: {0}.", entity.getId()));
                    saveEntity(entity);
                }
            }
        } else {
            Log.w(TAG, "Entities can not be null!");
        }
    }

    public Match find(Long id) {
        Log.i(TAG, MessageFormat.format("Find the Match by key: {0}.", id));
        final Match match = Match.findById(Match.class, id);
        reload(match);
        return match;
    }

    @Override
    public void delete(Match entity) {
        if (entity != null) {
            entity.reloadId();
            Log.i(TAG, MessageFormat.format("Delete to Match with id: {0}.", entity.getId()));
            Match.deleteAll(entity.getClass(),
                    MessageFormat.format("{0} = ?", BaseEntry.COLUMN_NAME_ID),
                    String.valueOf(entity.getId()));
        } else {
            Log.w(TAG, "Entity can not be null!");
        }
    }

    @Override
    public List<Match> findAll() {
        Log.i(TAG, "Find all Matches.");
        final List<Match> matches = Select
                .from(Match.class)
                .orderBy(MessageFormat.format("{0} DESC, {1} ASC",
                        BaseEntry.COLUMN_NAME_UPDATED_AT, MatchEntry.COLUMN_NAME_ALIAS))
                .list();
        if (matches != null) {
            for (Match match : matches) {
                reload(match);
            }
        }
        return matches;
    }

    public List<Match> findByGameId(Long idGame) {
        Log.i(TAG, MessageFormat.format("Find Matches by Id Game: {0}.", idGame));
        final List<Match> matches = Select
                .from(Match.class)
                .where(MessageFormat.format("{0} = ?", MatchEntry.FK_GAME_ID),
                        new String[]{String.valueOf(idGame)})
                .orderBy(MessageFormat.format("{0} DESC, {1} ASC",
                        BaseEntry.COLUMN_NAME_UPDATED_AT, MatchEntry.COLUMN_NAME_ALIAS))
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
        Log.i(TAG, "Delete all Matches.");
        Match.deleteAll(Match.class);
    }

    private void reload(Match entity) {
        Log.i(TAG, "Reload data Matches.");
        if (entity != null) {
            entity.reloadId();
            if (entity.getGame() == null && entity.getGameId() != null) {
                final Game game = new GameRepository().find(entity.getGameId());
                entity.game(game);
            }
            if (entity.getPlayers() == null || entity.getPlayers().isEmpty()) {
                final List<Player> players = new PlayerRepository().findByMatchId(entity.getId());
                entity.players(players);
            }
            reloadEntity(entity);
        }
    }
}
