/*
 *     (C) Copyright 2019, ForgetSky.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.zlyandroid.wanandroid.db.greendao;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class TodoData implements Parcelable {

    @Id(autoincrement = true)
    private Long id;

    private String title;//标题

    private int priority;//优先级

    private int type;//标签

    private String content;//内容

    private long date;//日期

    private String dateStr;

    private int isAccomplish;//是否完成

    private String user;//用户

    @Generated(hash = 81073558)
    public TodoData(Long id, String title, int priority, int type, String content, long date, String dateStr, int isAccomplish, String user) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.type = type;
        this.content = content;
        this.date = date;
        this.dateStr = dateStr;
        this.isAccomplish = isAccomplish;
        this.user = user;
    }
    @Generated(hash = 1383565172)
    public TodoData() {
    }
    protected TodoData(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        title = in.readString();
        priority = in.readInt();
        type = in.readInt();
        content = in.readString();
        date = in.readLong();
        dateStr = in.readString();
        isAccomplish =  in.readInt();
        user = in.readString();
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }



    public String getUser() {
        return user;
    }

    public void setUser(String userId) {
        this.user = userId;
    }

    public static final Creator<TodoData> CREATOR = new Creator<TodoData>() {
        @Override
        public TodoData createFromParcel(Parcel in) {
            return new TodoData(in);
        }

        @Override
        public TodoData[] newArray(int size) {
            return new TodoData[size];
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
        dest.writeInt(this.priority);
        dest.writeInt(this.type);
        dest.writeString(this.content);
        dest.writeLong(this.date);
        dest.writeString(this.dateStr);
        dest.writeInt(this.isAccomplish);
        dest.writeString(this.user);
    }
    public int getIsAccomplish() {
        return this.isAccomplish;
    }
    public void setIsAccomplish(int isAccomplish) {
        this.isAccomplish = isAccomplish;
    }

    public  String getStrintg(){
        StringBuilder sbStr=new StringBuilder();
        sbStr.append("title = "+title  +",");
        sbStr.append("priority = "+priority +"," );
        sbStr.append("content = "+content+","  );
        sbStr.append("dateStr = "+dateStr +",");
        sbStr.append("isAccomplish = "+isAccomplish  );
        return sbStr.toString();
    }
}
