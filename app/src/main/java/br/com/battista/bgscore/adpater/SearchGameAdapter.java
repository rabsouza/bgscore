package br.com.battista.bgscore.adpater;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import java.text.MessageFormat;
import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.fragment.dialog.SearchGameDialog;
import br.com.battista.bgscore.model.response.GameResponse;
import br.com.battista.bgscore.util.AndroidUtils;


public class SearchGameAdapter extends BaseAdapterAnimation<SearchGameViewHolder> {
    private static final String TAG = SearchGameAdapter.class.getSimpleName();

    private Context context;
    private List<GameResponse> games;

    public SearchGameAdapter(Context context, List<GameResponse> games) {
        super(context);
        this.context = context;
        this.games = games;
    }

    @Override
    public SearchGameViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.adapter_search_game, viewGroup, false);
        return new SearchGameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchGameViewHolder holder, int position) {
        if (games != null && !games.isEmpty()) {
            final View itemView = holder.itemView;
            setAnimationHolder(itemView, position);

            final GameResponse game = games.get(position);
            Log.i(TAG, String.format(
                    "onBindViewHolder: Fill to row position: %S with %s.", position, game));

            holder.getTxtInfoName().setText(
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getName()), " - "));

            if (game.getYearPublished() != null) {
                holder.getTxtInfoYear().setText(game.getYearPublished());
            } else {
                holder.getTxtInfoYear().setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createDialogAddGame(game, itemView);
                }
            });
        } else {
            Log.w(TAG, "onBindViewHolder: No content to holder!");
        }

    }

    private void createDialogAddGame(final GameResponse game, final View viewContent) {
        String msgDelete = context.getResources().getString(R.string.alert_confirmation_dialog_text_add_game);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.alert_confirmation_dialog_title_delete)
                .setMessage(MessageFormat.format(msgDelete, game.getName()))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.btn_confirmation_dialog_add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        FragmentManager fm = ((FragmentActivity) viewContent.getContext()).getSupportFragmentManager();
                        Fragment prev = fm.findFragmentByTag(SearchGameDialog.DIALOG_SEARCH_GAME);
                        if (prev != null) {
                            DialogFragment df = (DialogFragment) prev;

                            final Intent intent = df.getActivity().getIntent();
                            intent.putExtra(BundleConstant.SEARCH_GAME_ID, game.getBoardgameId());
                            df.getTargetFragment().onActivityResult(df.getTargetRequestCode(),
                                    Activity.RESULT_OK, intent);

                            df.dismiss();
                        } else {
                            AndroidUtils.snackbar(viewContent, R.string.msg_error_add_search_game);
                        }
                    }
                })
                .setNegativeButton(R.string.btn_confirmation_dialog_cancel, null).create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.animationAlert;
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return games != null ? games.size() : 0;
    }
}
