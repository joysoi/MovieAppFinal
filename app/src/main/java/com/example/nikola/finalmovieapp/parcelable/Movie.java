package com.example.nikola.finalmovieapp.parcelable;


import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    String _id;
    String _img;
    String _title;
    String _plot;
    String _releaseDate;
    String _rating;
    String _voteCount;
    String _backdrop;
    String _key;
    String _site;

    public Movie() {

    }

    public Movie(String id, String img, String title) {
        this._id = id;
        this._img = img;
        this._title = title;
    }


    public Movie(String key, String site) {
        this._key = key;
        this._site = site;
    }

    public Movie(String backdrop, String plot, String poster, String releaseDate, String rating, String voteCount) {
        this._backdrop = backdrop;
        this._plot = plot;
        this._img = poster;
        this._releaseDate = releaseDate;
        this._rating = rating;
        this._voteCount = voteCount;
    }

    public String getId() {
        return this._id;
    }

    public String getImg() {
        return this._img;
    }

    public String getTitle() {
        return this._title;
    }

    public String getPlot() {
        return this._plot;
    }

    public String getReleaseDate() {
        return this._releaseDate;
    }

    public String getRating() {
        return this._rating;
    }

    public String getVoteCount() {
        return this._voteCount;
    }

    public String getKey() {
        return this._key;
    }

    public String getBackdrop() {
        return this._backdrop;
    }

    public String getSite() {
        return this._site;
    }

    protected Movie(Parcel in) {
        this._id = in.readString();
        this._img = in.readString();
        this._title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(_img);
        dest.writeString(_title);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


}
