package bruno.stackrest;


import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface TopicSearchAPI {
    @GET("/search?order=desc&sort=activity&site=stackoverflow")
    public void getFeed(@Query("intitle=") String search_topic, Callback<MulticlassPOJO> response);
}
