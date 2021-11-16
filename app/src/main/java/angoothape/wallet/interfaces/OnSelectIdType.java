package angoothape.wallet.interfaces;

import angoothape.wallet.di.XMLdi.ResponseHelper.GetIdTypeResponse;

import java.util.List;

public interface OnSelectIdType extends OnMessageInterface{
    void onSelectIdType(GetIdTypeResponse response);
    void onGetIdTypes(List<GetIdTypeResponse> responses);
}
