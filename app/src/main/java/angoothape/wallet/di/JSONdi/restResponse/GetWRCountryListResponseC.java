package angoothape.wallet.di.JSONdi.restResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import angoothape.wallet.di.generic_response.ApiResponse;

public class GetWRCountryListResponseC extends ApiResponse<List<GetWRCountryListResponseC>> {

    @SerializedName("Country_Code")
    private String Country_Code;

    @SerializedName("Country_Type")
    private Integer Country_Type;

    @SerializedName("Country_Name")
    private String Country_Name;

    @SerializedName("Country_Currency")
    private String Country_Currency;

    @SerializedName("Currency_ShortName")
    private String Currency_ShortName;

    @SerializedName("Country_CurrencyAbbreviation")
    private String Country_CurrencyAbbreviation;

    @SerializedName("ISDCode")
    private String ISDCode;

    @SerializedName("URL")
    private String URL;

    public String getCountry_Code() {
        return Country_Code;
    }

    public void setCountry_Code(String country_Code) {
        Country_Code = country_Code;
    }

    public Integer getCountry_Type() {
        return Country_Type;
    }

    public void setCountry_Type(Integer country_Type) {
        Country_Type = country_Type;
    }

    public String getCountry_Name() {
        return Country_Name;
    }

    public void setCountry_Name(String country_Name) {
        Country_Name = country_Name;
    }

    public String getCountry_Currency() {
        return Country_Currency;
    }

    public void setCountry_Currency(String country_Currency) {
        Country_Currency = country_Currency;
    }

    public String getCurrency_ShortName() {
        return Currency_ShortName;
    }

    public void setCurrency_ShortName(String currency_ShortName) {
        Currency_ShortName = currency_ShortName;
    }

    public String getCountry_CurrencyAbbreviation() {
        return Country_CurrencyAbbreviation;
    }

    public void setCountry_CurrencyAbbreviation(String country_CurrencyAbbreviation) {
        Country_CurrencyAbbreviation = country_CurrencyAbbreviation;
    }

    public String getISDCode() {
        return ISDCode;
    }

    public void setISDCode(String ISDCode) {
        this.ISDCode = ISDCode;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
