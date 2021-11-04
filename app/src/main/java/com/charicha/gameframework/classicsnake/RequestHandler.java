package com.charicha.gameframework.classicsnake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Charicha on 1/6/2018.
 */

public class RequestHandler {


    public String handleConnection(String urlString, String postParams){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection hConnection = (HttpURLConnection) url.openConnection();
            hConnection.setConnectTimeout(15000);
            hConnection.setReadTimeout(15000);
            hConnection.setDoOutput(true);
            hConnection.setDoInput(true);
            hConnection.setRequestMethod("POST");

            //writing to http request body
            OutputStream osStream = hConnection.getOutputStream();
            osStream.write(postParams.getBytes());

            int responseCode = hConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader bfReader = new BufferedReader(new InputStreamReader(hConnection.getInputStream()));
                String line = "";

                while((line = bfReader.readLine()) != null){
                    stringBuilder.append(line);
                }
            }

        } catch(MalformedURLException mue){
            mue.printStackTrace();
        } catch(ProtocolException pe){
            pe.printStackTrace();
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String getPostParams(HashMap<String, String> hashMap){
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : hashMap.entrySet()){
            if(first){
                first = false;
            } else {
                result.append("&");
            }
            try{
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch(UnsupportedEncodingException uee){
                uee.printStackTrace();
            }
        }
        return result.toString();
    }

}
