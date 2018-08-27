package comvoice.example.zhangbin.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by zhangbin on 2018/5/23.
 */

public class User implements Parcelable{
    private String name;
    private String password;
    private int age;

    public User (){

    }
    protected User(Parcel in) {
        name = in.readString();
        password = in.readString();
        age = in.readInt();
    }

    /**
     * 序列化
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(password);
        parcel.writeInt(age);
    }

    /**
     * 反序列化
     */
    public static final Creator<User> CREATOR = new Creator<User>() {
        /**
         * 从序列化后的对象中创建原始对象
         */
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        /**
         * 创建指定长度的原始对象数组
         */
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }
}
