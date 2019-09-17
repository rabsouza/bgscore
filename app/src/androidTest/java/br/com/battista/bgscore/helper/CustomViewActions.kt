package br.com.battista.bgscore.helper

import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.util.HumanReadables
import android.support.test.espresso.util.TreeIterables
import android.view.View
import android.widget.Checkable
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.util.concurrent.TimeoutException

object CustomViewActions {

    fun waitWithId(viewId: Int, millis: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): org.hamcrest.Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for a specific view with id <$viewId> during $millis millis."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + millis
                val viewMatcher = withId(viewId)

                do {
                    for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50)
                } while (System.currentTimeMillis() < endTime)

                // timeout happens
                throw PerformException.Builder()
                        .withActionDescription(this.description)
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(TimeoutException())
                        .build()
            }
        }

    }

    fun waitWithText(textId: Int, millis: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): org.hamcrest.Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for a specific view with text id <$textId> during $millis millis."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + millis
                val viewMatcher = withText(textId)

                do {
                    for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50)
                } while (System.currentTimeMillis() < endTime)

                // timeout happens
                throw PerformException.Builder()
                        .withActionDescription(this.description)
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(TimeoutException())
                        .build()
            }
        }

    }

    fun setChecked(checked: Boolean): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return object : Matcher<View> {
                    override fun describeTo(description: Description) {
                        description.appendText("is checked ").appendValue(checked)
                    }

                    override fun matches(item: Any): Boolean {
                        return isA(Checkable::class.java).matches(item)
                    }

                    override fun describeMismatch(item: Any, mismatchDescription: Description) {}

                    override fun _dont_implement_Matcher___instead_extend_BaseMatcher_() {}


                }
            }

            override fun getDescription(): String {
                return "check to value is$checked."
            }

            override fun perform(uiController: UiController, view: View) {
                val checkableView = view as Checkable
                checkableView.isChecked = checked
            }
        }
    }

}
