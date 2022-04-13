//
// Created by Mohammad Waqas on 4/25/21.

//

#include <jni.h>
#include <string>

extern "C"
jstring
Java_angoothape_wallet_di_JSONdi_retrofit_RestClient_RitpayDomesticlivebaseurl(
        JNIEnv *env,
        jclass clazz) {
    std::string baseURL = "https://payinapi.angoothape.com/";
    return env->NewStringUTF(baseURL.c_str());
}


extern "C"
jstring
Java_angoothape_wallet_di_JSONdi_retrofit_RestClient_RitpayRestlivebaseurl(
        JNIEnv *env,
        jclass clazz) {
    std::string baseURL = "https://restpayinapi.angoothape.com/";
    return env->NewStringUTF(baseURL.c_str());
}



extern "C"
jstring
Java_angoothape_wallet_di_JSONdi_retrofit_KeyHelper_getKey(
        JNIEnv* env,
        jclass clazz) {
    std::string baseURL = "167114|RITMANPAYAPIUSER|BK61MNITO9!";
    return env->NewStringUTF(baseURL.c_str());
}


extern "C"
jstring
Java_angoothape_wallet_di_JSONdi_restRequest_Credentials_getPartnerCode(
        JNIEnv *env,
        jclass clazz) {
    std::string baseURL = "167114";
    return env->NewStringUTF(baseURL.c_str());
}

extern "C"
jstring
Java_angoothape_wallet_di_JSONdi_restRequest_Credentials_getUserName(
        JNIEnv *env,
        jclass clazz) {
    std::string baseURL = "RITMANPAYAPIUSER";
    return env->NewStringUTF(baseURL.c_str());
}

extern "C"
jstring
Java_angoothape_wallet_di_JSONdi_restRequest_Credentials_getPassword(
        JNIEnv *env,
        jclass clazz) {
    std::string baseURL = "BK61MNITO9!";
    return env->NewStringUTF(baseURL.c_str());
}


extern "C"
jstring
Java_angoothape_wallet_di_JSONdi_retrofit_RestClient_IndiaFirsturl(
        JNIEnv *env,
        jclass clazz) {
    std::string baseURL = "https://122.187.215.20/RitpayDomesticRestAPIUAT/";
    return env->NewStringUTF(baseURL.c_str());
}


extern "C"
jstring
Java_angoothape_wallet_di_JSONdi_retrofit_RestClient_EKYC(
        JNIEnv *env,
        jclass clazz) {
    std::string baseURL = "https://restpayinapi.angoothape.com/";
    return env->NewStringUTF(baseURL.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_angoothape_wallet_di_JSONdi_retrofit_RestClient_RitpayDomesticbaseurl(JNIEnv *env, jclass clazz) {
    std::string baseURL = "https://122.187.215.20/RitpayDomesticRestAPIUAT/RPAY/";
    return env->NewStringUTF(baseURL.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_angoothape_wallet_di_JSONdi_retrofit_TrackRestClient_baseKEY(JNIEnv *env, jclass clazz) {
    std::string baseURL = "xqgm3PkZb4RTXmxMmikizvh4d0IDDjELyWW9VtLYETSANVG8H0";
    return env->NewStringUTF(baseURL.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_angoothape_wallet_di_JSONdi_retrofit_TrackRestClient_baseURLJIN(JNIEnv *env, jclass clazz) {
    std::string baseURL = "https://ipapi.co/";
    return env->NewStringUTF(baseURL.c_str());
}