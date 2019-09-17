package br.com.battista.bgscore.helper

import android.view.View
import android.widget.EditText

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object AndroidTestUtils {

    fun withError(expected: String): Matcher<View> {
        return object : TypeSafeMatcher<View>() {

            public override fun matchesSafely(view: View): Boolean {
                return if (view !is EditText) {
                    false
                } else view.error.toString() == expected
            }

            override fun describeTo(description: Description) {

            }
        }
    }
}
