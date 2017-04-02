package com.project.srishti.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.project.srishti.Comment;
import com.project.srishti.MaterialColorPalette;
import com.project.srishti.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static com.project.srishti.activity.CommentsActivity.parseDateTime;

/**
 * Created by Sudarshan Sunder on 3/26/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Comment> list;

    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.context = context;
        this.list = list;
        getSortedList();
    }

    private void getSortedList() {
        ArrayList<Comment> users = new ArrayList<>();
        ArrayList<Comment> council = new ArrayList<>();
        for (Comment e : this.list) {
            if (e.getType().equals("Counselor")) {
                council.add(e);
            } else {
                users.add(e);
            }
        }
        sortList(users);
        sortList(council);
        this.list.clear();
        this.list.addAll(council);
        this.list.addAll(users);
    }

    private long getDateInMillis(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date d = sdf.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void sortList(ArrayList<Comment> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                long d1 = getDateInMillis(list.get(j).getDate());
                long d2 = getDateInMillis(list.get(j + 1).getDate());
                if (d1 > d2) {
                    Collections.swap(list, j, j + 1);
                }
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.comment_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentViewHolder) {
            int color = MaterialColorPalette.getRandomColor("500");
            TextDrawable drawable = TextDrawable.builder().buildRound(list.get(position).getName().charAt(0) + "", color);
            ((CommentViewHolder) holder).commentImage.setImageDrawable(drawable);
            ((CommentViewHolder) holder).content.setText(list.get(position).getContent());
            ((CommentViewHolder) holder).name.setText(list.get(position).getName());
            ((CommentViewHolder) holder).date.setText(parseDateTime(list.get(position).getDate()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView commentImage;
        TextView content, name, date;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentImage = (ImageView) itemView.findViewById(R.id.commentImage);
            content = (TextView) itemView.findViewById(R.id.commentDescription);
            name = (TextView) itemView.findViewById(R.id.commentName);
            date = (TextView) itemView.findViewById(R.id.dateOfComment);
        }
    }


}
