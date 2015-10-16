package dynobjx.com.jrs.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jobellebanez on 7/23/15.
 */
public class RateCalculator implements Parcelable {

    private int scopeOfDelivery;
    private int packageClass;
    private int packageType;

    private int origin;
    private int destination;
    private boolean valuation;
    private boolean insurance;
    private int declaredValue;
    private Double weight;
    private Double height;
    private Double length;
    private Double width;


    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }




    public RateCalculator (int scopeOfDelivery, int packageClass, int packageType, int origin, int destination, boolean valuation, boolean insurance, int declaredValue, Double weight,
                           Double height, Double width, Double length) {
        this.scopeOfDelivery = scopeOfDelivery;
        this.packageClass = packageClass;
        this.packageType = packageType;
        this.origin = origin;
        this.destination = destination;
        this.valuation = valuation;
        this.insurance = insurance;
        this.declaredValue = declaredValue;
        this.weight = weight;
        this.height = height;
        this.length = length;
        this.width = width;

    }


    @Override
    public int describeContents() {
        return 0;
    }

    public int getPackageClass() {
        return packageClass;
    }

    public void setPackageClass(int packageClass) {
        this.packageClass = packageClass;
    }

    public int getPackageType() {
        return packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public boolean isValuation() {
        return valuation;
    }

    public Boolean setValuation(boolean valuation) {
        this.valuation = valuation;
        return null;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public Boolean setInsurance(boolean insurance) {
        this.insurance = insurance;
        return null;
    }

    public int getDeclaredValue() {
        return declaredValue;
    }

    public void setDeclaredValue(int declaredValue) {
        this.declaredValue = declaredValue;
    }

    public Double getWeight() {
        return weight;
    }


    public static Creator getCREATOR() {
        return CREATOR;
    }

    public int getScopeOfDelivery() {

        return scopeOfDelivery;
    }

    public void setScopeOfDelivery(int scopeOfDelivery) {
        this.scopeOfDelivery = scopeOfDelivery;
    }

    @Override

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(scopeOfDelivery);
        dest.writeInt(packageClass);
        dest.writeInt(packageType);
        dest.writeInt(origin);
        dest.writeInt(destination);
        dest.writeValue(valuation);
        dest.writeValue(insurance);
        dest.writeInt(declaredValue);
        dest.writeDouble(weight);
    }
    public RateCalculator(Parcel parcel) {
        this.scopeOfDelivery = parcel.readInt();
        this.packageClass = parcel.readInt();
        this.packageType = parcel.readInt();
        this.origin = parcel.readInt();
        this.destination = parcel.readInt();
        this.valuation = parcel.readByte() != 0;
        this.insurance = parcel.readByte() != 0;
        this.weight = parcel.readDouble();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };

}
