package angoothape.wallet.di.JSONdi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BeneRegistrationOTPErrorResponse {
    @SerializedName("responseCode")
    public String responseCode;

    @SerializedName("responseMsg")
    public String msg;

    @SerializedName("errorResponse")
    public List<Error> errorList;


    public static class Error {
        @SerializedName("errorCode")
        public String errorCode;
        @SerializedName("errorMsg")
        public String errorMessage;
    }
}
