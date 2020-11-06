package com.zlyandroid.wanandroid.db.greendao;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


@Entity(nameInDb = "ReadRecordModel_table")
public class ReadRecordModel implements Parcelable {

    @Id(autoincrement = true)
    private Long id;

    private String title;//标题

    private String link;//连接

    private Long time;//时间

    @Generated(hash = 464076519)
    public ReadRecordModel(Long id, String title, String link, Long time) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.time = time;
    }
    @Generated(hash = 64919081)
    public ReadRecordModel() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    protected ReadRecordModel(Parcel in) {
    }

    public static final Creator<ReadRecordModel> CREATOR = new Creator<ReadRecordModel>() {
        @Override
        public ReadRecordModel createFromParcel(Parcel in) {
            return new ReadRecordModel(in);
        }

        @Override
        public ReadRecordModel[] newArray(int size) {
            return new ReadRecordModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.link);
        dest.writeLong(this.time);
    }

    public  String getStrintg(){
        StringBuilder sbStr=new StringBuilder();
        sbStr.append("title = "+title  +",");
        sbStr.append("link = "+link +"," );
        sbStr.append("time = "+time+","  );
        return sbStr.toString();
    }
}
