package com.example.myapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.activities.SignInActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SignInActivity::class.java)

    @Test
    fun initialState_registerButtonVisible() {
        // Arrange / Act (activity launched by rule)
        // Assert
        onView(withId(R.id.mbRegister)).check(matches(isDisplayed()))
    }

    @Test
    fun emailInvalid_showsErrorInTvInfo() {
        // Arrange
        onView(withId(R.id.tietEmail)).perform(typeText("bademail"), closeSoftKeyboard())
        // Act
        onView(withId(R.id.mbRegister)).perform(click())
        // Assert
        onView(withId(R.id.tvInfo)).check(matches(isDisplayed()))
    }

    @Test
    fun passwordTooShort_showsErrorInTvInfo() {
        // Arrange
        onView(withId(R.id.tietEmail)).perform(typeText("test@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.tietPassword)).perform(typeText("abc"), closeSoftKeyboard())
        // Act
        onView(withId(R.id.mbRegister)).perform(click())
        // Assert
        onView(withId(R.id.tvInfo)).check(matches(isDisplayed()))
    }

    @Test
    fun passwordsDontMatch_showsErrorInTvInfo() {
        // Arrange
        onView(withId(R.id.tietEmail)).perform(typeText("test@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.tietPassword)).perform(typeText("Passw0rd!"), closeSoftKeyboard())
        onView(withId(R.id.tietPassword2)).perform(typeText("Passw0rd#"), closeSoftKeyboard())
        // Act
        onView(withId(R.id.mbRegister)).perform(click())
        // Assert
        onView(withId(R.id.tvInfo)).check(matches(isDisplayed()))
    }

    @Test
    fun allFieldsValid_dialogAppears() {
        // Arrange
        onView(withId(R.id.tietEmail)).perform(typeText("test@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.tietPassword)).perform(typeText("Passw0rd!"), closeSoftKeyboard())
        onView(withId(R.id.tietPassword2)).perform(typeText("Passw0rd!"), closeSoftKeyboard())
        // Act
        onView(withId(R.id.mbRegister)).perform(click())
        // Assert
        onView(withText("Sí")).check(matches(isDisplayed()))
    }

    @Test
    fun emptyForm_showsErrorInTvInfo() {
        // Arrange (nothing typed)
        // Act
        onView(withId(R.id.mbRegister)).perform(click())
        // Assert
        onView(withId(R.id.tvInfo)).check(matches(isDisplayed()))
    }

    @Test
    fun backButton_navigates() {
        // Arrange / Act
        onView(withId(R.id.btnBack)).perform(click())
        // Assert: no crash (activity navigated without throwing)
    }
}
