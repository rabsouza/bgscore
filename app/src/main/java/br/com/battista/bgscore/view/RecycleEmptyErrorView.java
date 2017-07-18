package br.com.battista.bgscore.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RecycleEmptyErrorView extends RecyclerView {

    private View emptyView;
    private View errorView;
    private boolean error;
    private int visibility;

    private final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            updateEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            updateEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            updateEmptyView();
        }
    };

    public RecycleEmptyErrorView(Context context) {
        super(context);
        visibility = getVisibility();
    }

    public RecycleEmptyErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        visibility = getVisibility();
    }

    public RecycleEmptyErrorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        visibility = getVisibility();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        updateEmptyView();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        this.visibility = visibility;
        updateErrorView();
        updateEmptyView();
    }

    private void updateEmptyView() {
        if (emptyView != null && getAdapter() != null) {
            boolean isShowEmptyView = getAdapter().getItemCount() == 0;
            emptyView.setVisibility(isShowEmptyView && !shouldShowErrorView()
                    && visibility == VISIBLE ? VISIBLE : GONE);
            super.setVisibility(!isShowEmptyView && !shouldShowErrorView()
                    && visibility == VISIBLE ? VISIBLE : GONE);
        }
    }

    private void updateErrorView() {
        if (errorView != null) {
            errorView.setVisibility(shouldShowErrorView()
                    && visibility == VISIBLE ? VISIBLE : GONE);
        }
    }

    private boolean shouldShowErrorView() {
        return errorView != null && error;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        updateEmptyView();
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
        updateErrorView();
        updateEmptyView();
    }

    public void showErrorView() {
        error = true;
        updateErrorView();
        updateEmptyView();
    }

    public void hideErrorView() {
        error = false;
        updateErrorView();
        updateEmptyView();
    }
}
