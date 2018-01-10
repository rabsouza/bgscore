package br.com.battista.bgscore.fragment;

import static br.com.battista.bgscore.constants.CrashlyticsConstant.KEY_FRAGMENT;
import static br.com.battista.bgscore.constants.CrashlyticsConstant.KEY_OPEN_FRAGMENT;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;

import br.com.battista.bgscore.R;

public class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String nameView = this.getClass().getSimpleName();
        Answers.getInstance().logCustom(new CustomEvent(KEY_OPEN_FRAGMENT)
                .putCustomAttribute(KEY_FRAGMENT, nameView));

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(nameView)
                .putContentType(KEY_FRAGMENT));

        ImageButton btnSortList = getActivity().findViewById(R.id.btn_sort_list);
        if (btnSortList != null) {
            btnSortList.setVisibility(View.GONE);
        }

    }

    protected void replaceDetailFragment(Fragment fragment, int containerResID) {
        if (fragment != null) {
            Log.d(TAG, "replaceFragment: Change to detail fragment!");
            final FragmentTransaction transaction = getActivity().
                    getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            transaction.replace(containerResID, fragment);
            transaction.commit();
        }
    }
}
