package tamu.themetry2;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChapterCollectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChapterCollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChapterCollectionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton btn;
    private ArrayList<ChapterData> reorganizedChapterData;
    private MediaPlayer mediaPlayer;
    private File audioFile;
    private int iSaved;
    private int calculatedPlacementSaved;

    private OnFragmentInteractionListener mListener;

    public ChapterCollectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChapterCollectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChapterCollectionFragment newInstance(String param1, String param2) {
        ChapterCollectionFragment fragment = new ChapterCollectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chapter_collection, container, false);

        ImageButton playButton = (ImageButton) v.findViewById(R.id.missionImage);
        playButton.setBackground(getResources().getDrawable(R.drawable.play_button));

        int counter = 0;
        int calculatedPlacement = 0;
        int i = 0;
        while(counter <= Integer.parseInt(mParam1)){
            if(counter + ChapterDataSingleton.getSync(this.getContext()).getNumberOfRecordings(i) > Integer.parseInt(mParam1)){
                int savedCounter = counter;
                while(counter < savedCounter + ChapterDataSingleton.getSync(this.getContext()).getNumberOfRecordings(i)) {
                    if (counter == Integer.parseInt(mParam1)) {
                        iSaved = i;
                        calculatedPlacementSaved = calculatedPlacement;
                        ImageView leftWhiteBlock = (ImageView) v.findViewById(R.id.leftWhiteBlock);
                        ImageView rightWhiteBlock = (ImageView) v.findViewById(R.id.rightWhiteBlock);
                        String titleText = ChapterDataSingleton.getSync(this.getContext()).get(i).getNameOfTopic() + "-" + (calculatedPlacement+1); //instead of starting at 0
                        ((TextView) v.findViewById(R.id.missionStatus)).setText(titleText);
                        //display correct recordings right here
                        if(Integer.parseInt(mParam1) == 0){
                            leftWhiteBlock.setVisibility(v.INVISIBLE);
                        }
                        if (Integer.parseInt(mParam1) == ChapterDataSingleton.getSync(this.getContext()).totalNumberOfRecordings() - 1){
                            rightWhiteBlock.setVisibility(v.INVISIBLE);
                        }
                        btn = (ImageButton) v.findViewById(R.id.missionImage);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String currentDateandTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
                                String levelTimestamp = iSaved + "-" + calculatedPlacementSaved + ": " + currentDateandTime + "\n";
                                ChapterDataSingleton.getSync(ChapterCollection.missionBoardContext).writeToFile(Environment.getExternalStorageDirectory().getAbsolutePath(), "replayrecording.txt", levelTimestamp);

                                playAudio(iSaved, calculatedPlacementSaved);
                            }
                        });
                    }
                    counter++;
                    calculatedPlacement++;
                }
            } else {
                counter += ChapterDataSingleton.getSync(this.getContext()).getNumberOfRecordings(i);
            }
            i++;
        }
        return v;
    }

    public void playAudio(int i, int calculatedPlacement){
        //playback code
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            //CURRENT ERROR - no such file or directory
            audioFile = new File(copyInternal2External(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + i + "_chapter_recording_" + calculatedPlacement + ".wav"));
            FileInputStream inputStream = new FileInputStream(audioFile);
            mediaPlayer.setDataSource(inputStream.getFD(), 0, audioFile.length());
            inputStream.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
            endAudioPlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void endAudioPlayback(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.stop();
                mediaPlayer.release();
                audioFile.delete();
            }
        }, mediaPlayer.getDuration());
    }

    public String copyInternal2External(String source) throws IOException {
        String destination = this.getContext().getExternalCacheDir() + "/" + UUID.randomUUID();
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
        //how to clean the cache, NEED TO DO THIS
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(View v) {
        if (mListener != null) {
            mListener.onFragmentInteraction(v, mParam1);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(View v, String missionNumber);
    }
}