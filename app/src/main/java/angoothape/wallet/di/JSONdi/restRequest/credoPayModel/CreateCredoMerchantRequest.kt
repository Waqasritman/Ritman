package angoothape.wallet.di.JSONdi.restRequest.credoPayModel

import angoothape.wallet.di.JSONdi.restRequest.Cred

class CreateCredoMerchantRequest {
    var Credentials = Cred()

    var company_legal_name = CreateMerchantRepo.company_legal_name
    var company_brand_name = CreateMerchantRepo.company_brand_name
    var company_registered_address = CreateMerchantRepo.company_registered_address
    var company_registered_pincode = CreateMerchantRepo.company_registered_pincode
    var company_pan = CreateMerchantRepo.company_pan
    var company_business_type = CreateMerchantRepo.company_business_type
    var company_established_year = CreateMerchantRepo.company_established_year
    var company_business_nature = CreateMerchantRepo.company_business_nature
    var company_contact_name = CreateMerchantRepo.company_contact_name
    var company_contact_mobile = CreateMerchantRepo.company_contact_mobile
    var company_contact_email = CreateMerchantRepo.company_contact_email
    var international_card_accepted = CreateMerchantRepo.international_card_accepted
    var company_registration_no  = CreateMerchantRepo.company_registration_no
    var merchant_category_code_id = CreateMerchantRepo.merchant_category_code_id
    var merchant_type = "5e97e5ce9f535a7fc56dc12a"
    var merchant_title = CreateMerchantRepo.merchant_title
    var merchant_pincode = CreateMerchantRepo.merchant_pincode
    var merchant_has_own_house = CreateMerchantRepo.merchant_has_own_house
    var merchant_dob = CreateMerchantRepo.merchant_dob
    var merchant_txn_sets = CreateMerchantRepo.merchant_txn_sets

    var merchant_document_pan_card = CreateMerchantRepo.merchant_document_pan_card
    var merchant_document_aadhar_card = CreateMerchantRepo.merchant_document_aadhar_card
    var merchant_document_cancelled_cheque = CreateMerchantRepo.merchant_document_cancelled_cheque
}