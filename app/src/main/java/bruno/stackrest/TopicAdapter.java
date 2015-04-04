package bruno.stackrest;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class TopicAdapter extends ArrayAdapter<Topic> implements View.OnClickListener{



    private Context context;
    private List<Topic> mList;


    public TopicAdapter(Context context, int resource, List<Topic> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mList = objects;

        //This below is for pictures
       // final int maxMemory = (int)(Runtime.getRuntime().maxMemory() /1024);
       // final int cacheSize = maxMemory / 8;
        //imageCache = new LruCache<>(cacheSize);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_searchresult, parent, false);

        //Display flower name in the TextView widget
        Topic flower = mList.get(position);


        TextView mPoster_name = (TextView) view.findViewById(R.id.poster_name);
            mPoster_name.setText(flower.getdisplay_name());

        TextView mAnswers_number = (TextView) view.findViewById(R.id.answers_number);
            mAnswers_number.setText(flower.getanswer_count());

        TextView m_Title = (TextView) view.findViewById(R.id.thread_name);
            m_Title.setText(flower.gettitle());

        return view;








    }  // End of getView






    // Have to implement with the OnClickListner    onClick is called when a view has been clicked.
    @Override
    public void onClick(View view) { // Parameter v stands for the view that was clicked.

        Log.i("Banana","yo it was clicked");
    }

    //@Override
    public void onViewClick(TopicAdapter arg0, View arg1, int arg2, long arg3) {
        // arg2 = the id of the item in our view (List/Grid) that we clicked
        // arg3 = the id of the item that we have clicked
        // if we didn't assign any id for the Object (Book) the arg3 value is 0
        // That means if we comment, aBookDetail.setBookIsbn(i); arg3 value become 0
        Log.i("Banana","you clicked it like a boss");
    }

}
