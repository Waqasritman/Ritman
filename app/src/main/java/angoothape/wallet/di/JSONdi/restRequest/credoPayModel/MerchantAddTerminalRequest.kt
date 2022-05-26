package angoothape.wallet.di.JSONdi.restRequest.credoPayModel

import angoothape.wallet.di.JSONdi.restRequest.Cred

class MerchantAddTerminalRequest {
    val Credentials = Cred()
    var location = ""
    var address = ""
    var pincode = ""
    var terminal_type = "5f0806e797a3271aa95278a0"
    var device_model = ""
    var device_owned = "M"
}