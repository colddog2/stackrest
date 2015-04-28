package bruno.StackSearch.Adapters;

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

        //Todo.  Investigate this.  It seems like the pictures aren't downloaded asynchronously as they should.  I don't know Picasso much.

        ViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        System.out.println("getView " + position + " " + convertView);


        if (convertView == null) {
            Log.i("getView","The ConvertView got created from scratch");
            convertView = inflater.inflate(R.layout.item_searchresult, parent, false);
            holder = new ViewHolder();

            holder.PosterName = (TextView) convertView.findViewById(R.id.poster_name);
            holder.AnswerCount = (TextView) convertView.findViewById(R.id.answers_number) ;
            holder.PostTitle = (TextView) convertView.findViewById(R.id.thread_name) ;
            holder.PosterPicture = (ImageView) convertView.findViewById(R.id.picture_poster);

            convertView.setTag(holder);
        } else {
            Log.i("getView","The ConvertView got recycled");
            holder = (ViewHolder) convertView.getTag();
        }

        Topic mTopic = mList.get(position);


        holder.PosterName.setText(mTopic.getdisplay_name());
        holder.AnswerCount.setText(mTopic.getanswer_count());
        holder.PostTitle.setText(mTopic.gettitle());
        Picasso.with(getContext())
                .load(mTopic.getuser_image())
                .resize(80, 80)
                .into(holder.PosterPicture);

        return convertView;
    }

    private static class ViewHolder {
        public TextView PosterName;
        public TextView AnswerCount;
        public TextView PostTitle;
        public ImageView PosterPicture;
    }


}
