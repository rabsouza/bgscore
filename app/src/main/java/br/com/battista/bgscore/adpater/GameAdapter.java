package br.com.battista.bgscore.adpater;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.util.ImageLoadUtils;
import br.com.battista.bgscore.util.PopupMenuUtils;


public class GameAdapter extends BaseAdapterAnimation<GameViewHolder> {
    private static final String TAG = GameAdapter.class.getSimpleName();

    private Context context;
    private List<Game> games;

    public GameAdapter(Context context, List<Game> games) {
        super(context);
        this.context = context;
        this.games = games;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.adapter_game, viewGroup, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        if (games != null && !games.isEmpty()) {
            View itemView = holder.itemView;
            setAnimationHolder(itemView, position);

            Game game = games.get(position);
            Log.i(TAG, String.format(
                    "onBindViewHolder: Fill to row position: %S with %s.", position, game));

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

            holder.getTxtInfoName().setText(MessageFormat.format("{0} ({1})",
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getName()), "-"),
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getAge()), "-")));

            holder.getTxtInfoPlayers().setText(MessageFormat.format("{0}/{1} joga.",
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayers()), "1"),
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMaxPlayers()), "*")));

            holder.getTxtInfoTime().setText(MessageFormat.format("{0}-{1} mins",
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayTime()), "*"),
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMaxPlayTime()), "∞")));

            holder.getTxtInfoAges().setText(MessageFormat.format("{0}+ anos",
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayTime()), "*")));

            if (Strings.isNullOrEmpty(game.getRating())) {
                holder.getRtbInfoRating().setRating(0F);
            } else {
                holder.getRtbInfoRating().setRating(Float.parseFloat(game.getRating()));
            }

            ImageView imageMoreActions = holder.getImgMoreActions();
            final PopupMenu popup = new PopupMenu(itemView.getContext(), imageMoreActions);
            PopupMenuUtils.showPopupWindow(popup);
            popup.getMenuInflater().inflate(R.menu.menu_actions_game, popup.getMenu());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.show();
                }
            });

        } else {
            Log.w(TAG, "onBindViewHolder: No content to holder!");
        }

    }

    @Override
    public int getItemCount() {
        return games != null ? games.size() : 0;
    }
}
