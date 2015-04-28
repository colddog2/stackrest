package bruno.StackSearch.APIs;


import java.util.Map;

import bruno.StackSearch.POJOs.ResponseStackREST;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

public interface TopicSearchAPI {
    @GET("/search?order=desc&sort=activity&site=stackoverflow")
    public void getFeed(@QueryMap Map testMap, Callback<ResponseStackREST> response);
}
