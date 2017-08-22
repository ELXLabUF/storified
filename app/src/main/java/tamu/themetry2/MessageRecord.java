package tamu.themetry2;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.wearable.view.WatchViewStub;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
    Screen to record audio. Saves the data in PCM and converts to WAV format.
 */

public class MessageRecord extends Activity {

    private TextView mTextView;
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    private boolean isRecordingButtonPressed = false;
    public static int missionNumber;
    private String startTime;
    private String endTime;
    private static int numberOfRecordings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_record);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
        setContentView(R.layout.rect_activity_message_record);

        loadMissionNumber();
        ImageButton playButton = (ImageButton) findViewById(R.id.recordButton);
        playButton.setBackground(getResources().getDrawable(R.drawable.record_button));
        final TextView textView = (TextView) findViewById(R.id.recordText);
        final ImageButton button = (ImageButton) findViewById(R.id.recordButton);
        final ImageView opacityImage = (ImageView) findViewById(R.id.opacityFilter);
        final Button uploadButton = (Button) findViewById(R.id.uploadRecordingButton);
        final Button discardButton = (Button) findViewById(R.id.discardRecordingButton);
        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        //center text for the current level
        final TextView centerLevel = (TextView) findViewById(R.id.centerLevel);
        final String currentLevel = "Episode " + (missionNumber + 1);
        centerLevel.setTextColor(Color.parseColor("#FFFFFF"));
        centerLevel.setText(currentLevel);

        final String currentRecordings = "Recordings: " + ChapterDataSingleton.getSync(this).getNumberOfRecordings(missionNumber);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setText(currentRecordings);

        opacityImage.setVisibility(View.VISIBLE);

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN && !isRecordingButtonPressed){
                    try {
                        startRecording();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    //Chapter.myVP.setPagingEnabled(false);
                    startTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
                    textView.setTextColor(Color.parseColor("#FF0000"));
                    textView.setText("Recording...");

                    isRecordingButtonPressed = true;
                }
                else if(event.getAction() == MotionEvent.ACTION_DOWN && isRecordingButtonPressed){
                    try {
                        stopRecording();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    //Chapter.myVP.setPagingEnabled(true);
                    endTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
                    textView.setTextColor(Color.parseColor("#FFFFFF"));
                    textView.setText(currentRecordings); //finger was lifted

                    //STARTING FADE IN OF AUDIO SUBMIT MENU
                    button.setEnabled(false);
                    uploadButton.startAnimation(animationFadeIn);
                    uploadButton.setVisibility(v.VISIBLE);
                    discardButton.startAnimation(animationFadeIn);
                    discardButton.setVisibility(v.VISIBLE);
                    uploadButton.setEnabled(true);
                    discardButton.setEnabled(true);
                    //Chapter.myVP.setPagingEnabled(false);
                    isRecordingButtonPressed = false;
                }
                return true;
            }
        });
        discardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {

                    String currentDateandTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
                    String levelTimestamp = (missionNumber + 1) + "-" + numberOfRecordings + ": DECLINED at " + currentDateandTime + "\n";
                    writeToFile(Environment.getExternalStorageDirectory().getAbsolutePath(), "confirmrecording.txt", levelTimestamp);

                    uploadButton.startAnimation(animationFadeOut);
                    uploadButton.setVisibility(v.INVISIBLE);
                    discardButton.startAnimation(animationFadeOut);
                    discardButton.setVisibility(v.INVISIBLE);
                    uploadButton.setEnabled(false);
                    discardButton.setEnabled(false);
                    button.setEnabled(true);
                    deleteRecording();
                }
                return true;
            }
        });
    }

    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format

    private void deleteRecording(){
        File audioFileToDelete = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + missionNumber + "_chapter_recording_" + ChapterDataSingleton.getSync(this).getNumberOfRecordings(missionNumber) + ".pcm");
        audioFileToDelete.delete();
    }

    public void loadMissionNumber() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            missionNumber = bundle.getInt("missionNumber");
        }

        numberOfRecordings = ChapterDataSingleton.getSync(this).getNumberOfRecordings(missionNumber);
    }

    private void startRecording() {

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);


        recorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecord Thread");
        recordingThread.start();
    }

    //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private void writeAudioDataToFile() {
        // Write the output audio in byte
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + missionNumber + "_chapter_recording_" + ChapterDataSingleton.getSync(this).getNumberOfRecordings(missionNumber) + ".pcm";
        short sData[] = new short[BufferElements2Rec];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            recorder.read(sData, 0, BufferElements2Rec);
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        // stops the recording activity
        if (null != recorder) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + missionNumber + "_chapter_recording_" + ChapterDataSingleton.getSync(this).getNumberOfRecordings(missionNumber);
            File oldPcmFile = new File (filePath + ".pcm");
            File newWaveFile = new File(filePath + ".wav");
            try {
                rawToWave(oldPcmFile, newWaveFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            oldPcmFile.delete();
            recordingThread = null;
        }
    }

    private void rawToWave(final File rawFile, final File waveFile) throws IOException {

        byte[] rawData = new byte[(int) rawFile.length()];
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(rawFile));
            input.read(rawData);
        } finally {
            if (input != null) {
                input.close();
            }
        }

        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new FileOutputStream(waveFile));
            // WAVE header
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, RECORDER_SAMPLERATE); // sample rate
            writeInt(output, RECORDER_SAMPLERATE * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }

            output.write(fullyReadFileToBytes(rawFile));
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }

    private void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }

    public void uploadRecording(View v){

        if(ChapterDataSingleton.getSync(this).getNumberOfRecordings(missionNumber) > 2) {
            updateIsCompleted("" + missionNumber);
        }

        ChapterDataSingleton.getSync(this).incrementListofNumberRecordings(missionNumber);
        ChapterDataSingleton.getSync(this).writeNumberRecordingsToFile();

        String currentDateandTime = startTime + " to " + endTime;
        String recordingTimestamp = "" + (missionNumber + 1) + "-" + ChapterDataSingleton.getSync(this).getNumberOfRecordings(missionNumber) + ": " + currentDateandTime + "\n";
        ChapterDataSingleton.getSync(this).writeToFile(Environment.getExternalStorageDirectory().getAbsolutePath(), "timestamps.txt", recordingTimestamp);

        currentDateandTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
        String levelTimestamp = (missionNumber + 1) + "-" + (ChapterDataSingleton.getSync(this).getNumberOfRecordings(missionNumber)) + ": CONFIRMED at " + currentDateandTime + "\n";
        ChapterDataSingleton.getSync(this).writeToFile(Environment.getExternalStorageDirectory().getAbsolutePath(), "confirmrecording.txt", levelTimestamp);

        Intent intent = new Intent(this, MessagePromptEnd.class);
        intent.putExtra("missionNumber", missionNumber);
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                0, v.getWidth(), v.getHeight());
        startActivity(intent, options.toBundle());
        finish();
    }

    //this function works but it doesn't work perfectly, may cause a BUG later down the line
    public void updateIsCompleted(String missionNumber){
        try {
            File f = new File(getFilesDir(), ChapterDataSingleton.getSync(this).get(Integer.parseInt(missionNumber)).getFileName());
            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            raf.seek(0);
            raf.writeUTF("Completed(set this to false initially): true\n");
            raf.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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