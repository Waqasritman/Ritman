package ritman.wallet.utils;

import java.util.ArrayList;
import java.util.List;

import ritman.wallet.di.XMLdi.ResponseHelper.WRBillerTypeResponse;

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
