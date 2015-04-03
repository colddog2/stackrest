package bruno.stackrest;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Bruno on 2015-04-03.
 */
public interface ThreadSearchAPI {

    @GET("BUILT_STRING")
    public void getFeed(Callback<List<ThreadSearch>> response);

}
