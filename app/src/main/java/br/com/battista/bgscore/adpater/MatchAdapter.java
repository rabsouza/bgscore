package br.com.battista.bgscore.adpater;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.common.base.Strings;

import java.util.Calendar;
import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.DateUtils;
import br.com.battista.bgscore.util.ImageLoadUtils;
import br.com.battista.bgscore.util.PopupMenuUtils;


public class MatchAdapter extends BaseAdapterAnimation<MatchViewHolder> {
    private static final String TAG = MatchAdapter.class.getSimpleName();

    private Context context;
    private List<Match> matches;

    public MatchAdapter(Context context, List<Match> matches) {
        super(context);
        this.context = context;
        this.matches = matches;
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.adapter_match, viewGroup, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        if (matches != null && !matches.isEmpty()) {
            final View itemView = holder.itemView;
            setAnimationHolder(itemView, position);

            final Match match = matches.get(position);
            final Game game = match.getGame();
            itemView.setTag(match.getId());
            Log.i(TAG, String.format(
                    "onBindViewHolder: Fill to row position: %S with %s.", position, match));

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

            holder.getTxtInfoAlias().setText(match.getAlias());
            holder.getTxtInfoNameGame().setText(game.getName());
            final Calendar createdAt = Calendar.getInstance();
            createdAt.setTime(match.getCreatedAt());
            holder.getTxtInfoMatchDate().setText(DateUtils.format(createdAt));
            holder.getTxtInfoPlayers().setText(String.valueOf(match.getPlayers().size()));
            if (match.getDuration() == null) {
                holder.getTxtInfoDuration().setText("∞");
            } else {
                holder.getTxtInfoDuration().setText(String.valueOf(match.getDuration()));
            }
            if (match.isFinished()) {
                holder.getImgInfoFeedback().setImageResource(match.getFeedbackIdRes());
            } else {
                holder.getImgInfoFeedback().setVisibility(View.GONE);
            }

            ImageView imageMoreActions = holder.getImgMoreActions();
            final PopupMenu popup = new PopupMenu(itemView.getContext(), imageMoreActions);
            PopupMenuUtils.showPopupWindow(popup);
            popup.getMenuInflater().inflate(R.menu.menu_actions_match, popup.getMenu());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.show();
                }
            });

            final RecyclerView.Adapter adapterCurrent = this;
            final int positionRemoved = holder.getAdapterPosition();
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
                        case R.id.menu_action_finish:
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
        return matches != null ? matches.size() : 0;
    }
}
