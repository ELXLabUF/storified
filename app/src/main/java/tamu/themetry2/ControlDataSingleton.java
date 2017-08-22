package tamu.themetry2;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by colin on 7/3/2016.
 */
public class ControlDataSingleton {

    private static ControlDataSingleton instance = null;
    private static Context context;
    private String teacherID;
    private String unitSelected;
    private List<Integer> promptOrder = new ArrayList<>();

    protected ControlDataSingleton(Context context) {
        this.context = context.getApplicationContext();
        readInControlFile();
    }

    //standard singleton, creates a new instance if it doesn't exist, otherwise return the old iteration
    public static synchronized ControlDataSingleton getSync(Context context) {
        if(instance == null){
            instance = new ControlDataSingleton(context);
        } else {
            //instance = new ChapterDataSingleton(context);
            instance.context = context.getApplicationContext();
        }
        return instance;
    }

    public void readInControlFile() {
        try {
            List<String> mLines = new ArrayList<>();
            String pathname = "master_control.txt";
            AssetManager am = context.getAssets();
            InputStream is = am.open(pathname);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            FileOutputStream fos = context.openFileOutput(pathname, Context.MODE_PRIVATE);
            //read in the lines from the text file
            while ((line = reader.readLine()) != null) {
                mLines.add(line);
                fos.write((line + "\n").getBytes()); //maybe this one will work
            }
            reader.close();
            fos.close();
            assignValues(mLines);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void assignValues(List<String> mLines){
        for (int i = 0; i < mLines.size(); i++) {
            int startIndex = mLines.get(i).indexOf(":");
            String trimmedString = mLines.get(i).substring(startIndex + 1, mLines.get(i).length()).trim();
            if (i == 0) {
                teacherID = trimmedString;
            } else if (i == 1) {
                unitSelected = trimmedString;
            } else if (i == 2) {
                List<String> splitOrderString = Arrays.asList(trimmedString.split(","));
                for(int j = 0; j < splitOrderString.size(); j++){
                    promptOrder.add(Integer.parseInt(splitOrderString.get(j).trim()));
                }
            }
        }
    }

    public String getTeacherID(){
        return teacherID;
    }

    public String getUnitSelected(){
        return unitSelected;
    }

    public List<Integer> getPromptOrder(){
        return promptOrder;
    }
}