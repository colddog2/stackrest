package bruno.StackSearch.Activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import bruno.StackSearch.APIs.TopicSearchAPI;
import bruno.StackSearch.Adapters.TopicAdapter;
import bruno.StackSearch.POJOs.ResponseStackREST;
import bruno.StackSearch.POJOs.Topic;
import bruno.StackSearch.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class FeedResultActivity extends ActionBarActivity {

        //TODO: Refactor this activity
        //TODO: Convert the HTML encoding to normality
    public static final double VERSION = 2.2;
    public static final String ENDPOINT =
            "https://api.stackexchange.com/" + VERSION;

    public static Long second = 1000L ;
    public static Long minute = 60*second ;
    public static Long HTTP_TIMEOUT = 10*second ;

    private boolean DataAlreadyDownloaded, android_tag_applied ;
    private String search_string, tag_searched, json_array_content, device_size ;

    private ProgressDialog mProgressDialog;
    private ListView mListView;
    private Integer NumberOfSearchReturns;
    private List<Topic> ListTopic ;
    private View header;
    private TopicAdapter mAdapter ;
    private Bundle mBundle ;
    private OkHttpClient mOkHttpClient ;
    private ResponseStackREST mResponseStackREST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InitializeUI();
        InitializeHeaderUI();

        if (DataAlreadyDownloaded) {
            setupHeaderUI();
            SetSearchResultsAdapter();
        } else if(!QueryArgumentsAreNull()) {
            PrepareQueryArgumentsFromBundle();
            requestData();
        } else {
            Log.i("Warning", "Warning: something odd and unexpected happened");
        }
    }

    private boolean QueryArgumentsAreNull() {
        if (mBundle==null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
   }

    private void PrepareQueryArgumentsFromBundle() {
        android_tag_applied = (Boolean) mBundle.get("android_tag_applied");
        String temp_string = (String) mBundle.get("search_string");
        PrepareSearchTag();

        if (temp_string != null && !temp_string.isEmpty() ) search_string = temp_string;
    }

    private void InitializeHeaderUI() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    DataAlreadyDownloaded = false ; //Setting it to false ensures that if something goes wrong during the requestData() method, the activity will start anew
                    requestData();
                } else {
                    startWebView(position);
                }
            }

        });
    }

    private void startWebView (Integer position) {
        Intent mIntent = new Intent(this, WebViewActivity.class);

        mIntent.putExtra("url", (ListTopic.get(position - 1).getlink()));
        startActivity(mIntent);
    }

    private void requestData() {
        displayProgressDialog();

        BuildOkHttpClient();
        RestAdapter mRestAdapter = buildRestAdapter();

        Map QueryMap = buildQueryMap();

        TopicSearchAPI api = mRestAdapter.create(TopicSearchAPI.class);
        api.getFeed(QueryMap, new Callback<ResponseStackREST>() {

            @Override
            public void success(ResponseStackREST arg0, Response arg1) {
                NumberOfSearchReturns = arg0.getItems().length ;
                Log.i("Banana","Number of items returned: " + Integer.toString(NumberOfSearchReturns) )  ;

                if (NumberOfSearchReturns == 0) {
                    NoSearchResultsFinishActivity();
                } else {
                    mResponseStackREST = arg0;
                    buildListTopic(mResponseStackREST);

                    getDeviceSize();
                    setMaximumListviewLength();
                    setupHeaderUI();
                    SetSearchResultsAdapter();

                    convertListTopicIntoJson();

                    DataAlreadyDownloaded = true ;
                    dismissProgressDialogIfPresent();
                }
            }

            @Override
            public void failure(RetrofitError arg0) {
            }
        });
    }

    private void NoSearchResultsFinishActivity() {
        Toast.makeText(getBaseContext(), "Your search yielded no result.  Try another search term.", Toast.LENGTH_SHORT).show();
        dismissProgressDialogIfPresent();
        finish();
    }

    private void dismissProgressDialogIfPresent() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();
    }

    private void convertListTopicIntoJson() {
        json_array_content = new Gson().toJson(ListTopic);  //Convert ListTopic to JSON so it can be saved in onSaveInstanceState(...)
    }

    private void SetSearchResultsAdapter() {
        mAdapter = new TopicAdapter(getBaseContext(), R.layout.item_searchresult, ListTopic);
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
    }

    private void buildListTopic(ResponseStackREST mResponseStackREST) {
        ListTopic = new ArrayList<>();  // ListTopic is the object that will populate the listview

        for (int i = 0; i < NumberOfSearchReturns -1; i++) {
            ListTopic.add(new Topic(mResponseStackREST.getItems()[i].getOwner().getDisplay_name(),
                            mResponseStackREST.getItems()[i].getOwner().getProfile_image(),
                            mResponseStackREST.getItems()[i].getAnswer_count(),
                            mResponseStackREST.getItems()[i].getTitle(),
                            mResponseStackREST.getItems()[i].getLink())
                         );  // End of ListTopic.add
        }  // end of for loop that populates ListTopic
    }

    private void setMaximumListviewLength() {
        /** Note: If the number of search returns is lower than any threshold, that number of returns will be preserved.  The idea here is to prevent 30-item lists on phones */
        if (device_size.equals("large_tablet") && NumberOfSearchReturns > 26 ) {
            NumberOfSearchReturns = 26 ;
        } else if (device_size.equals("small_tablet") & NumberOfSearchReturns > 19 ) {
            NumberOfSearchReturns = 19 ;
        }  else if (device_size.equals("phone") & NumberOfSearchReturns > 13 ) {
            NumberOfSearchReturns = 13 ;
        }
    }

    private Map buildQueryMap() {
        Map QueryMap = new HashMap<String,String>();
            QueryMap.put("intitle=", search_string);
            QueryMap.put("tagged=", tag_searched);
        return QueryMap;
    }

    private void PrepareSearchTag() {
        if (android_tag_applied) {  tag_searched = "Android";  }
    }

    private void displayProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Loading");
            mProgressDialog.setMessage("Please wait while your results are loading");
            mProgressDialog.show();
    }

    private void getDeviceSize() {
        if (TextUtils.isEmpty(device_size)) {  device_size = infer_device_size() ;   }
    }

    private RestAdapter buildRestAdapter() {
        return new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(ENDPOINT)
                    .setClient(new OkClient(mOkHttpClient))
                    .build();
    }

    private void BuildOkHttpClient() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
        mOkHttpClient.setWriteTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
        mOkHttpClient.setReadTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private String infer_device_size () {

        String size = "";
        DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float widthInches = metrics.widthPixels / metrics.xdpi;
        float heightInches = metrics.heightPixels / metrics.ydpi;

        double diagonalInches = Math.sqrt((widthInches * widthInches) + (heightInches * heightInches));     //a² + b² = c² -- pythagorean theorem

        if (diagonalInches >= 10)        size = "large_tablet";
        else if (diagonalInches >= 7)    size = "small_tablet";
        else if (diagonalInches < 7)     size = "phone";
        else                            Log.i("Banana", "Something went horribly wrong in infer_device_size().  Sorry.");

        return size ;
    }

    @Override
    public void onSaveInstanceState(Bundle Icicle) {
        super.onSaveInstanceState(Icicle);
        Icicle.putBoolean("android_tag_applied", android_tag_applied);
        Icicle.putString("search_string", search_string);
        Icicle.putString("device_size", device_size);

        Icicle.putString("json_array_content", json_array_content);
        Icicle.putBoolean("DataAlreadyDownloaded", DataAlreadyDownloaded);
    }

    @Override
    protected void onRestoreInstanceState(Bundle Icicle) {
        super.onRestoreInstanceState(Icicle);

        android_tag_applied = Icicle.getBoolean("android_tag_applied");
        search_string = Icicle.getString("search_string");
        device_size = Icicle.getString("device_size");

        json_array_content = Icicle.getString("json_array_content");
            if (TextUtils.isEmpty(json_array_content)) {
                //Do nothing since the string is empty
            } else {
                //This below creates a list that can be input to the adapter
                Type type = new TypeToken<List<Topic>>(){}.getType();
                ListTopic = new Gson().fromJson(json_array_content, type);

                DataAlreadyDownloaded = Icicle.getBoolean("DataAlreadyDownloaded");
            }

    }

    private void setupHeaderUI() {
        if (mListView.getHeaderViewsCount() == 0) {  mListView.addHeaderView(header);   }
    }

    private void InitializeUI() {
        setContentView(R.layout.search_results);

        mListView = (ListView)findViewById(android.R.id.list);
        mBundle = getIntent().getExtras();

        LayoutInflater mInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        header = mInflater.inflate(R.layout.list_header, null);

        android.support.v7.app.ActionBar menu = getSupportActionBar();
            menu.setDisplayShowHomeEnabled(true);
            menu.setLogo(R.mipmap.ic_launcher);
            menu.setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listactivity, menu);
        return true;
    }

}

