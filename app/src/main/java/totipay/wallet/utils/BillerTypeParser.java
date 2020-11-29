package totipay.wallet.utils;

import java.util.ArrayList;
import java.util.List;

import totipay.wallet.di.ResponseHelper.WRBillerTypeResponse;

public class BillerTypeParser {

    public static List<WRBillerTypeResponse> getMobileOperators(List<WRBillerTypeResponse> list) {
        List<WRBillerTypeResponse> filteredList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).billerName.contains("Mobile")
             || list.get(i).billerName.contains("mobile")) {
                filteredList.add(list.get(i));
            }
        }
        return filteredList;
    }
}
