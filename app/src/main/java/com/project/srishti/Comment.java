package com.project.srishti;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {

    public String id, name, content, type, date, postid;

    public Comment() {

    }

    public Comment(String id, String name, String content, String type, String date, String postid) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.type = type;
        this.date = date;
        this.postid = postid;
    }

    protected Comment(Parcel in) {
        id = in.readString();
        name = in.readString();
        content = in.readString();
        type = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(content);
        parcel.writeString(type);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
