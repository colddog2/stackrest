package bruno.stackrest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
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

       // List<List_for_adapter> objects = new List_for_adapter ;


        requestData();

        //objects.
        //List<List_for_adapter> mList = List_for_adapter.class;
        //mList.add_record


        //requestData();

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



                List<List_for_adapter> object = new ArrayList<>();

                for(int i=0; i<7; i++){
                    object.add(new List_for_adapter(threadList.getItems()[i].getOwner().getDisplay_name(),
                                    threadList.getItems()[i].getOwner().getProfile_image(),
                                    threadList.getItems()[i].getAnswer_count(),
                                    threadList.getItems()[i].getTitle(),
                                    threadList.getItems()[i].getLink() )
                              );

                    Log.i("BANANA", "object " + i + " display name: " + object.get(i).getdisplay_name());
                    Log.i("BANANA", "object " + i + " user_image: " + object.get(i).getuser_image());
                    Log.i("BANANA", "object " + i + " answer count: " + object.get(i).getanswer_count());
                    Log.i("BANANA", "object " + i + " title: " + object.get(i).gettitle());
                    Log.i("BANANA", "object " + i + " link: " + object.get(i).getlink());
                }  // end of for loop








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
