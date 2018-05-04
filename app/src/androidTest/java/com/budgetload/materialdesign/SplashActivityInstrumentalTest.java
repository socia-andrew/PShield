package com.budgetload.materialdesign;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.budgetload.materialdesign.activity.Registration_Spash;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by andrewlaurienrsocia on 05/09/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SplashActivityInstrumentalTest {


    @Rule
    ActivityTestRule<Registration_Spash> mActivityRule = new ActivityTestRule<>(Registration_Spash.class);


    @Test
    public void myTest() {

    }

}
