package dynobjx.com.jrs.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by jobellebanez on 7/27/15.
 */
public class Track implements Parcelable {
    private int airbill;
    private int trackingCode;
    private String sender;
    private String service;
    private String origin;
    private String destination;
    private String adressee;
    private String dateSent;
    private String originRemarks;
    private String subAirBill;
    private String deliveryStatus;
    private String statusDate;
    private String statusTime;
    private String station;
    private String receiver;
    private String deliveryRemarks;
    private String clientref;



    public Track (int airbill, int trackingCode, String sender, String service, String origin, String destination,
                  String adressee, String dateSent, String originRemarks, String subAirBill, String deliveryStatus, String statusDate,
                  String statusTime, String station, String receiver, String deliveryRemarks, String clientref) {
        this.airbill = airbill;
        this.trackingCode = trackingCode;
        this.sender = sender;
        this.service = service;
        this.origin = origin;
        this.destination = destination;
        this.adressee = adressee;
        this.dateSent = dateSent;
        this.originRemarks = originRemarks;
        this.subAirBill = subAirBill;
        this.deliveryStatus = deliveryStatus;
        this.statusDate = statusDate;
        this.statusTime = statusTime;
        this.station = station;
        this.receiver = receiver;
        this.deliveryRemarks = deliveryRemarks;
        this.clientref = clientref;

    }

    public int getAirbill() {
        return airbill;
    }

    public void setAirbill(int airbill) {
        this.airbill = airbill;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(int trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAdressee() {
        return adressee;
    }

    public void setAdressee(String adressee) {
        this.adressee = adressee;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public String getOriginRemarks() {
        return originRemarks;
    }

    public void setOriginRemarks(String originRemarks) {
        this.originRemarks = originRemarks;
    }

    public String getSubAirBill() {
        return subAirBill;
    }

    public void setSubAirBill(String subAirBill) {
        this.subAirBill = subAirBill;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getReciever() {
        return receiver;
    }

    public void setReciever(String reciever) {
        this.receiver = reciever;
    }

    public String getDeliveryRemarks() {
        return deliveryRemarks;
    }

    public void setDeliveryRemarks(String deliveryRemarks) {
        this.deliveryRemarks = deliveryRemarks;
    }

    public String getClientref() {
        return clientref;
    }

    public void setClientref(String clientref) {
        this.clientref = clientref;
    }


    @Override
    public int describeContents() { return 0;
    }

    public void writeToParcel (Parcel parcel, int i) {
        parcel.writeInt(airbill);
        parcel.writeInt(trackingCode);
        parcel.writeString(sender);
        parcel.writeString(service);
        parcel.writeString(origin);
        parcel.writeString(destination);
        parcel.writeString(adressee);
        parcel.writeString(dateSent);
        parcel.writeString(originRemarks);
        parcel.writeString(subAirBill);
        parcel.writeString(deliveryStatus);
        parcel.writeString(statusDate);
        parcel.writeString(statusTime);
        parcel.writeString(station);
        parcel.writeString(receiver);
        parcel.writeString(deliveryRemarks);
        parcel.writeString(clientref);
    }

    public Track (Parcel parcel) {
        this.airbill = parcel.readInt();
        this.trackingCode = parcel.readInt();
        this.sender = parcel.readString();
        this.service = parcel.readString();
        this.origin = parcel.readString();
        this.destination = parcel.readString();
        this.adressee = parcel.readString();
        this.dateSent = parcel.readString();
        this.originRemarks = parcel.readString();
        this.subAirBill = parcel.readString();
        this.deliveryStatus = parcel.readString();
        this.statusDate = parcel.readString();
        this.statusTime = parcel.readString();
        this.station = parcel.readString();
        this.receiver = parcel.readString();
        this.deliveryRemarks = parcel.readString();
        this.clientref = parcel.readString();
    }




    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Object[] newArray(int arg0) {
            return null;
        }
    };
}
