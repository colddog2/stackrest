package bruno.StackSearch.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import bruno.StackSearch.POJOs.Topic;
import bruno.StackSearch.R;


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

        //TODO: use the convertView to recycle the view and not instantiate it every single time
        // best explanation: http://android.amberfog.com/?p=296

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        System.out.println("getView " + position + " " + convertView);

        //convertView = inflater.inflate(R.layout.item_searchresult, parent, false);

        if (convertView == null) convertView = inflater.inflate(R.layout.item_searchresult, parent, false);

        Topic mTopic = mList.get(position);


        TextView mPoster_name = (TextView) convertView.findViewById(R.id.poster_name);
            mPoster_name.setText(mTopic.getdisplay_name());

        TextView mAnswers_number = (TextView) convertView.findViewById(R.id.answers_number);
            mAnswers_number.setText(mTopic.getanswer_count());

        TextView m_Title = (TextView) convertView.findViewById(R.id.thread_name);
            m_Title.setText(mTopic.gettitle());

        ImageView mPicture = (ImageView) convertView.findViewById(R.id.picture_poster);
            Picasso.with(getContext())
                    .load(mTopic.getuser_image())
                    .resize(80, 80)
                    .into(mPicture);

        return convertView;
  }

    private static class ViewHolder {
        public TextView mPoster_name;
        public TextView mAnswers_number;
        public TextView m_Title;
        public ImageView mPicture;

    }






}
