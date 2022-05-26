package angoothape.wallet.di.JSONdi.restRequest.credoPayModel

import angoothape.wallet.di.JSONdi.restRequest.Cred

class ActivateTerminalRequest {
    var Credentials = Cred()

    var terminal_id = ""
    var device_serial_number = ""
    var imei_number = ""
}