package br.com.battista.bgscore.view;

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

public class Scoreboard extends LinearLayout {

    private TextView scoreView;
    private TextView labelView;

    public Scoreboard(Context context) {
        super(context);
    }

    public Scoreboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Scoreboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Scoreboard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        setOrientation(VERTICAL);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Scoreboard,
                0, 0);

        String value = null;
        String label = null;
        try {
            value = a.getString(R.styleable.Scoreboard_score);
            label = a.getString(R.styleable.Scoreboard_label);
        } finally {
            a.recycle();
        }

        scoreView = new TextView(getContext());

        scoreView.setText(value);
        setStyle(scoreView, R.style.CardHeadStyle);
        addView(scoreView);

        labelView = new TextView(getContext());
        labelView.setText(label);
        labelView.setGravity(Gravity.CENTER_HORIZONTAL);
        setStyle(labelView, R.style.CardTextStyle);
        addView(labelView);

        scoreView.setWidth(labelView.getMeasuredWidthAndState());
        scoreView.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    private void setStyle(TextView textView, int idResStyle) {
        if (Build.VERSION.SDK_INT < 23) {
            textView.setTextAppearance(getContext(), idResStyle);
        } else {
            textView.setTextAppearance(idResStyle);
        }
    }

}
