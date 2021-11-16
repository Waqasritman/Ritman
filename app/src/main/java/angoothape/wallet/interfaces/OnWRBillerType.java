package angoothape.wallet.interfaces;

import java.util.List;

import angoothape.wallet.di.JSONdi.restResponse.GetWRBillerTypeResponse;

public interface OnWRBillerType extends OnMessageInterface{
    void onBillerTypeList(List<GetWRBillerTypeResponse> billerTypeList);
    void onBillerTypeSelect(GetWRBillerTypeResponse billerType);
}
