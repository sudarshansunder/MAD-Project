package com.project.srishti;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {

    public String id, content, date, name, cat, tags;

    public Post() {

    }

    public Post(String id, String content, String date, String name, String cat, String tags) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.name = name;
        this.cat = cat;
        this.tags = tags;
    }

    protected Post(Parcel in) {
        id = in.readString();
        content = in.readString();
        date = in.readString();
        name = in.readString();
        cat = in.readString();
        tags = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(content);
        parcel.writeString(date);
        parcel.writeString(name);
        parcel.writeString(cat);
        parcel.writeString(tags);
    }
}
