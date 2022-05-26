package angoothape.wallet.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * class will return dataand time objects
 *
 * @author waqas
 * @since 05-05-2019
 */

public class DateAndTime {

    /**
     * get parameter form view datepickerdialog
     * and return the date result with specfic format that we apply
     *
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     * @return date
     */


    public static String getLedgerFormat(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.set(year, monthOfYear, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return format.format(calendar.getTime());
    }

    public static String getBusFormat(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.set(year, monthOfYear, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        return format.format(calendar.getTime());
    }


    public static String getBusFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return format.format(date.getTime());
    }

    public static String setDateFormat(int year, int monthOfYear, int dayOfMonth) {


        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.set(year, monthOfYear, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        return format.format(calendar.getTime());
    }

    public static String setDateFormat(int year, int monthOfYear, int dayOfMonth , String pattern) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.set(year, monthOfYear, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return format.format(calendar.getTime());
    }

    public static String getForgotPinFormat(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.set(year, monthOfYear, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        return format.format(calendar.getTime());
    }


    /**
     * return the current today date
     *
     * @return stirng date
     */

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        return DateAndTime.getLedgerFormat(Year, Month, Day);
    }


    /**
     * return the one month ago date
     *
     * @return date
     */

    public static String getLastMothDate() {
        Calendar calendar = Calendar.getInstance();

        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);

        return DateAndTime.getLedgerFormat(Year, Month - 1, Day);
    }


    public static String getDateToddMMyyy(String data) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(data);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String getTimeFormat(String time) {
        String inputPattern = "HH:mm:ss";
        String outputPattern = "h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}