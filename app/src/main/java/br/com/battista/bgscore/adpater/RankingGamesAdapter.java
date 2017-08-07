package br.com.battista.bgscore.adpater;

import com.google.common.base.Strings;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.dto.RankingGamesDto;
import br.com.battista.bgscore.util.DateUtils;
import br.com.battista.bgscore.util.ImageLoadUtils;
import br.com.battista.bgscore.util.RatingUtils;

import static br.com.battista.bgscore.constants.ViewConstant.SPACE_DRAWABLE;
import static com.google.common.base.MoreObjects.firstNonNull;


public class RankingGamesAdapter extends BaseAdapterAnimation<RankingGamesViewHolder> {
    private static final String TAG = RankingGamesAdapter.class.getSimpleName();

    private Context context;
    private List<RankingGamesDto> rankingGames;

    public RankingGamesAdapter(Context context, List<RankingGamesDto> rankingGames) {
        super(context);
        this.context = context;
        this.rankingGames = rankingGames;
    }

    @Override
    public RankingGamesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.adapter_ranking_games, viewGroup, false);
        return new RankingGamesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RankingGamesViewHolder holder, int position) {
        if (rankingGames != null && !rankingGames.isEmpty()) {
            final View itemView = holder.itemView;
            setAnimationHolder(itemView, position);

            final RankingGamesDto rankingGames = this.rankingGames.get(position);
            final Game game = rankingGames.getGame();
            Log.i(TAG, String.format(
                    "onBindViewHolder: Fill to row position: %S with %s.", position, rankingGames));

            String urlThumbnail = game.getUrlThumbnail();
            if (Strings.isNullOrEmpty(urlThumbnail)) {
                ImageLoadUtils.loadImage(itemView.getContext(),
                        R.drawable.boardgame_game,
                        holder.getImgInfoGame());
            } else {
                ImageLoadUtils.loadImage(itemView.getContext(),
                        urlThumbnail,
                        holder.getImgInfoGame());
            }

            holder.getTxtInfoName().setText(
                    firstNonNull(Strings.emptyToNull(game.getName()), "-"));

            holder.getImgMyGame().setVisibility(View.GONE);

            holder.getTxtInfoCountMatches()
                    .setText(SPACE_DRAWABLE + firstNonNull(rankingGames.getCount(), 0));

            holder.getTxtInfoSumTime()
                    .setText(SPACE_DRAWABLE +
                            DateUtils.formatTime(firstNonNull(rankingGames.getDuration(), 0L)));

            Calendar lastPlayed = Calendar.getInstance();
            lastPlayed.setTime(rankingGames.getLastPlayed());
            holder.getTxtInfoLastPlayed().setText(SPACE_DRAWABLE + DateUtils.format(lastPlayed));

            if (Strings.isNullOrEmpty(game.getRating())) {
                holder.getRtbInfoRating().setRating(0F);
            } else {
                holder.getRtbInfoRating().setRating(RatingUtils.convertFrom(game.getRating()));
            }

        } else {
            Log.w(TAG, "onBindViewHolder: No content to holder!");
        }

    }

    @Override
    public int getItemCount() {
        return rankingGames != null ? rankingGames.size() : 0;
    }
}
