package tamu.themetry2;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by colin on 8/7/2016.
 */
public class UnitDataSingleton {

    private static UnitDataSingleton instance = null;
    private Context context;
    private String nameOfUnit;
    private String unitDescription;
    private ArrayList<String> keywordList = new ArrayList<String>();

    protected UnitDataSingleton(Context context) {
        this.context = context.getApplicationContext();
        openData();
    }

    //standard singleton, creates a new instance if it doesn't exist, otherwise return the old iteration
    public static synchronized UnitDataSingleton getSync(Context context) {
        if(instance == null){
            instance = new UnitDataSingleton(context);
        } else {
            instance.context = context.getApplicationContext();
        }
        return instance;
    }

    private UnitDataSingleton openData() {
        List<String> mLines = new ArrayList<>();
        String pathname = "unit_data.txt";
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < mLines.size(); i++) {
            int startIndex = mLines.get(i).indexOf(":");
            String trimmedString = mLines.get(i).substring(startIndex + 1, mLines.get(i).length()).trim();
            if (i == 0) {
                nameOfUnit = trimmedString;
            } else if (i == 1){
                unitDescription = trimmedString;
            } else {
                keywordList.add(trimmedString);
            }
        }
        return this;
    }

    public String getNameOfUnit(){
        return nameOfUnit;
    }

    public String getUnitDescription(){
        return unitDescription;
    }

    public ArrayList<String> getKeywordList(){
        return keywordList;
    }

    public String getKeyword(int i){
        return keywordList.get(i);
    }
}
