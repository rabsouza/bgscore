package br.com.battista.bgscore.helper;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class CustomViewMatcher {

    public static Matcher<Object> withCollapsibleToolbarTitle(final Matcher<String> textMatcher) {
        return new BoundedMatcher<Object, CollapsingToolbarLayout>(CollapsingToolbarLayout.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(CollapsingToolbarLayout toolbarLayout) {
                return textMatcher.matches(toolbarLayout.getTitle());
            }
        };
    }

}
