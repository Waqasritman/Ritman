package angoothape.wallet.interfaces

import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.GetApiData
import angoothape.wallet.di.JSONdi.restResponse.credopaymodels.MerchantTerminalResponse

interface OnCredoPayApiDataSelect {
    fun onSelectApiData(data: GetApiData)
    fun onSelectTransactionData(data: List<GetApiData>)
    fun onSelectMerchantTerminal(data: MerchantTerminalResponse)
}