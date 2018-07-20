package com.zachary.lynch.bakingapp.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Steps implements Parcelable {
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnail;

    public Steps(int pageId, String shortDesc, String desc, String video, String thumb){
        id = pageId;
        shortDescription = shortDesc;
        description = desc;
        videoURL = video;
        thumbnail = thumb;
    }

    public int getIds() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnail);
    }
    private Steps(Parcel in){
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnail = in.readString();
    }
    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };
}
