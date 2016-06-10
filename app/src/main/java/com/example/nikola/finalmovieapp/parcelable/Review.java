package com.example.nikola.finalmovieapp.parcelable;

import android.os.Parcel;
import android.os.Parcelable;


public class Review implements Parcelable {

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
    public String author;
    public String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public Review() {
    }

    protected Review(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.content);
    }
}
