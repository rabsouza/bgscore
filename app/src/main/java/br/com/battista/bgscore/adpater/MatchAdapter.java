package br.com.battista.bgscore.adpater;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.common.base.Strings;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.MatchActivity;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.repository.MatchRepository;
import br.com.battista.bgscore.service.CacheManageService;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
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
                            processEditMatch(itemView, match);
                            break;
                        case R.id.menu_action_finish:
                            AndroidUtils.toast(itemView.getContext(), R.string.text_under_construction);
                            break;
                        case R.id.menu_action_remove:
                            createDialogRemoveMatch(match, positionRemoved, adapterCurrent, itemView);
                            break;
                    }

                    return false;
                }
            });

        } else {
            Log.w(TAG, "onBindViewHolder: No content to holder!");
        }

    }

    private void processEditMatch(View itemView, Match match) {
        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_EDIT_MATCH);

        Bundle args = new Bundle();
        Intent intent = new Intent(itemView.getContext(), MatchActivity.class);
        args.putSerializable(BundleConstant.DATA, match);
        intent.putExtras(args);

        itemView.getContext().startActivity(intent);
    }

    private void createDialogRemoveMatch(final Match match, final int position,
                                         final RecyclerView.Adapter adapterCurrent,
                                         final View itemView) {
        String msgDelete = context.getResources().getString(R.string.alert_confirmation_dialog_text_remove_match);
        new AlertDialog.Builder(context)
                .setTitle(R.string.alert_confirmation_dialog_title_delete)
                .setMessage(MessageFormat.format(msgDelete, match.getAlias()))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.btn_confirmation_dialog_remove, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d(TAG, "onClick: Success remove the match and refresh recyclerView!");

                        new MatchRepository().delete(match);
                        matches.remove(position);
                        adapterCurrent.notifyItemRemoved(position);
                        adapterCurrent.notifyDataSetChanged();

                        Log.i(TAG, "fillDataAndSave: Reload cache data.");
                        new CacheManageService().onActionCache(ActionCacheEnum.LOAD_DATA_MATCHES);

                        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_REMOVE_MATCH);
                    }
                })
                .setNegativeButton(R.string.btn_confirmation_dialog_cancel, null).show();
    }

    @Override
    public int getItemCount() {
        return matches != null ? matches.size() : 0;
    }
}
