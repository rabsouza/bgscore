package br.com.battista.bgscore.helper

import android.support.design.internal.BottomNavigationItemView
import android.support.design.widget.CollapsingToolbarLayout
import android.support.test.espresso.matcher.BoundedMatcher
import android.view.View
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

    fun withBottomBarItemCheckedStatus(isChecked: Boolean): Matcher<View> {
        return object : BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView::class.java) {
            var triedMatching: Boolean = false

            override fun describeTo(description: Description) {
                if (triedMatching) {
                    description.appendText("with BottomNavigationItem check status: $isChecked")
                    description.appendText("But was: " + (!isChecked).toString())
                }
            }

            override fun matchesSafely(item: BottomNavigationItemView): Boolean {
                triedMatching = true
                return item.itemData.isChecked == isChecked
            }
        }
    }

}
