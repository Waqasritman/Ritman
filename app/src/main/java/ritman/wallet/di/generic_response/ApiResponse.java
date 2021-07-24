package ritman.wallet.di.generic_response;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

public class ApiResponse<T>{

    @SerializedName("ResponseCode")
    public Integer responseCode;
    @SerializedName("Description")
    public String description;
    @SerializedName("data")
    public T data;

    public T getData() {
        return data;
    }

}
