package dynobjx.com.jrs.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jobellebanez on 7/27/15.
 */
public class Province extends dynobjx.com.jrs.dao.Province implements Parcelable{

    private int provinceId;
    private String provinceName;

    public Province (int provinceId, String provinceName) {
        this.provinceId = provinceId;
        this.provinceName = provinceName;
    }


    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {

        return provinceName;
    }

    public Integer getProvinceId() {

        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public void writeToParcel (Parcel parcel, int i) {
        parcel.writeInt(provinceId);
        parcel.writeString(provinceName);
    }

    public Province (Parcel parcel) {
        this.provinceId = parcel.readInt();
        this.provinceName = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Province createFromParcel(Parcel in) {
            return new Province(in);
        }

        @Override
        public Object[] newArray(int arg0) {
            return null;
        }
    };

}
