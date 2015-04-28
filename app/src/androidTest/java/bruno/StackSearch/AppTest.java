package bruno.StackSearch;


import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.robotium.solo.Solo;

import junit.framework.Test;
import junit.framework.TestSuite;

import bruno.StackSearch.Activities.FeedResultActivity;
import bruno.StackSearch.Activities.MainActivity;
import bruno.StackSearch.Activities.WebViewActivity;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class AppTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public AppTest() {
        super(MainActivity.class);
    }

    // A suite wouldn't be needed for such a simple app, but I thought why not :-)
    public static Test suite() {
        TestSuite suite = new TestSuite(AppTest.class.getName());
        suite.addTest(TestSuite.createTest(AppTest.class, "testMainActivity"));
        suite.addTest(TestSuite.createTest(AppTest.class, "testFailedSearch"));
        suite.addTest(TestSuite.createTest(AppTest.class, "testFeedActivity"));
        return suite;
    }




    public void testMainActivity() throws Exception {
        solo.unlockScreen();   //Unlock the lock screen

        String test_string = "random nonsense 10313921";

        //Makes sure that the checkbox is always *unchecked* when conducting the test.  This is not a test of the app itself.
        if (solo.isCheckBoxChecked(0)) { solo.clickOnCheckBox(0);}

        solo.clickOnButton("Search");  //Makes sure the insanity check is in place
        assertEquals("A toast informing the user that a search term was necessary failed to appear.",
                true, solo.waitForText("Please enter a search term") );

        solo.assertCurrentActivity("Current activity was ", MainActivity.class) ;

        solo.enterText(0, test_string); //Write some random gibberish in the EditText field and then test to make sure it is preserved on activity recreation
        solo.setActivityOrientation(0);
        assertEquals("The EditText content failed to be preserved after the activity was rotated.",
                true, solo.searchText(test_string)) ;
        Log.i("Whatever", "this is a fun log message.");
    }



    public void testFailedSearch() throws Exception {
        solo.unlockScreen();   //Unlock the lock screen

        String test_string = "random nonsense 10313921";

        //Makes sure that the checkbox is always *checked* when conducting the test.  This is not a test of the app itself.
        if (!solo.isCheckBoxChecked(0)) { solo.clickOnCheckBox(0);}

        solo.enterText(0, test_string); //Write some random gibberish in the EditText field and then test to make sure it is preserved on activity recreation

        solo.clickOnButton("Search");  //Makes sure the insanity check is in place

        solo.waitForActivity("MainActivity");

        assertEquals("The failed search sequence failed to initiate.  A toast message was expected in the MainActivity",
                true, solo.waitForText("Your search yielded no result.  Try another search term.") );
    }



    public void testFeedActivity() throws Exception {
        solo.unlockScreen();   //Unlock the lock screen

        //Makes sure that the checkbox is always *checked* when conducting the test.  This is not a test of the app itself.
        if (!solo.isCheckBoxChecked(0)) { solo.clickOnCheckBox(0);}

        String test_string = "Retrofit";
            solo.enterText(0, test_string); //Write some random gibberish in the EditText field and then test to make sure it is preserved on activity recreation

        solo.clickOnButton("Search");  //Makes sure the insanity check is in place

        assertEquals("The header has not been loaded properly",
                true, solo.waitForText("Refresh feed") );

        solo.setActivityOrientation(0);

        assertEquals("The header was not visible after the activity was rotated",
                true, solo.waitForText("Refresh feed") );

        solo.clickInList(2);
        solo.assertCurrentActivity("Current activity is ", WebViewActivity.class) ;

        solo.goBack();

        solo.assertCurrentActivity("Current activity is ", FeedResultActivity.class) ;
    }

    @Override
    public void setUp() throws Exception {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        solo = new Solo(getInstrumentation(), getActivity());
    }


    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }



}
