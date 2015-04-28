package bruno.StackSearch.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import bruno.StackSearch.R;


public class MainActivity extends ActionBarActivity {

    //TODO: Refactor this activity

    private static Button search_button;
    private static EditText mEditText;
    private static CheckBox mCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitializeUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitializeClickListeners();
    }

    private void InitializeClickListeners() {
        mCheckbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {     save_checkbox_to_preferences();    }
        }); //End of mCheckbox.setOnClickListener


        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String temp_string =  mEditText.getText().toString();

                if ( TextUtils.isEmpty(temp_string) && !mCheckbox.isChecked()) {
                    Toast.makeText(getBaseContext(), "Please enter a search term", Toast.LENGTH_SHORT).show();
                    return ;
                }  else if (!isNetworkAvailable()) {
                    Toast.makeText(getBaseContext(), "You need an Internet connection to search Stackoverflow.", Toast.LENGTH_SHORT).show();
                    return ;
                }

                InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

                start_feed_activity(temp_string);

          }
        });
    }

    private void InitializeUI() {
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar menu = getSupportActionBar();
            menu.setDisplayShowHomeEnabled(true);
            menu.setLogo(R.mipmap.ic_launcher);
            menu.setDisplayUseLogoEnabled(true);;

        search_button =  (Button)findViewById(R.id.button_search);
        mEditText =  (EditText)findViewById(R.id.et_SearchTopic);
        mCheckbox = (CheckBox) findViewById(R.id.AndroidTagCheckbox);

        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            mCheckbox.setChecked(mSharedPreferences.getBoolean("android_tag_applied", false));
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void save_checkbox_to_preferences () {
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEditor = mSharedPreferences.edit();
        prefEditor.putBoolean("android_tag_applied", mCheckbox.isChecked());
        prefEditor.commit();
    }

    private void start_feed_activity (String search_string) {
        Intent mIntent = new Intent(this, FeedResultActivity.class);
        mIntent.putExtra("search_string", search_string);
        mIntent.putExtra("android_tag_applied", mCheckbox.isChecked());
        startActivity(mIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}