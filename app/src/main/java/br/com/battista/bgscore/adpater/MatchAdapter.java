package br.com.battista.bgscore.adpater;

import static br.com.battista.bgscore.constants.BundleConstant.NAVIGATION_TO;
import static br.com.battista.bgscore.constants.BundleConstant.NavigationTo.DETAIL_MATCH_FRAGMENT;
import static br.com.battista.bgscore.constants.BundleConstant.NavigationTo.FINISH_MATCH_FRAGMENT;
import static br.com.battista.bgscore.constants.BundleConstant.NavigationTo.NEW_MATCH_FRAGMENT;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.common.base.Strings;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.MatchActivity;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.fragment.dialog.ShareMatchDialog;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.Player;
import br.com.battista.bgscore.model.dto.StatisticDto;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.repository.MatchRepository;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.DateUtils;
import br.com.battista.bgscore.util.ImageLoadUtils;
import br.com.battista.bgscore.util.LogUtils;
import br.com.battista.bgscore.util.PopupUtils;


public class MatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = MatchAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<Match> matches;

    public MatchAdapter(Context context, List<Match> matches) {
        this.context = context;
        this.matches = matches;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (TYPE_HEADER == viewType) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.adapter_match_header, viewGroup, false);
            return new MatchViewHeaderHolder(view);
        } else {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.adapter_match_item, viewGroup, false);
            return new MatchViewItemHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MatchViewHeaderHolder) {
            configItemHolder((MatchViewHeaderHolder) holder);
        } else {
            configItemHolder((MatchViewItemHolder) holder, position - 1);
        }
    }

    private void configItemHolder(MatchViewHeaderHolder holder) {
        LogUtils.i(TAG, "configItemHolder: configure help game button!");
        holder.getImgHelpMatch().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                        CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_LEGEND_MATCH);

                View customView = LayoutInflater.from(context).inflate(R.layout.dialog_help_match, null);

                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle(R.string.title_help)
                        .setView(customView)
                        .setPositiveButton(R.string.btn_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.animationAlert;
                alertDialog.show();
            }
        });

        LogUtils.i(TAG, "updateScoresMatches: Update the scores to matches!");
        final StatisticDto statistic = MainApplication.instance().getUser().getStatisticDto();
        DecimalFormat decimalFormatScore = new DecimalFormat("#00");
        holder.getScoreMatchVeryDissatisfied().setScoreText(decimalFormatScore.format(statistic.getCountMatchVeryDissatisfied()));
        holder.getScoreMatchDissatisfied().setScoreText(decimalFormatScore.format(statistic.getCountMatchDissatisfied()));
        holder.getScoreMatchNeutral().setScoreText(decimalFormatScore.format(statistic.getCountMatchNeutral()));
        holder.getScoreMatchSatisfied().setScoreText(decimalFormatScore.format(statistic.getCountMatchSatisfied()));
        holder.getScoreMatchVerySatisfied().setScoreText(decimalFormatScore.format(statistic.getCountMatchVerySatisfied()));

    }

    private void configItemHolder(MatchViewItemHolder holder, int position) {
        MatchViewItemHolder rowHolder = holder;
        if (matches != null && !matches.isEmpty()) {
            final View itemView = rowHolder.itemView;

            final Match match = matches.get(position);
            final Game game = match.getGame();
            itemView.setTag(match.getId());
            LogUtils.i(TAG, String.format(
                    "onBindViewHolder: Fill to row position: %S with %s.", position, match.getAlias()));

            String urlThumbnail = game.getUrlThumbnail();
            if (Strings.isNullOrEmpty(urlThumbnail)) {
                ImageLoadUtils.loadImage(itemView.getContext(),
                        R.drawable.boardgame_game,
                        rowHolder.getImgInfoGame());
            } else {
                ImageLoadUtils.loadImage(itemView.getContext(),
                        urlThumbnail,
                        rowHolder.getImgInfoGame());
            }

            rowHolder.getTxtInfoAlias().setText(match.getAlias());
            rowHolder.getTxtInfoNameGame().setText(game.getName());

            final Calendar createdAt = Calendar.getInstance();
            createdAt.setTime(match.getCreatedAt());
            rowHolder.getTxtInfoMatchDate().setText(DateUtils.format(createdAt));
            rowHolder.getTxtInfoPlayers().setText("0" + match.getPlayers().size());
            if (match.getDuration() == null) {
                rowHolder.getTxtInfoDuration().setText("00:00");
            } else {
                rowHolder.getTxtInfoDuration().setText(
                        DateUtils.formatTime(match.getDuration()));
            }
            if (match.isFinished()) {
                rowHolder.getImgInfoFeedback().setImageResource(match.getFeedback().getIdResDrawable());
                switch (match.getFeedback().getIdResDrawable()) {
                    case R.drawable.ic_feedback_very_dissatisfied:
                    case R.drawable.ic_feedback_dissatisfied:
                        rowHolder.getImgInfoFeedback().setColorFilter(
                                ContextCompat.getColor(itemView.getContext(), R.color.colorImgFeedbackDissatisfied));
                        break;
                    case R.drawable.ic_feedback_neutral:
                        rowHolder.getImgInfoFeedback().setColorFilter(
                                ContextCompat.getColor(itemView.getContext(), R.color.colorImgFeedbackNeutral));
                        break;
                    case R.drawable.ic_feedback_satisfied:
                    case R.drawable.ic_feedback_very_satisfied:
                        rowHolder.getImgInfoFeedback().setColorFilter(
                                ContextCompat.getColor(itemView.getContext(), R.color.colorImgFeedbackSatisfied));
                        break;
                }
                rowHolder.getImgInfoAlarm().setVisibility(View.GONE);
            } else {
                if (match.isScheduleMatch()) {
                    rowHolder.getImgInfoAlarm().setVisibility(View.VISIBLE);
                } else {
                    rowHolder.getImgInfoAlarm().setVisibility(View.GONE);
                }
                rowHolder.getImgInfoFeedback().setVisibility(View.GONE);
            }

            ImageView imageMoreActions = rowHolder.getImgMoreActions();
            final PopupMenu popup = new PopupMenu(itemView.getContext(), imageMoreActions);
            PopupUtils.showPopupWindow(popup);
            popup.getMenuInflater().inflate(R.menu.menu_actions_match, popup.getMenu());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.show();
                }
            });

            final RecyclerView.Adapter adapterCurrent = this;
            final int positionRemoved = rowHolder.getAdapterPosition();
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_action_copy:
                            processCopyMatch(itemView, match);
                            break;
                        case R.id.menu_action_detail:
                            processDetailMatch(itemView, match);
                            break;
                        case R.id.menu_action_share:
                            processShareMatch(itemView, match);
                            break;
                        case R.id.menu_action_edit:
                            processEditMatch(itemView, match);
                            break;
                        case R.id.menu_action_finish:
                            processFinishMatch(itemView, match);
                            break;
                        case R.id.menu_action_remove:
                            createDialogRemoveMatch(match, positionRemoved, adapterCurrent, itemView);
                            break;
                    }

                    return false;
                }
            });

        } else {
            LogUtils.w(TAG, "onBindViewHolder: No content to rowHolder!");
        }

    }

    private void processCopyMatch(View itemView, Match match) {
        LogUtils.i(TAG, "processCopyMatch: Copy the match!");
        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_COPY_MATCH);

        Match matchCopy = new Match();
        matchCopy.initEntity();
        matchCopy.alias(match.getAlias());
        matchCopy.createdAt(match.getCreatedAt());
        matchCopy.game(match.getGame());

        for (Player player : match.getPlayers()) {
            Player newPlayer = new Player()
                    .name(player.getName())
                    .typePlayer(player.getTypePlayer());
            newPlayer.initEntity();
            matchCopy.addPlayer(newPlayer);
        }

        Bundle args = new Bundle();
        Intent intent = new Intent(itemView.getContext(), MatchActivity.class);
        args.putSerializable(BundleConstant.DATA, matchCopy);
        intent.putExtras(args);

        itemView.getContext().startActivity(intent);
    }

    private void processShareMatch(View itemView, Match match) {
        LogUtils.i(TAG, "processShareMatch: Share the match!");
        final FragmentManager supportFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        new ShareMatchDialog().newInstance(match).showDialog(supportFragmentManager.findFragmentById(R.id.container));
    }

    private void processDetailMatch(View itemView, Match match) {
        LogUtils.i(TAG, "processDetailMatch: Detail the macth!");
        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_DETAIL_MATCH);

        Bundle args = new Bundle();
        Intent intent = new Intent(itemView.getContext(), MatchActivity.class);
        args.putSerializable(BundleConstant.DATA, match);
        args.putInt(NAVIGATION_TO, DETAIL_MATCH_FRAGMENT);
        intent.putExtras(args);

        itemView.getContext().startActivity(intent);
    }

    private void processFinishMatch(View itemView, Match match) {
        LogUtils.i(TAG, "processFinishMatch: Finish the match!");
        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_FINISH_MATCH);

        Bundle args = new Bundle();
        Intent intent = new Intent(itemView.getContext(), MatchActivity.class);
        args.putSerializable(BundleConstant.DATA, match);
        args.putInt(NAVIGATION_TO, FINISH_MATCH_FRAGMENT);
        intent.putExtras(args);

        itemView.getContext().startActivity(intent);
    }

    private void processEditMatch(View itemView, Match match) {
        LogUtils.i(TAG, "processEditMatch: Edit the match!");
        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_EDIT_MATCH);

        Bundle args = new Bundle();
        Intent intent = new Intent(itemView.getContext(), MatchActivity.class);
        args.putSerializable(BundleConstant.DATA, match);
        args.putInt(NAVIGATION_TO, NEW_MATCH_FRAGMENT);
        intent.putExtras(args);

        itemView.getContext().startActivity(intent);
    }

    private void createDialogRemoveMatch(final Match match, final int position,
                                         final RecyclerView.Adapter adapterCurrent,
                                         final View itemView) {
        String msgDelete = context.getResources().getString(R.string.alert_confirmation_dialog_text_remove_match);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.alert_confirmation_dialog_title_delete)
                .setMessage(MessageFormat.format(msgDelete, match.getAlias()))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.btn_confirmation_dialog_remove, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        LogUtils.d(TAG, "onClick: Success remove the match and refresh recyclerView!");

                        new MatchRepository().delete(match);
                        matches.remove(position - 1);
                        adapterCurrent.notifyItemRemoved(position);
                        adapterCurrent.notifyDataSetChanged();

                        LogUtils.i(TAG, "fillDataAndSave: Reload cache data.");
                        AndroidUtils.postAction(ActionCacheEnum.LOAD_DATA_MATCHES);

                        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_REMOVE_MATCH);
                    }
                })
                .setNegativeButton(R.string.btn_confirmation_dialog_cancel, null).create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.animationAlert;
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return matches != null ? matches.size() + 1 : 0;
    }


    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }
}
