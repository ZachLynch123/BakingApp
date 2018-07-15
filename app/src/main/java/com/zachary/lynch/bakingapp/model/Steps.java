package com.zachary.lynch.bakingapp.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Steps implements Parcelable {
    private int id;
    private String shortDescription;
    private String description;
    private String videoUri;
    private String thumbnail;

    public int getIds() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUri() {
        return videoUri;
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
        dest.writeString(videoUri);
        dest.writeString(thumbnail);
    }
    private Steps(Parcel in){
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoUri = in.readString();
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
