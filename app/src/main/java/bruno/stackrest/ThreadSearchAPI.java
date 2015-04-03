package bruno.stackrest;

import android.util.Log;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Bruno on 2015-04-03.
 */
public interface ThreadSearchAPI {



    @GET("/search?pagesize=7&order=desc&sort=activity&tagged=android&site=stackoverflow")

    public void getFeed(Callback<ThreadSearch> response);


}
