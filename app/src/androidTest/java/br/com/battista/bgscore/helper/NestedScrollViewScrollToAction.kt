package br.com.battista.bgscore.helper

import android.graphics.Rect
import android.support.design.widget.CoordinatorLayout
import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.util.HumanReadables
import android.support.v4.widget.NestedScrollView
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ListView
import android.widget.ScrollView
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf

/**
 * Enables scrolling to the given view. View must be a descendant of a ScrollView.
 */
class NestedScrollViewScrollToAction : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return allOf(withEffectiveVisibility(Visibility.VISIBLE), isDescendantOfA(anyOf(
                isAssignableFrom(NestedScrollView::class.java),
                isAssignableFrom(ScrollView::class.java),
                isAssignableFrom(HorizontalScrollView::class.java),
                isAssignableFrom(ListView::class.java))))
    }

    override fun perform(uiController: UiController, view: View) {
        if (isDisplayingAtLeast(95).matches(view)) {
            Log.i(TAG, "View is already displayed. Returning.")
            return
        }

        val parentScrollView = findScrollView(view)

        val params = parentScrollView.layoutParams as CoordinatorLayout.LayoutParams
        params.behavior = null
        parentScrollView.requestLayout()

        uiController.loopMainThreadUntilIdle()

        val rect = Rect()
        view.getDrawingRect(rect)
        if (!/* immediate */view.requestRectangleOnScreen(rect, true)) {
            Log.w(TAG, "Scrolling to view was requested, but none of the parents scrolled.")
        }

        uiController.loopMainThreadUntilIdle()

        if (!isDisplayingAtLeast(95).matches(view)) {
            throw PerformException.Builder()
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(RuntimeException(
                            "Scrolling to view was attempted, but the view is not displayed"))
                    .build()
        }
    }

    private fun findScrollView(view: View): View {
        val parent = view.parent as View
        return parent as? NestedScrollView ?: findScrollView(parent)
    }

    override fun getDescription(): String {
        return "scroll to"
    }

    companion object {

        private val TAG = NestedScrollViewScrollToAction::class.java.simpleName

        fun scrollTo(): NestedScrollViewScrollToAction {
            return NestedScrollViewScrollToAction()
        }
    }
}
