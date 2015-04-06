package bruno.StackSearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Bruno on 2015-04-06.
 */
public class WebViewActivity extends ActionBarActivity{

    private Bundle mBundle ;
    private String url ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
    }

    @Override
    protected void onResume() {
        super.onResume();

        android.support.v7.app.ActionBar menu = getSupportActionBar();
            menu.setDisplayShowHomeEnabled(true);
            menu.setLogo(R.mipmap.ic_launcher);
            menu.setDisplayUseLogoEnabled(true);



        mBundle = getIntent().getExtras();

        if(mBundle!=null)               //The condition is met when no data has succesfully been downloaded in the activity.  This check should be superfluous, but I'm keeping it in case this app gets expanded in the future
        {
            url = (String) mBundle.get("url");
        } else {                        // This case below should never happen.  I keep it here in case an unforeseen condition does happen, so the user knows they shouldn't stay there forever
            Toast.makeText(getBaseContext(), "There was a problem loading the webpage.", Toast.LENGTH_SHORT).show();
            finish();
    }                                   //End of case: (mBundle!=null)

        WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.loadUrl(url);

    }  // End of onResume



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



}
