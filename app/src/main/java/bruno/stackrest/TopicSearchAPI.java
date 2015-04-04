package bruno.stackrest;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Bruno on 2015-04-03.
 */
public interface TopicSearchAPI {



    @GET("/search?order=desc&sort=activity&intitle=a&site=stackoverflow")

    public void getFeed(Callback<MulticlassPOJO> response);


}
