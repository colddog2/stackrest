package bruno.stackrest;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ListActivity {

    Button button_search;
    MulticlassPOJO mMulticlassPOJO;


    public static final double VERSION = 2.2;
    public static final String ENDPOINT =
            "https://api.stackexchange.com/" + VERSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.search_results);
       // List<List_for_adapter> objects = new List_for_adapter ;

        ListView mListView = getListView();

        requestData();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Toast.makeText(getBaseContext(), "This is the header", Toast.LENGTH_SHORT).show();
                    //TODO: refresh the feed from here.  Yo.
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse(mMulticlassPOJO.getItems()[position - 1].getLink()));
                    //TODO : manage the stack in a way that makes sense
                    startActivity(i);
                }

            }

        }); // End of setOnItemClickListener
    }  // End of onResume




    private void requestData() {

        Toast.makeText(getBaseContext(), "Requesting new data", Toast.LENGTH_SHORT).show();


        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();

        TopicSearchAPI api = adapter.create(TopicSearchAPI.class);

        api.getFeed(new Callback<MulticlassPOJO>() {

            @Override
            public void success(MulticlassPOJO arg0, Response arg1) {
                mMulticlassPOJO = arg0;


                ListView mListView = getListView();


                LayoutInflater mInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View header = mInflater.inflate(R.layout.list_header, null);
                mListView.addHeaderView(header);





                List<Topic> ListTopic = new ArrayList<>();  // This is the object that will populate the listview

                for(int i=0; i<7; i++){
                    ListTopic.add(new Topic(mMulticlassPOJO.getItems()[i].getOwner().getDisplay_name(),
                                    mMulticlassPOJO.getItems()[i].getOwner().getProfile_image(),
                                    mMulticlassPOJO.getItems()[i].getAnswer_count(),
                                    mMulticlassPOJO.getItems()[i].getTitle(),
                                    mMulticlassPOJO.getItems()[i].getLink())
                    );

                   /* Log.i("BANANA", "object " + i + " display name: " + object.get(i).getdisplay_name());
                      Log.i("BANANA", "object " + i + " user_image: " + object.get(i).getuser_image());
                      Log.i("BANANA", "object " + i + " answer count: " + object.get(i).getanswer_count());
                      Log.i("BANANA", "object " + i + " title: " + object.get(i).gettitle());
                      Log.i("BANANA", "object " + i + " link: " + object.get(i).getlink()); */
                }  // end of for loop


                // try this to deal with the state of the adapter - http://stackoverflow.com/questions/6534740/android-listview-adapter-how-to-detect-an-empty-list

                TopicAdapter adapter = new TopicAdapter(getBaseContext(), R.layout.item_searchresult, ListTopic);
                setListAdapter(adapter);
                adapter.notifyDataSetChanged();


                Log.i("BANANA", "Quota remaining: " + mMulticlassPOJO.getQuota_remaining());
                Log.i("BANANA", "First title: " + mMulticlassPOJO.getItems()[0].getTitle());
                Log.i("BANANA", "Last title: " + mMulticlassPOJO.getItems()[6].getTitle());
            }

            @Override
            public void failure(RetrofitError arg0) {
                Log.i("BANANA", "Retrofit failed: " + arg0.getMessage());
            }
        });

    }





    protected void updateDisplay() {
        //Use FlowerAdapter to display data

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
