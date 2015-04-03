package bruno.stackrest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {

    Button button_search;
    ThreadSearch threadList;

    public static final double VERSION = 2.2;
    public static final String ENDPOINT =
            "https://api.stackexchange.com/" + VERSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestData();

        button_search = (Button) findViewById(R.id.button_search);

        button_search.setOnClickListener(new View.OnClickListener() { //Start work button
            @Override
            public void onClick(View v) {
                requestData();

            }
        });
    }




    private void requestData() {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();

        ThreadSearchAPI api = adapter.create(ThreadSearchAPI.class);

        api.getFeed(new Callback<ThreadSearch>() {

            @Override
            public void success(ThreadSearch arg0, Response arg1) {
                threadList = arg0;
                //updateDisplay();
                Log.i("BANANA", "Had a success in the callback method");
            }

            @Override
            public void failure(RetrofitError arg0) {
                Log.i("BANANA","Failed to get a success in the callback method");
            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
