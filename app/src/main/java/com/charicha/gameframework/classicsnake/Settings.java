package com.charicha.gameframework.classicsnake;

import android.os.AsyncTask;

import com.charicha.gameframework.framework.FileIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/**
 * Created by Charicha on 12/25/2017.
 */

public class Settings {

    public static boolean soundEnabled = true;
    public static int[] localHighScores = {100, 60, 40, 30, 20};
    public static String[] localPlayerNames = {"CHARICHA", "CHARICHA", "CHARICHA", "CHARICHA", "CHARICHA"};

    static {
        System.out.println(localPlayerNames[0]);
    }

    //ONLINE LEADER BOARD
    public static int[] onlineHighScores= {100, 60, 40, 30, 20};
    public static String[] onlinePlayerNames = {"CHARICHA", "CHARICHA", "CHARICHA", "CHARICHA", "CHARICHA"};

    public static final String ROOT_URL = "http://charichaleaderboard.000webhostapp.com/snakeleaderboard/api.php?apicall=";
    public static final String  INSERT_URL = ROOT_URL + "insertScore";
    public static final String QUERY_URL = ROOT_URL + "queryScore";

    public static String onlineQueryUserMessage = "";
    public static String onlineInsertUserMessage = "";
    public static String onlineInsertDevMessage = "";
    public static boolean waitingOnlineResponse = false;
    public static boolean onlineError = false;

    public static void loadSettings(FileIO fileIO){
        BufferedReader bReader = null;
        try {
            InputStreamReader isReader = new InputStreamReader(fileIO.readFile("settings.snkclsc"));
            bReader = new BufferedReader(isReader);

            soundEnabled = Boolean.parseBoolean(bReader.readLine());
            for(int i = 0; i < 5; i++){
                localHighScores[i] = Integer.parseInt(bReader.readLine());
                localPlayerNames[i] = bReader.readLine();
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        } catch(NumberFormatException nfe){
            System.err.println("Couldn't parse the scores");
            nfe.printStackTrace();
        } finally {
            if(bReader != null){
                try {
                    bReader.close();
                } catch(IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
                }

    public static void saveSettings(FileIO fileIO){
        BufferedWriter bWriter = null;
        try {
            OutputStreamWriter osWriter = new OutputStreamWriter(fileIO.writeFile("settings.snkclsc"));
            bWriter = new BufferedWriter(osWriter);

            bWriter.write(Boolean.toString(soundEnabled));
            bWriter.newLine();
            for(int i = 0; i < 5; i++){
                bWriter.write(Integer.toString(localHighScores[i]));
                bWriter.newLine();
                bWriter.write(localPlayerNames[i]);
                bWriter.newLine();
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        } finally {
            if(bWriter != null){
                try {
                    bWriter.close();
                } catch(IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
    }

    public static void addLocalScore(String name, int score){
        for(int i = 0; i < 5; i++){
            if(score > localHighScores[i]){
                for(int k = 4; k > i; k--){
                    localHighScores[k] = localHighScores[k - 1];
                }
                localHighScores[i] = score;
                localPlayerNames[i] = name;
                break;
            }
        }
    }

    public static void insertOnlineScore(String name, int score){
        HashMap<String, String> scoreHashMap = new HashMap<>();
        scoreHashMap.put("name", name);
        scoreHashMap.put("score", score + "");

        class InsertScore extends AsyncTask<HashMap<String, String>, Void, String>{
            public InsertScore() {
                super();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                onlineInsertUserMessage = "Processing..";
                waitingOnlineResponse = true;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject response = new JSONObject(s);
                    onlineError = response.getBoolean("error");
                    if (!onlineError) {
                        onlineInsertUserMessage = "Your scores are submitted!";
                    } else {
                        onlineInsertUserMessage = "Failed to submit!";
                    }
                } catch(JSONException je){
                    je.printStackTrace();
                    onlineInsertUserMessage = "Failed to submit!";
                    onlineError = true;
                }
                waitingOnlineResponse = false;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected String doInBackground(HashMap<String, String>[] hashMaps) {
                return new RequestHandler().handleConnection(INSERT_URL, RequestHandler.getPostParams(hashMaps[0]));
            }
        }
        new InsertScore().execute(scoreHashMap);
    }

    public static void queryOnlineScore(){
        class QueryScore extends AsyncTask<Void, Void, String> {
            public QueryScore() {
                super();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                onlineQueryUserMessage = "Loading.";
            }

            @Override
            protected void onPostExecute(String queryResult) {
                super.onPostExecute(queryResult);
                if(queryResult.isEmpty()){
                    onlineQueryUserMessage = "Unable to load!";
                } else {
                    try {
                        JSONArray allScores = new JSONObject(queryResult).getJSONArray("allscores");
                        int len;
                        if(allScores.length() > 5){
                            len = 5;
                        } else {
                            len = allScores.length();
                            for(int i = len; i < 5; i++){
                                onlineHighScores[i] = 0;
                                onlinePlayerNames[i] = "Charicha";
                            }
                        }
                        if(len == 0){
                            onlineQueryUserMessage = "Noone has the highscores yet!";
                        } else {
                            onlineQueryUserMessage = "Loaded.";
                        }
                        for(int i = 0; i < len; i++){
                            JSONObject score = allScores.getJSONObject(i);
                            onlinePlayerNames[i] = score.getString("name");
                            onlineHighScores[i] = score.getInt("score");
                        }
                    } catch(JSONException jse){
                        jse.printStackTrace();
                        onlineQueryUserMessage = "Unable to load!";
                    }
                }

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
                onlineQueryUserMessage = "Loading...";
            }

            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
                onlineQueryUserMessage = "Canceled...";
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                return requestHandler.handleConnection(QUERY_URL, "");
            }
        }
        new QueryScore().execute();
    }



}
