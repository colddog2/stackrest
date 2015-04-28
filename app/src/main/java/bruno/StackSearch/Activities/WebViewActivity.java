package bruno.StackSearch.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.Toast;

import bruno.StackSearch.R;

public class WebViewActivity extends ActionBarActivity{

    private Bundle mBundle ;
    private String url ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        SetupActionBar();

        mBundle = getIntent().getExtras();



        finishIfBundleIsNull();
        loadURLintoWebView();
    }

    private void SetupActionBar() {
        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.mipmap.ic_launcher);
        menu.setDisplayUseLogoEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void finishIfBundleIsNull() {
        if (mBundle==null)   {
            Toast.makeText(getBaseContext(), "There was a problem loading the webpage.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadURLintoWebView() {
        if(mBundle!=null)  url = (String) mBundle.get("url");
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



}
