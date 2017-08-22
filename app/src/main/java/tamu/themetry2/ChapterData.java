package tamu.themetry2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    Class to read in and store file data for each chapter
 */

/**
 * Created by colin on 6/6/2016.
 */
public class ChapterData {

    private Context mContext;
    private String nameOfTopic;
    private String isMissionCompleted;
    private String missionPrompt;
    private String missionPromptSecondary;
    private String nameOfUnit;
    private String fileName;
    private String theme;
    private int numberOfRecordings = 0;
    private boolean withRecording;
    private boolean firstPlay = true;

    public ChapterData(Context context) {
        this.mContext = context;
    }

    //opens the text files given by the server and stores the values within private values
    public ChapterData openData(int missionNumber, String selectedTheme) {
        List<String> mLines = new ArrayList<>();
        String pathname = missionNumber + "_" + selectedTheme + "_chapter.txt";
        SharedPreferences settings = mContext.getSharedPreferences("prefName", mContext.MODE_PRIVATE);
        Boolean filesLoaded = settings.getBoolean("filesLoaded64", false); //does this need to be false or true?
        if(filesLoaded){
            try{
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(mContext.openFileInput(pathname)));
                String read;

                while((read = inputReader.readLine()) != null){
                    mLines.add(read);
                }
                inputReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                System.out.println("We're reading from the assets files.");
                AssetManager am = mContext.getAssets();
                InputStream is = am.open(pathname);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;

                FileOutputStream fos = mContext.openFileOutput(pathname, Context.MODE_PRIVATE);
                //read in the lines from the text file
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    mLines.add(line);
                    fos.write((line + "\n").getBytes()); //maybe this one will work
                }
                reader.close();
                fos.close();
                ChapterDataSingleton.getSync(mContext).setFilesLoaded(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //based off of which line, determine which value is which
        for (int i = 0; i < mLines.size(); i++) {
            int startIndex = mLines.get(i).indexOf(":");
            String trimmedString = mLines.get(i).substring(startIndex + 1, mLines.get(i).length()).trim();
            if (i == 0) {
                isMissionCompleted = trimmedString;
            } else if (i == 1){
                theme = trimmedString;
            } else if (i == 2) {
                nameOfTopic = trimmedString;
            } else if (i == 3) {
                missionPrompt = trimmedString;
            } else if (i == 4) {
                missionPromptSecondary = trimmedString;
            } else if (i == 5) {
                nameOfUnit = trimmedString;
            } else {
                withRecording = Boolean.parseBoolean(trimmedString);
            }
        }
        fileName = pathname;
        return this;
    }

    //list of getter functions for the private values
    public String getNameOfTopic() {
        return nameOfTopic;
    }

    public String getIsMissionCompleted() {
        return isMissionCompleted;
    }

    public String getMissionPrompt() {
        return missionPrompt;
    }

    public String getNameOfUnit() { return nameOfUnit; }

    public String getFileName() { return fileName; }

    public Integer getFileNameDigit() {
        List<String> splitFileName = Arrays.asList(fileName.split("_"));
        return Integer.parseInt(splitFileName.get(0).trim());
    }

    public String getTheme(){
        return theme;
    }

    public int getNumberOfRecordings(){ //counter to see how many recordings we have
        return numberOfRecordings;
    }

    public String getMissionPromptSecondary(){
        return missionPromptSecondary;
    }

    public boolean getWithRecording(){
        return withRecording;
    }

    public boolean isFirstPlay(){
        return firstPlay;
    }

    public void setFirstPlay(boolean bool){
        firstPlay = bool;
    }
}