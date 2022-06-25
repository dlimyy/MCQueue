package orbital.project

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.TypeTextAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LoginpageTest {

    @get:Rule var activityScenarioRule = activityScenarioRule<Loginpage>()

    @Test
    fun checkActivityVisibility() {
        onView(withId(R.id.layout_loginActivity)).check(matches(isDisplayed()))
    }

    @Test
    fun loginWithoutUsernameAndPassword() {
        onView(withId(R.id.login)).perform(click())
        onView(withId(R.id.usernamelayout))
            .check(matches(hasTextInputLayoutError("Please enter username")))
        onView(withId(R.id.passwordlayout))
            .check(matches(hasTextInputLayoutError("Please enter password")))
    }

    @Test
    fun loginWithoutPassword() {
        onView(withId(R.id.login)).perform(click())
        onView(withId(R.id.loginusername)).perform(TypeTextAction("test@gmail.com"))
        onView(withId(R.id.passwordlayout))
            .check(matches(hasTextInputLayoutError("Please enter password")))
    }


    //Helper function
    private fun hasTextInputLayoutError(expectedErrorText: String): Matcher<View> = object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description?) { }

        override fun matchesSafely(item: View?): Boolean {
            if (item !is TextInputLayout) return false
            val error = item.error ?: return false
            val errorMessage = error.toString()
            return expectedErrorText == errorMessage
        }
    }
}