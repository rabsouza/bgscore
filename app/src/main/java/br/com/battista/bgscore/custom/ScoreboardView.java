package br.com.battista.bgscore.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.battista.bgscore.R;

public class ScoreboardView extends LinearLayout {

    public static final String TAG_SCORE_VALUE = "SCORE_VALUE";
    public static final String TAG_SCORE_LABEL = "SCORE_LABEL";

    private TextView scoreView;
    private TextView labelView;
    private int iconView;

    public ScoreboardView(Context context) {
        super(context);
    }

    public ScoreboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ScoreboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScoreboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        setOrientation(VERTICAL);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ScoreboardView,
                0, 0);

        String value = null;
        String label = null;
        try {
            value = a.getString(R.styleable.ScoreboardView_value_score);
            label = a.getString(R.styleable.ScoreboardView_label_score);
            iconView = a.getResourceId(R.styleable.ScoreboardView_icon_score, 0);
        } finally {
            a.recycle();
        }

        scoreView = new TextView(getContext());

        scoreView.setTag(TAG_SCORE_VALUE);
        scoreView.setText(value);
        setStyle(scoreView, R.style.ScoreValueStyle);
        addView(scoreView);

        labelView = new TextView(getContext());
        labelView.setTag(TAG_SCORE_LABEL);
        labelView.setText(label);
        labelView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        if (iconView != 0) {
            labelView.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.drawable_padding));
            labelView.setCompoundDrawablesWithIntrinsicBounds(iconView, 0, 0, 0);
        }
        setStyle(labelView, R.style.CardHeadStyle);
        addView(labelView);

        scoreView.setWidth(labelView.getMeasuredWidthAndState());
        scoreView.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void setScoreText(String text) {
        scoreView.setText(text);
    }

    public void setLabelText(String text) {
        labelView.setText(text);
    }

    private void setStyle(TextView textView, int idResStyle) {
        if (Build.VERSION.SDK_INT < 23) {
            textView.setTextAppearance(getContext(), idResStyle);
        } else {
            textView.setTextAppearance(idResStyle);
        }
    }

}
