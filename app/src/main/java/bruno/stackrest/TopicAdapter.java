package bruno.stackrest;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class TopicAdapter extends ArrayAdapter<Topic> {



    private Context context;
    private List<Topic> mList;


    public TopicAdapter(Context context, int resource, List<Topic> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View search_item = inflater.inflate(R.layout.item_searchresult, parent, false);

        //Display flower name in the TextView widget
        Topic flower = mList.get(position);


        TextView mPoster_name = (TextView) search_item.findViewById(R.id.poster_name);
            mPoster_name.setText(flower.getdisplay_name());

        TextView mAnswers_number = (TextView) search_item.findViewById(R.id.answers_number);
            mAnswers_number.setText(flower.getanswer_count());

        TextView m_Title = (TextView) search_item.findViewById(R.id.thread_name);
            m_Title.setText(flower.gettitle());

        ImageView mPicture = (ImageView) search_item.findViewById(R.id.picture_poster);
            Picasso.with(getContext())
                    .load(flower.getuser_image())
                    .resize(80, 80)
                    .into(mPicture);



        return search_item;
  }  // End of getView








}
