package bruno.StackSearch;


import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

public interface TopicSearchAPI {
    @GET("/search?order=desc&sort=activity&site=stackoverflow")
    public void getFeed(@QueryMap Map testMap, Callback<MulticlassPOJO> response);
}
