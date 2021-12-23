package angoothape.wallet.di.JSONdi.restResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Services implements Parcelable {
    @SerializedName("operatorId")
    public String operatorId;
    @SerializedName("Service_key")
    public String service_key;
    @SerializedName("Service_Name")
    public String service_Name;
    @SerializedName("Service_Number")
    public String service_Number;
    @SerializedName("Traveler_Agent_Name")
    public String traveler_Agent_Name;
    @SerializedName("Bus_Type_Name")
    public String bus_Type_Name;
    @SerializedName("Start_time")
    public String start_time;
    @SerializedName("Arr_Time")
    public String arr_Time;
    @SerializedName("TravelTime")
    public String travelTime;
    @SerializedName("Source_ID")
    public int source_ID;
    @SerializedName("Destination_ID")
    public int destination_ID;
    @SerializedName("Fare")
    public int fare;
    @SerializedName("available_seats")
    public String available_seats;
    @SerializedName("jdate")
    public String jdate;
    @SerializedName("BUS_START_DATE")
    public String bUS_START_DATE;
    public int layout_id;
    @SerializedName("Amenities")
    public String amenities;
    @SerializedName("boarding_info")
    public List<String> boarding_info;
    @SerializedName("dropping_info")
    public List<String> dropping_info;
    @SerializedName("Cancellationpolicy")
    public String cancellationpolicy;
    @SerializedName("bus_type")
    public String bus_type;
    public String isBordDropFirst;
    public String isSingleLady;
    public List<Object> allowedConcessions;
    public Object operator_discount;
    public Object operator_ratings;


    protected Services(Parcel in) {
        operatorId = in.readString();
        service_key = in.readString();
        service_Name = in.readString();
        service_Number = in.readString();
        traveler_Agent_Name = in.readString();
        bus_Type_Name = in.readString();
        start_time = in.readString();
        arr_Time = in.readString();
        travelTime = in.readString();
        source_ID = in.readInt();
        destination_ID = in.readInt();
        fare = in.readInt();
        available_seats = in.readString();
        jdate = in.readString();
        bUS_START_DATE = in.readString();
        layout_id = in.readInt();
        amenities = in.readString();
        boarding_info = in.createStringArrayList();
        dropping_info = in.createStringArrayList();
        cancellationpolicy = in.readString();
        bus_type = in.readString();
        isBordDropFirst = in.readString();
        isSingleLady = in.readString();
    }

    public static final Creator<Services> CREATOR = new Creator<Services>() {
        @Override
        public Services createFromParcel(Parcel in) {
            return new Services(in);
        }

        @Override
        public Services[] newArray(int size) {
            return new Services[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(operatorId);
        dest.writeString(service_key);
        dest.writeString(service_Name);
        dest.writeString(service_Number);
        dest.writeString(traveler_Agent_Name);
        dest.writeString(bus_Type_Name);
        dest.writeString(start_time);
        dest.writeString(arr_Time);
        dest.writeString(travelTime);
        dest.writeInt(source_ID);
        dest.writeInt(destination_ID);
        dest.writeInt(fare);
        dest.writeString(available_seats);
        dest.writeString(jdate);
        dest.writeString(bUS_START_DATE);
        dest.writeInt(layout_id);
        dest.writeString(amenities);
        dest.writeStringList(boarding_info);
        dest.writeStringList(dropping_info);
        dest.writeString(cancellationpolicy);
        dest.writeString(bus_type);
        dest.writeString(isBordDropFirst);
        dest.writeString(isSingleLady);
    }
}
