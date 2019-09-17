package br.com.battista.bgscore.helper

import android.support.design.widget.CollapsingToolbarLayout
import android.support.test.espresso.matcher.BoundedMatcher

import org.hamcrest.Description
import org.hamcrest.Matcher

object CustomViewMatcher {

    fun withCollapsibleToolbarTitle(textMatcher: Matcher<String>): Matcher<Any> {
        return object : BoundedMatcher<Any, CollapsingToolbarLayout>(CollapsingToolbarLayout::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("with toolbar title: ")
                textMatcher.describeTo(description)
            }

            override fun matchesSafely(toolbarLayout: CollapsingToolbarLayout): Boolean {
                return textMatcher.matches(toolbarLayout.title)
            }
        }
    }

}
