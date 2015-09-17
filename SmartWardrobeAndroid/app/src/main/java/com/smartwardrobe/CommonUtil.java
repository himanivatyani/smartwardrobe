package com.smartwardrobe;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by leechunhoe on 12/9/15.
 */
public class CommonUtil
{
    public static void sendWebRequest(String request, final HashMap<String, String> urlParams, Serializable bodyObject, final WebServiceListener webServiceListener)
    {
        String body = null;

        if (bodyObject != null)
        {
            Gson gson = new Gson();
            body = gson.toJson(bodyObject);
        }

        class RequestTask extends AsyncTask<String, String, Object>
        {
            int responseCode = -1;
            String content = null;

            @Override
            protected String doInBackground(String... request)
            {
                try
                {
                    String urlString = Values.getWebRequestUrl(request[0]);
                    String requestMethod = Values.getWebRequestMethod(request[0]);

                    URL url;

                    if (urlParams != null)
                    {
                        if (urlParams.containsKey("rfid"))
                        {
                            url = new URL(urlString + urlParams.get("rfid"));
                        } else
                        {
                            url = new URL(urlString);
                        }
                    } else
                    {
                        url = new URL(urlString);
                    }

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod(requestMethod);
                    connection.setRequestProperty("Content-Type", "application/json");

                    if (requestMethod.equals(Values.REQUEST_METHOD_GET))
                    {

                    } else if (requestMethod.equals(Values.REQUEST_METHOD_POST))
                    {
                        connection.setDoOutput(true);
                        String body = request[1];
                        OutputStreamWriter out = new OutputStreamWriter(
                                connection.getOutputStream());
                        out.write(body);
                        out.close();
                    } else if (requestMethod.equals(Values.REQUEST_METHOD_PUT))
                    {
                        connection.setDoOutput(true);
                        String body = request[1];
                        OutputStreamWriter out = new OutputStreamWriter(
                                connection.getOutputStream());
                        out.write(body);
                        out.close();
                    }

                    connection.connect();

                    responseCode = connection.getResponseCode();

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    br.close();
                    content = sb.toString();

                    //content = connection.getContent();
                } catch (Exception e)
                {
                    Log.d("mailuan", e.getMessage() + "");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object result)
            {
                super.onPostExecute(result);
                webServiceListener.onWebResponse(responseCode, content);
            }
        }

        new RequestTask().execute(request, body);
    }

    public static void getGcmRegId(final Context applicationContext)
    {
        new AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... params)
            {
                String msg = "";

                try
                {
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(applicationContext);

                    String regid = gcm.register(Values.PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM", msg);

                } catch (IOException ex)
                {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg)
            {
//                Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show();
            }
        }.execute(null, null, null);
    }

    /**
     * Save bitmap into path
     *
     * @param bitmap   Bitmap
     * @param path     Local path in SD card, start with /
     * @param fileName Image filename without extension
     */
    public static void saveBitmap(Bitmap bitmap, String path, String fileName)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + path);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        File file = new File(myDir, fileName + ".jpg");

        if (file.exists())
        {
            file.delete();
        }

        try
        {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get random string with specific length
     * @param length String length
     * @return Random string
     */
    public static String getRandomString(int length)
    {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();

        char tempChar;
        for (int i = 0; i < length; i++)
        {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }

        return randomStringBuilder.toString();
    }
}