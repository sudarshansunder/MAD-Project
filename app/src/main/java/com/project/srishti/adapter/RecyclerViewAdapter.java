package com.project.srishti.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.project.srishti.MaterialColorPalette;
import com.project.srishti.Post;
import com.project.srishti.R;
import com.project.srishti.activity.CommentsActivity;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Post> list;
    private Context context;

    public RecyclerViewAdapter(ArrayList<Post> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0)
            return 1;
        else
            return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
                return new PostViewHolder(LayoutInflater.from(context).inflate(R.layout.post_view, parent, false));
            case 1:
                return new NoPostViewHolder(LayoutInflater.from(context).inflate(R.layout.no_post, parent, false));
        }
        return null;
    }

    private String parseDateTime(String dateTime) {
        Log.d("DateTime", dateTime);
        String date = "";
        String temp = dateTime.substring(0, 10);
        String arrs[] = temp.split("/");
        String month = null;
        switch (arrs[1]) {
            case "01":
                month = "Jan";
                break;
            case "02":
                month = "Feb";
                break;
            case "03":
                month = "Mar";
                break;
            case "04":
                month = "Apr";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "Jun";
                break;
            case "07":
                month = "Jul";
                break;
            case "08":
                month = "Aug";
                break;
            case "09":
                month = "Sep";
                break;
            case "10":
                month = "Oct";
                break;
            case "11":
                month = "Nov";
                break;
            case "12":
                month = "Dec";
                break;
        }
        String time = "";
        int day = Integer.valueOf(arrs[2]);
        date = "" + month + " " + day;
        time = dateTime.substring(11, dateTime.length() - 3);
        return "" + date + " at " + time;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PostViewHolder) {
            int color = MaterialColorPalette.getRandomColor("500");
            TextDrawable drawable = TextDrawable.builder().buildRound(list.get(position).getName().charAt(0) + "", color);
            ((PostViewHolder) holder).postImage.setImageDrawable(drawable);
            ((PostViewHolder) holder).content.setText(list.get(position).getContent());
            ((PostViewHolder) holder).name.setText(list.get(position).getName());
            ((PostViewHolder) holder).date.setText(parseDateTime(list.get(position).getDate()));
            ((PostViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getComments(position);
                }
            });
        }
    }

    private void getComments(final int position) {

        Log.d("Getting comments for id", list.get(position).getId());
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra("post", list.get(position));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (list.size() == 0)
            return 1;
        return list.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        ImageView postImage;
        TextView content, name, date;
        CardView cardView;

        public PostViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.postCard);
            postImage = (ImageView) itemView.findViewById(R.id.postImage);
            content = (TextView) itemView.findViewById(R.id.postDescription);
            name = (TextView) itemView.findViewById(R.id.postName);
            date = (TextView) itemView.findViewById(R.id.dateOfPost);
        }
    }

    public class NoPostViewHolder extends RecyclerView.ViewHolder {
        public NoPostViewHolder(View inflate) {
            super(inflate);
        }
    }
}
