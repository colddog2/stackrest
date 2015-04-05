package bruno.StackSearch;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class FeedResultActivity extends ListActivity {

    MulticlassPOJO mMulticlassPOJO;
    public String search_string ;

    public static final double VERSION = 2.2;
    public static final String ENDPOINT =
            "https://api.stackexchange.com/" + VERSION;
    private ProgressDialog progress;
    private ListView mListView;
    private Integer search_returns_quantity;
    boolean data_loaded_succesfully ;
    protected Integer screenHeight;
    protected boolean android_tag_applied ;
    protected String tag_searched ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.search_results);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;

        Log.i("Banana","Screen height: " + Integer.toString(screenHeight) )  ;


        if (!data_loaded_succesfully && !data_loaded_succesfully) {  //TODO: this is not working as intended.  See other boolean condition
            progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Please wait while your results are loading");
            progress.show();
        }


        mListView = getListView();

        Bundle mBundle = getIntent().getExtras();

        if(mBundle!=null)  //The condition should always be met since this activity gets started in 1 way only
        {
            String temp_string = (String) mBundle.get("search_string");
            if (temp_string != null && !temp_string.isEmpty()) {  search_string = temp_string;  }
            android_tag_applied = (Boolean) mBundle.get("android_tag_applied");
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    requestData();
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse(mMulticlassPOJO.getItems()[position - 1].getLink()));
                    //TODO : manage the stack in a way that makes sense
                    startActivity(i);
                }
            }

        }); // End of setOnItemClickListener

        requestData();
   }  // End of onResume




    private void requestData() {
         Log.i("Banana", "Requesting new data");

        OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(Constants.HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
            okHttpClient.setWriteTimeout(Constants.HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(Constants.HTTP_TIMEOUT, TimeUnit.MILLISECONDS);

        RestAdapter adapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(ENDPOINT)
                .setClient(new OkClient(okHttpClient))
                .build();

        TopicSearchAPI api = adapter.create(TopicSearchAPI.class);

        Log.i("Banana", "search_string is: " + search_string);

        if (android_tag_applied) {
            tag_searched = "Android";
        }

        Map QueryMap = new HashMap<String,String>();
        QueryMap.put("intitle=", search_string);
        QueryMap.put("tagged=", tag_searched);

        api.getFeed(QueryMap, new Callback<MulticlassPOJO>() {

            @Override
            public void success(MulticlassPOJO arg0, Response arg1) {

            search_returns_quantity = arg0.getItems().length;
            Log.i("Banana","Number of items returned: " + Integer.toString(search_returns_quantity) )  ;

                if (search_returns_quantity == 0) {
                    Toast.makeText(getBaseContext(), "Your search yielded no result.  Try another search term.", Toast.LENGTH_SHORT).show();
                    return_main_activity();
                } else {
                    mMulticlassPOJO = arg0;
                    ListView mListView = getListView();
                    LayoutInflater mInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    if (mListView.getHeaderViewsCount() > 0) {
                        //Do nothing
                    } else {
                        View header = mInflater.inflate(R.layout.list_header, null);
                        mListView.addHeaderView(header);
                    }

                    List<Topic> ListTopic = new ArrayList<>();  // This is the object that will populate the listview

                    if (search_returns_quantity> 14) {
                        search_returns_quantity = 14 ;
                    }

                    //screenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(getWindowManager().getDefaultDisplay());



                    for (int i = 0; i < search_returns_quantity-1; i++) {
                        ListTopic.add(new Topic(mMulticlassPOJO.getItems()[i].getOwner().getDisplay_name(),
                                        mMulticlassPOJO.getItems()[i].getOwner().getProfile_image(),
                                        mMulticlassPOJO.getItems()[i].getAnswer_count(),
                                        mMulticlassPOJO.getItems()[i].getTitle(),
                                        mMulticlassPOJO.getItems()[i].getLink())
                                     );  // End of ListTopic.add
                    }  // end of for loop


                    if (!data_loaded_succesfully) {   //TODO: this is not working as intended.  See other boolean condition
                        progress.dismiss();  //Dismisses the progress dialog that appeared at the beginning of the activity
                    }

                    // try this to deal with the state of the adapter - http://stackoverflow.com/questions/6534740/android-listview-adapter-how-to-detect-an-empty-list

                    TopicAdapter adapter = new TopicAdapter(getBaseContext(), R.layout.item_searchresult, ListTopic);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    data_loaded_succesfully = true ;
                }
            }

            @Override
            public void failure(RetrofitError arg0) {
                Log.i("BANANA", "Retrofit failed: " + arg0.getMessage());
            }
        });

    }


    private void return_main_activity () {
        Intent mIntent = new Intent(this, MainActivity.class);
        startActivity(mIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listactivity, menu);
        return true;
    }


}  // End of Activity

