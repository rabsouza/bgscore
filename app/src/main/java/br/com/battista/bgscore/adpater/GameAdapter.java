package br.com.battista.bgscore.adpater;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.GameActivity;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.dto.StatisticDto;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.model.enuns.BadgeGameEnum;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.repository.MatchRepository;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.ImageLoadUtils;
import br.com.battista.bgscore.util.LogUtils;
import br.com.battista.bgscore.util.PopupUtils;

public class GameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = GameAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<Game> games;

    public GameAdapter(Context context, List<Game> games) {
        this.context = context;
        this.games = games;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (TYPE_HEADER == viewType) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.adapter_game_header, viewGroup, false);
            return new GameViewHeaderHolder(view);
        } else {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.adapter_game_item, viewGroup, false);
            return new GameViewItemHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GameViewHeaderHolder) {
            configItemHolder((GameViewHeaderHolder) holder);
        } else {
            configItemHolder((GameViewItemHolder) holder, position - 1);
        }
    }

    private void configItemHolder(GameViewHeaderHolder holder) {
        LogUtils.i(TAG, "configItemHolder: configure help game button!");
        holder.getImgHelpGame().setOnClickListener(view -> {
            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_LEGEND_GAME);

            View customView = LayoutInflater.from(context).inflate(R.layout.dialog_help_game, null);

            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle(R.string.title_help)
                    .setView(customView)
                    .setPositiveButton(R.string.btn_ok,
                            (dialog, whichButton) -> dialog.dismiss()
                    )
                    .create();
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.animationAlert;
            alertDialog.show();
        });

        LogUtils.i(TAG, "configItemHolder: Update the scores to games!");
        final StatisticDto statistic = MainApplication.instance().getUser().getStatisticDto();
        DecimalFormat decimalFormatScore = new DecimalFormat("#00");
        holder.getScoreMyGame().setScoreText(decimalFormatScore.format(statistic.getCountGameMyGame()));
        holder.getScoreFavorite().setScoreText(decimalFormatScore.format(statistic.getCountGameFavorite()));
        holder.getScoreWantGame().setScoreText(decimalFormatScore.format(statistic.getCountGameWantGame()));

    }

    private void configItemHolder(GameViewItemHolder holder, int position) {
        GameViewItemHolder rowHolder = holder;
        if (games != null && !games.isEmpty()) {

            final View itemView = rowHolder.itemView;
            final Game game = games.get(position);
            itemView.setTag(game.getId());
            LogUtils.i(TAG, String.format(
                    "onBindViewHolder: Fill to rowHolder position: %S with %s.", position, game.getName()));

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

            if (BadgeGameEnum.BADGE_GAME_NONE.equals(game.getBadgeGame())) {
                rowHolder.getImgInfoBadgeGame().setVisibility(View.GONE);
            } else {
                rowHolder.getImgInfoBadgeGame().setImageResource(
                        game.getBadgeGame().getIdResDrawable());
            }

            rowHolder.getTxtInfoName().setText(
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getName()), "-"));

            rowHolder.getTxtInfoYear().setText(
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getYearPublished()), "****"));

            if (Strings.isNullOrEmpty(game.getMaxPlayers())) {
                rowHolder.getTxtInfoPlayers().setText(MessageFormat.format("{0}",
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayers()), "1")));
            } else {
                rowHolder.getTxtInfoPlayers().setText(MessageFormat.format("{0}-{1}",
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayers()), "1"),
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMaxPlayers()), "*")));
            }

            if (Strings.isNullOrEmpty(game.getMaxPlayTime())) {
                rowHolder.getTxtInfoTime().setText(MessageFormat.format("{0}´",
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayTime()), "∞")));
            } else {
                rowHolder.getTxtInfoTime().setText(MessageFormat.format("{0}-{1}´",
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayTime()), "*"),
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMaxPlayTime()), "∞")));
            }

            if (Strings.isNullOrEmpty(game.getAge())) {
                rowHolder.getTxtInfoAges().setText("-");
            } else {
                rowHolder.getTxtInfoAges().setText(MessageFormat.format("{0}+", game.getAge()));
            }

            if (game.getRating() == null) {
                rowHolder.getRtbInfoRating().setRating(0F);
            } else {
                rowHolder.getRtbInfoRating().setRating(game.getRating());
            }

            if (game.isMyGame()) {
                rowHolder.getImgMyGame().setVisibility(View.VISIBLE);
            } else {
                rowHolder.getImgMyGame().setVisibility(View.GONE);

                if (game.isFavorite()) {
                    RelativeLayout.LayoutParams layoutParams =
                            (RelativeLayout.LayoutParams) rowHolder.getImgFavorite().getLayoutParams();
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                } else if (game.isWantGame()) {
                    RelativeLayout.LayoutParams layoutParams =
                            (RelativeLayout.LayoutParams) rowHolder.getImgWantGame().getLayoutParams();
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
            }

            if (game.isFavorite()) {
                rowHolder.getImgFavorite().setVisibility(View.VISIBLE);
            } else {
                rowHolder.getImgFavorite().setVisibility(View.GONE);
            }

            if (game.isWantGame()) {
                rowHolder.getImgWantGame().setVisibility(View.VISIBLE);
            } else {
                rowHolder.getImgWantGame().setVisibility(View.GONE);
            }

            ImageView imageMoreActions = rowHolder.getImgMoreActions();
            final PopupMenu popup = new PopupMenu(itemView.getContext(), imageMoreActions);
            PopupUtils.showPopupWindow(popup);
            popup.getMenuInflater().inflate(R.menu.menu_actions_game, popup.getMenu());
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
                            processCopyGame(itemView, game);
                            break;
                        case R.id.menu_action_detail:
                            openDetailInBrowser(itemView, game);
                            break;
                        case R.id.menu_action_buy:
                            openBuyInBrowser(itemView, game);
                            break;
                        case R.id.menu_action_edit:
                            processEditGame(itemView, game);
                            break;
                        case R.id.menu_action_remove:
                            createDialogRemoveGame(game, positionRemoved, adapterCurrent, itemView);
                            break;
                    }

                    return false;
                }
            });

        } else {
            LogUtils.w(TAG, "onBindViewHolder: No content to rowHolder!");
        }
    }

    private void openBuyInBrowser(View itemView, Game game) {
        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_BUY_GAME);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(game.getUrlBuy()));
        itemView.getContext().startActivity(intent);
    }

    private void openDetailInBrowser(View itemView, Game game) {
        if (!Strings.isNullOrEmpty(game.getUrlInfo())) {
            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_OPEN_INFO_GAME);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(game.getUrlInfo()));
            itemView.getContext().startActivity(intent);
        } else {
            AndroidUtils.snackbar(itemView, itemView.getContext().getText(R.string.msg_game_dont_found_open).toString());
        }
    }

    private void processCopyGame(View itemView, Game game) {
        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_COPY_GAME);

        Game newGame = new Game();
        newGame.initEntity();
        newGame.name(game.getName());
        newGame.myGame(game.isMyGame());
        newGame.urlThumbnail(game.getUrlThumbnail());
        newGame.urlImage(game.getUrlImage());
        newGame.urlInfo(game.getUrlInfo());
        newGame.yearPublished(game.getYearPublished());
        newGame.minPlayers(game.getMinPlayers());
        newGame.maxPlayers(game.getMaxPlayers());
        newGame.minPlayTime(game.getMinPlayTime());
        newGame.maxPlayTime(game.getMaxPlayTime());
        newGame.age(game.getAge());

        Bundle args = new Bundle();
        Intent intent = new Intent(itemView.getContext(), GameActivity.class);
        args.putSerializable(BundleConstant.DATA, newGame);
        intent.putExtras(args);

        itemView.getContext().startActivity(intent);
    }

    private void processEditGame(View itemView, Game game) {
        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_EDIT_GAME);

        Bundle args = new Bundle();
        Intent intent = new Intent(itemView.getContext(), GameActivity.class);
        args.putSerializable(BundleConstant.DATA, game);
        intent.putExtras(args);

        itemView.getContext().startActivity(intent);
    }

    private void createDialogRemoveGame(final Game game, final int position,
                                        final RecyclerView.Adapter adapterCurrent,
                                        final View itemView) {
        String msgDelete = context.getResources().getString(R.string.alert_confirmation_dialog_text_remove_game);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.alert_confirmation_dialog_title_delete)
                .setMessage(MessageFormat.format(msgDelete, game.getName()))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.btn_confirmation_dialog_remove, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        List<Match> matches = new MatchRepository().findByGameId(game.getId());
                        if (matches != null && !matches.isEmpty()) {
                            final String msgError = itemView.getContext().getString(R.string.msg_error_game_with_partner, game.getName());
                            AndroidUtils.snackbar(itemView, msgError);
                            return;
                        }

                        LogUtils.d(TAG, "onClick: Success remove the game and refresh recyclerView!");

                        new GameRepository().delete(game);
                        games.remove(position - 1);
                        adapterCurrent.notifyItemRemoved(position);
                        adapterCurrent.notifyDataSetChanged();

                        LogUtils.i(TAG, "fillDataAndSave: Reload cache data.");
                        AndroidUtils.postAction(ActionCacheEnum.LOAD_DATA_GAME);

                        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_REMOVE_GAME);
                    }
                })
                .setNegativeButton(R.string.btn_confirmation_dialog_cancel, null).create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.animationAlert;
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return games != null ? games.size() + 1 : 0;
    }


    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }
}
