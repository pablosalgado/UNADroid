package co.edu.unadvirtual.computacion.movil.videos;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.edu.unadvirtual.computacion.movil.R;

public class VideoListViewAdapter extends BaseAdapter {
    private final List<Video> videoList;
    private final Context context;

    public VideoListViewAdapter(List<Video> videoList, Context context) {

        this.videoList = videoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.videos_listview_items, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.icon = rowView.findViewById(R.id.videoIcon);
            viewHolder.title = rowView.findViewById(R.id.videoTitle);
            viewHolder.description = rowView.findViewById(R.id.videoDescription);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        Video video = videoList.get(position);

        holder.title.setText(video.getName());
        holder.description.setText(video.getDescription());

        return rowView;
    }

    static class ViewHolder {
        public TextView title;
        public ImageView icon;
        public TextView description;
    }
}
