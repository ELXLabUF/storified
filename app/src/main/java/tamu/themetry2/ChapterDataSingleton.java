package tamu.themetry2;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

/**
 * Created by colin on 6/14/2016.
 */
public class ChapterDataSingleton {

    private static ChapterDataSingleton instance = null;
    private static Context context;
    private static ArrayList<ChapterData> listOfMissionData = new ArrayList<ChapterData>();
    private static ArrayList<Integer> listOfNumberRecordings = new ArrayList<Integer>();
    private static String[] fileList;
    private static Queue<Integer> promptOrder;
    private static String theme = "spy";
    private static boolean filesLoaded = false;

    protected ChapterDataSingleton(Context context){
        this.context = context.getApplicationContext();
        //listFiles(this.context);
        //testing calling this from setTheme, have to call setTheme now to fully initialize the singleton
    }

    //standard singleton, creates a new instance if it doesn't exist, otherwise return the old iteration
    public static synchronized ChapterDataSingleton getSync(Context context){
        if(instance == null){
            instance = new ChapterDataSingleton(context);
        } else {
            instance.context = context.getApplicationContext();
        }
        return instance;
    }

    public static synchronized void forceReload(Context context){
        instance = new ChapterDataSingleton(context);
    }

    //loads the missionData into the singleton
    private void loadListMissionData(ArrayList<Integer> missionNumberList){
        listOfMissionData.clear(); //just in case it's not empty
        for(int i = 0; i < missionNumberList.size(); i++){
            ChapterData md = new ChapterData(context);
            listOfMissionData.add(md.openData(missionNumberList.get(i), theme));
        }
        initializeListOfNumberRecordings(listOfMissionData.size());
        readNumberRecordingsFromFile();
    }

    public void initializeListOfNumberRecordings(int numberOfChapters){
        listOfNumberRecordings = new ArrayList<>(Collections.nCopies(numberOfChapters,0));
    }

    public boolean canPlayAudio(Context context) {
        PackageManager packageManager = context.getPackageManager();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        // Check whether the device has a speaker.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check FEATURE_AUDIO_OUTPUT to guard against false positives.
            if (!packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
                return false;
            }

            AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (AudioDeviceInfo device : devices) {
                if (device.getType() == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
                    return true;
                }
            }
        }
        return false;
    }

    //THIS THING IS NOT WORKING, FIX TOMORROW RIP JULY 4TH
    public void loadPromptOrder() throws IOException{
        ArrayList<ChapterData> reorganizedChapterData = getByUnit(ControlDataSingleton.getSync(context).getUnitSelected());
        reorganizedChapterData = reorganizeByOrder(reorganizedChapterData, ControlDataSingleton.getSync(context).getPromptOrder());
        Queue<Integer> tempQueue = new LinkedList<>();
        for(int i = 0; i < reorganizedChapterData.size(); i++){
            if(!Boolean.parseBoolean(reorganizedChapterData.get(i).getIsMissionCompleted())) {
                tempQueue.add(convertFileNumberByUnit(reorganizedChapterData.get(i).getFileNameDigit()));
            }
        }
        promptOrder = tempQueue;
    }

    //call this whenever the text files are changed, this function should work
    public void updateListMissionData(){
        ArrayList<ChapterData> updatedList = new ArrayList<>();
        for(int i = 0; i < listOfMissionData.size(); i++){
            ChapterData md = new ChapterData(context);
            int underscorePosition = listOfMissionData.get(i).getFileName().indexOf("_");
            String trimmedString = listOfMissionData.get(i).getFileName().substring(0, underscorePosition).trim();
            updatedList.add(md.openData(Integer.parseInt(trimmedString), theme));
        }
        listOfMissionData = updatedList;
    }

    public ArrayList<ChapterData> get(){
        return listOfMissionData;
    }

    public ChapterData get(int i){
        return listOfMissionData.get(i);
    }

    public int size(){
        return listOfMissionData.size();
    }

    //returns data sorted by type of Unit
    public ArrayList<ChapterData> getByUnit(String unit){
        ArrayList<ChapterData> listByUnit = new ArrayList<>();
        for(int i = 0; i < listOfMissionData.size(); i++){
            if(listOfMissionData.get(i).getNameOfUnit().equals(unit)){
                listByUnit.add(listOfMissionData.get(i));
            }
        }
        return listByUnit;
    }

    //ControlDataSingleton.getSync(context).getPromptOrder() in orderList
    public ArrayList<ChapterData> reorganizeByOrder(ArrayList<ChapterData> chapterDataList, List<Integer> orderList){
        ArrayList<ChapterData> tempChapterDataList = new ArrayList<>();
        for(int i = 0; i < orderList.size(); i++) {
            for (int j = 0; j < chapterDataList.size(); j++) {
                if (chapterDataList.get(j).getFileNameDigit().equals(orderList.get(i))) {
                    tempChapterDataList.add(chapterDataList.get(j));
                }
            }
        }
        return tempChapterDataList;
    }

    //returns the number of unique units located within all of the text files loaded
    public ArrayList<String> getUnits(){
        ArrayList<String> uniqueUnits = new ArrayList<>();
        for(int i = 0; i < listOfMissionData.size(); i++){
            if(i == 0){
                uniqueUnits.add(listOfMissionData.get(i).getNameOfUnit());
            } else {
                boolean doesNotExist = true;
                for(int j = 0; j < uniqueUnits.size(); j++){
                    if(uniqueUnits.get(j).equals(listOfMissionData.get(i).getNameOfUnit())){
                        doesNotExist = false;
                    }
                }
                if(doesNotExist){
                    uniqueUnits.add(listOfMissionData.get(i).getNameOfUnit());
                }
            }
        }
        return uniqueUnits;
    }

    public Queue<Integer> getPromptOrder(){
        return promptOrder;
    }

    //loads in the data into the singleton if it did not exist
    private void listFiles(Context context){
        try {
            AssetManager am = context.getAssets();
            ArrayList<Integer> missionNumberList = new ArrayList<Integer>();
            fileList = am.list("");
            String currentTag = "_" + theme + "_chapter.txt";
            for (String aFileList : fileList) {
                if (aFileList.contains(currentTag)) {
                    missionNumberList.add(Integer.parseInt(aFileList.substring(0, aFileList.length() - currentTag.length())));
                }
            }

            //SharedPreferences preferences = context.getSharedPreferences("prefName", context.MODE_PRIVATE);
            //SharedPreferences.Editor edit = preferences.edit();
            //edit.putBoolean("filesLoaded65", true);
            //edit.apply();
            loadListMissionData(missionNumberList);
            loadPromptOrder();
            //readNumberRecordingsFromFile(); shouldn't need this
        } catch (Exception e){
            System.err.println("IO Exception: " + e.getMessage());
        }
    }

    public Integer convertFileNumberByUnit(Integer valueToConvert){
        for(int i = 0; i < listOfMissionData.size(); i++){
            if(valueToConvert.equals(listOfMissionData.get(i).getFileNameDigit())){
                return i;
            }
        }
        return -1; //just in case it gets through, should never
    }

    public void setTheme(String themeName){
        theme = themeName.toLowerCase();
        //setFilesLoaded(false);
        listFiles(this.context);
    }

    public String getTheme(){
        return theme;
    }

    public void setFilesLoaded(Boolean bool){
        filesLoaded = bool;
    }

    public boolean getFilesLoaded(){
        return filesLoaded;
    }

    public ArrayList<ChapterData> getListOfMissionData(){
        return listOfMissionData;
    }

    public void incrementListofNumberRecordings(Integer missionNumber){
        listOfNumberRecordings.ensureCapacity(missionNumber + 1);
        listOfNumberRecordings.set(missionNumber, listOfNumberRecordings.get(missionNumber) + 1);
        //how in the world is there an index out of bound exception on an arraylist ?????
    }

    public ArrayList<Integer> getListOfNumberRecordings(){
        return listOfNumberRecordings;
    }

    public int getNumberOfRecordings(int i){
        return listOfNumberRecordings.get(i);
    }

    public int totalNumberOfRecordings(){
        int total = 0;
        for(int i = 0; i < listOfNumberRecordings.size(); i++){
            total += listOfNumberRecordings.get(i);
        }
        return total;
    }

    public void clearFile(String filename){
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + filename;
            PrintWriter pw = new PrintWriter(new FileOutputStream(path));
            pw.print("");
            pw.close();
        } catch (Exception e){
            System.err.println("File Not Found Exception: " + e.getMessage());
        }
    }

    public void clearNumberRecordingsFile(){
        try {
            System.out.println("ARE WE ATTEMPTING TO DELETE THE INFOS");
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "number_recordings.txt";
            PrintWriter pw = new PrintWriter(new FileOutputStream(path));
            for (int i = 0; i < listOfNumberRecordings.size(); i++) {
                pw.println(0);
            }
            pw.close();
        } catch (Exception e){
            System.err.println("File Not Found Exception: " + e.getMessage());
        }
    }

    public void writeNumberRecordingsToFile(){
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "number_recordings.txt";
            //String path = "number_recordings.txt";
            System.out.println("Writing data to the audio data file:");
            PrintWriter pw = new PrintWriter(new FileOutputStream(path));
            for (int i = 0; i < listOfNumberRecordings.size(); i++) {
                pw.println(listOfNumberRecordings.get(i));
                System.out.println("listOfNumberRecordings.get(" + i + "): " + listOfNumberRecordings.get(i));
            }
            pw.close();
        } catch (Exception e){
            System.err.println("File Not Found Exception: " + e.getMessage());
        }
    }

    public void readNumberRecordingsFromFile() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "number_recordings.txt";
        //String path = "number_recordings.txt";
        List<String> mLines = new ArrayList<>();
        SharedPreferences settings = context.getSharedPreferences("prefName", context.MODE_PRIVATE);
        Boolean numberFilesLoaded = settings.getBoolean("filesLoaded65", false); //does this need to be false or true?
        Boolean systemPermissionsGranted = settings.getBoolean("systemPermissionsGiven", false);
        if(numberFilesLoaded){
            try{
                System.out.println("attempting to read in from internal file for number_recordings");
                File audioDataFile = new File(copyInternal2External(path));
                FileInputStream inputStream = new FileInputStream(audioDataFile);
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
                String read;

                int counter = 0;
                while((read = inputReader.readLine()) != null){
                    System.out.println("what did I read in for number_recordings?: " + read);
                    mLines.add(read);
                    listOfNumberRecordings.set(counter, Integer.parseInt(read));
                    counter++;
                }
                inputReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(systemPermissionsGranted){

            //clear all of the text files
            clearNumberRecordingsFile();
            clearFile("timestamps.txt");
            clearFile("enterapp.txt");
            clearFile("levelselect.txt");
            clearFile("confirmrecording.txt");
            clearFile("replayrecording.txt");

            //reset the values in shared preferences
            SharedPreferences preferences = context.getSharedPreferences("prefName", context.MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("filesLoaded65", true);
            edit.putString("startedFrom", "general");
            edit.apply();
        }

        System.out.println("mLines.size(): " + mLines.size());

        //based off of which line, determine which value is which
        for (int i = 0; i < mLines.size(); i++) {
            int value = Integer.parseInt(mLines.get(i));
            listOfNumberRecordings.set(i, value);
        }

        System.out.println("before the fall");

        for(int i = 0; i < listOfNumberRecordings.size(); i++){
            System.out.println("listOfNumberRecordings.get(" + i + "): " + listOfNumberRecordings.get(i));
        }

        System.out.println("after the fall");

    }

    public String copyInternal2External(String source) throws IOException {
        String destination = context.getExternalCacheDir() + "/" + UUID.randomUUID();
        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(destination);

        byte[] buf = new byte[1024];
        int len;
        int total = 0;
        while ((len = fis.read(buf)) > 0) {
            total += len;
            fos.write(buf, 0, len);
            if (total > 20 * 1024) {
                fos.flush();
            }
        }
        fis.close();
        fos.close();
        return destination;
    }

    public void writeToFile(String directory, String filename, String data){
        File out;
        OutputStreamWriter outStreamWriter = null;
        FileOutputStream outStream = null;

        out = new File(new File(directory), filename);
        try {
            if (out.exists() == false) {
                out.createNewFile();
            }

            outStream = new FileOutputStream(out, true);
            outStreamWriter = new OutputStreamWriter(outStream);

            outStreamWriter.append(data);
            outStreamWriter.flush();
        } catch (Exception e){
            System.out.println(e);
        }
    }
}