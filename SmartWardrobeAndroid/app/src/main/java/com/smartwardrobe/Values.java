package com.smartwardrobe;

import android.os.Environment;

/**
 * Created by leechunhoe on 13/9/15.
 */
public class Values
{
    public static final String BASE_URL = "http://smartwardrobe.azurewebsites.net/api/";

    public static final String URL_CLOTH = BASE_URL + "clothes/";

    public static final String WEB_REQUEST_GET_ALL_CLOTHES = "WEB_REQUEST_GET_ALL_CLOTHES";
    public static final String WEB_REQUEST_GET_CLOTH_DETAILS = "WEB_REQUEST_GET_CLOTH_DETAILS";
    public static final String WEB_REQUEST_UPDATE_CLOTH_INFO = "WEB_REQUEST_UPDATE_CLOTH_INFO";

    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";
    public static final String REQUEST_METHOD_PUT = "PUT";
    public static final String REQUEST_METHOD_DELETE = "DELETE";

    // For GCM
    public static final String PROJECT_ID = "smartwardrobe-efb06";
    public static final String PROJECT_NAME = "Smartwardrobe";
    public static final String PROJECT_NUMBER = "752389406740";

    public static final String TAG_DEBUG = "debug";

    public static final String APP_ROOT = Environment.getExternalStorageDirectory().toString() + "/SmartWardrobe/";

    public static String getWebRequestUrl(String webRequest)
    {
        if (webRequest == null)
        {
            return "";
        }

        if (webRequest.equals(WEB_REQUEST_GET_ALL_CLOTHES))
        {
            return URL_CLOTH;
        }
        else if (webRequest.equals(WEB_REQUEST_GET_CLOTH_DETAILS))
        {
            return URL_CLOTH;
        }
        else if (webRequest.equals(WEB_REQUEST_UPDATE_CLOTH_INFO))
        {
            // TODO: Put ID
            return URL_CLOTH;
        }

        return "";
    }

    public static String getWebRequestMethod(String webRequest)
    {
        if (webRequest == null)
        {
            return "";
        }

        if (webRequest.equals(WEB_REQUEST_GET_ALL_CLOTHES))
        {
            return REQUEST_METHOD_GET;
        }
        else if (webRequest.equals(WEB_REQUEST_GET_CLOTH_DETAILS))
        {
            return REQUEST_METHOD_GET;
        }
        else if (webRequest.equals(WEB_REQUEST_UPDATE_CLOTH_INFO))
        {
            return REQUEST_METHOD_POST;
        }

        return "";
    }
}