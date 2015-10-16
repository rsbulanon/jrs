package dynobjx.com.jrs.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rsbulanon on 7/23/15.
 */
public class Branch implements Parcelable {

    private int branchId;
    private String branchName;
    private String branchAddress;
    private String contactPerson;
    private String contactNumbers;
    private double longitude;
    private double latitude;

    public Branch(int branchId, String branchName, String branchAddress, String contactPerson,
                  String contactNumbers, double longitude, double latitude) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.branchAddress = branchAddress;
        this.contactPerson = contactPerson;
        this.contactNumbers = contactNumbers;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumbers() {
        return contactNumbers;
    }

    public void setContactNumbers(String contactNumbers) {
        this.contactNumbers = contactNumbers;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(branchId);
        parcel.writeString(branchName);
        parcel.writeString(branchAddress);
        parcel.writeString(contactPerson);
        parcel.writeString(contactNumbers);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
    }

    public Branch(Parcel in) {
        this.branchId = in.readInt();
        this.branchName = in.readString();
        this.branchAddress = in.readString();
        this.contactPerson = in.readString();
        this.contactNumbers = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Branch createFromParcel(Parcel in) {
            return new Branch(in);
        }

        @Override
        public Object[] newArray(int arg0) {
            return null;
        }
    };
}
