package br.com.battista.bgscore.adpater;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.GameActivity;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.repository.MatchRepository;
import br.com.battista.bgscore.service.CacheManageService;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
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

            final Game game = games.get(position);
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

            holder.getTxtInfoName().setText(
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getName()), "-"));

            holder.getTxtInfoYear().setText(
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getYearPublished()), "****"));

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

            final RecyclerView.Adapter adapterCurrent = this;
            final int positionRemoved = holder.getAdapterPosition();
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_action_detail:
                            openDetailInBrowser(itemView, game);
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
            Log.w(TAG, "onBindViewHolder: No content to holder!");
        }

    }

    private void openDetailInBrowser(View itemView, Game game) {
        if (!Strings.isNullOrEmpty(game.getUrlInfo())) {
            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_OPEN_INFO_GAME);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(game.getUrlInfo()));
            itemView.getContext().startActivity(intent);
        } else {
            AndroidUtils.snackbar(itemView, itemView.getContext().getText(R.string.msg_game_dont_found).toString());
        }
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
        new AlertDialog.Builder(context)
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

                        Log.d(TAG, "onClick: Success remove the game and refresh recyclerView!");

                        new GameRepository().delete(game);
                        games.remove(position);
                        adapterCurrent.notifyItemRemoved(position);
                        adapterCurrent.notifyDataSetChanged();

                        Log.i(TAG, "fillDataAndSave: Reload cache data.");
                        new CacheManageService().onActionCache(ActionCacheEnum.LOAD_DATA_GAME);

                        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_REMOVE_GAME);
                    }
                })
                .setNegativeButton(R.string.btn_confirmation_dialog_cancel, null).show();
    }

    @Override
    public int getItemCount() {
        return games != null ? games.size() : 0;
    }
}
