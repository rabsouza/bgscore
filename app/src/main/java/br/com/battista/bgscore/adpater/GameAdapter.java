package br.com.battista.bgscore.adpater;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.ImageLoadUtils;
import br.com.battista.bgscore.util.PopupMenuUtils;
import br.com.battista.bgscore.util.RatingUtils;


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
            final View itemView = holder.itemView;
            setAnimationHolder(itemView, position);

            Game game = games.get(position);
            itemView.setTag(game.getId());
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
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getYearPublished()), "*")));

            if (Strings.isNullOrEmpty(game.getMaxPlayers())) {
                holder.getTxtInfoPlayers().setText(MessageFormat.format("{0}",
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayers()), "1")));
            } else {
                holder.getTxtInfoPlayers().setText(MessageFormat.format("{0}-{1}",
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayers()), "1"),
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMaxPlayers()), "*")));
            }

            if (Strings.isNullOrEmpty(game.getMaxPlayTime())) {
                holder.getTxtInfoTime().setText(MessageFormat.format("{0}´",
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayTime()), "∞")));
            } else {
                holder.getTxtInfoTime().setText(MessageFormat.format("{0}-{1}´",
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayTime()), "*"),
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMaxPlayTime()), "∞")));
            }

            if (Strings.isNullOrEmpty(game.getAge())) {
                holder.getTxtInfoAges().setText("-");
            } else {
                holder.getTxtInfoAges().setText(MessageFormat.format("{0}+", game.getAge()));
            }

            if (Strings.isNullOrEmpty(game.getRating())) {
                holder.getRtbInfoRating().setRating(0F);
            } else {
                holder.getRtbInfoRating().setRating(RatingUtils.convertFrom(game.getRating()));
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

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_action_detail:
                            AndroidUtils.toast(itemView.getContext(), R.string.text_under_construction);
                            break;
                        case R.id.menu_action_edit:
                            AndroidUtils.toast(itemView.getContext(), R.string.text_under_construction);
                            break;
                        case R.id.menu_action_remove:
                            AndroidUtils.toast(itemView.getContext(), R.string.text_under_construction);
                            break;
                    }

                    return false;
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
