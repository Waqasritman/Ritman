package angoothape.wallet.interfaces;

import java.util.List;

import angoothape.wallet.di.JSONdi.restResponse.IINListResponse;


public interface InterfaceIINList extends OnMessageInterface {

    void IINList(List<IINListResponse> IINlist);
    void onSelectIINList(IINListResponse SelectIIN);
}
