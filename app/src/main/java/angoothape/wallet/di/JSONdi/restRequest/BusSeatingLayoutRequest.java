package angoothape.wallet.di.JSONdi.restRequest;

import android.os.Parcel;
import android.os.Parcelable;

public class BusSeatingLayoutRequest implements Parcelable {
    Credentials Credentials = new Credentials();
    public String operator_id;
    public String journey_date;
    public String service_id;
    public String designation_id;
    public String singleLady;
    public String concessionId;
    public String layoutId;
    public String Source_ID;
    public String seatFareList;
    public String boardingPointID;
    public String droppingPointID;


    public String startStation;
    public String startTime;
    public String endStation;
    public String endTime;

    public BusSeatingLayoutRequest() {
    }


    protected BusSeatingLayoutRequest(Parcel in) {
        Credentials = in.readParcelable(angoothape.wallet.di.JSONdi.restRequest.Credentials.class.getClassLoader());
        operator_id = in.readString();
        journey_date = in.readString();
        service_id = in.readString();
        designation_id = in.readString();
        singleLady = in.readString();
        concessionId = in.readString();
        layoutId = in.readString();
        Source_ID = in.readString();
        seatFareList = in.readString();
        boardingPointID = in.readString();
        droppingPointID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Credentials, flags);
        dest.writeString(operator_id);
        dest.writeString(journey_date);
        dest.writeString(service_id);
        dest.writeString(designation_id);
        dest.writeString(singleLady);
        dest.writeString(concessionId);
        dest.writeString(layoutId);
        dest.writeString(Source_ID);
        dest.writeString(seatFareList);
        dest.writeString(boardingPointID);
        dest.writeString(droppingPointID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BusSeatingLayoutRequest> CREATOR = new Creator<BusSeatingLayoutRequest>() {
        @Override
        public BusSeatingLayoutRequest createFromParcel(Parcel in) {
            return new BusSeatingLayoutRequest(in);
        }

        @Override
        public BusSeatingLayoutRequest[] newArray(int size) {
            return new BusSeatingLayoutRequest[size];
        }
    };
}
