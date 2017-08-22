package tamu.themetry2;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LevelSelectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LevelSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LevelSelectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LevelSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LevelSelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LevelSelectFragment newInstance(String param1, String param2) {
        LevelSelectFragment fragment = new LevelSelectFragment();
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
        View v = inflater.inflate(R.layout.fragment_level_select, container, false);
        ImageView leftWhiteBlock = (ImageView) v.findViewById(R.id.leftWhiteBlock);
        ImageView rightWhiteBlock = (ImageView) v.findViewById(R.id.rightWhiteBlock);
        if(Integer.parseInt(mParam1) == 0){
            leftWhiteBlock.setVisibility(View.INVISIBLE);
        }
        if(Integer.parseInt(mParam1) == ChapterDataSingleton.getSync(this.getContext()).getListOfMissionData().size() - 1){
            rightWhiteBlock.setVisibility(View.INVISIBLE);
        }
        TextView numberOfRecordings = (TextView) v.findViewById(R.id.numberOfRecordings);
        int currentRecordingPlace = ChapterDataSingleton.getSync(this.getContext()).getListOfMissionData().get(Integer.parseInt(mParam1)).getFileNameDigit() - 1;
        //numberOfRecordings.setText("" + ChapterDataSingleton.getSync(this.getContext()).getNumberOfRecordings(currentRecordingPlace));

        int numberRecordings = ChapterDataSingleton.getSync(this.getContext()).getNumberOfRecordings(currentRecordingPlace);
        //setting stars
        if(numberRecordings > 0){
            ImageView leftStar = (ImageView) v.findViewById(R.id.starLeft);
            leftStar.setImageResource(R.drawable.mic_teal);
        }
        if(numberRecordings > 1){
            ImageView middleStar = (ImageView) v.findViewById(R.id.starMiddle);
            middleStar.setImageResource(R.drawable.mic_teal);
        }
        if(numberRecordings > 2){
            ImageView rightStar = (ImageView) v.findViewById(R.id.starRight);
            rightStar.setImageResource(R.drawable.mic_teal);
        }

        //was told to disable selectLevelScreen for the moment
        //selectLevelScreen(v);
        final ImageButton selectLevel = (ImageButton) v.findViewById(R.id.levelSelectButton);

        TextView messageNumber = (TextView) v.findViewById(R.id.missionNumber);
        String levelNumberCorrected = "" + (Integer.parseInt(mParam1) + 1);
        messageNumber.setText(levelNumberCorrected);

        final ImageButton levelSelectButton = (ImageButton) v.findViewById(R.id.levelSelectButton);
        int prevLevelChecker = Integer.parseInt(mParam1) - 1;
        if(prevLevelChecker == -1 && ChapterDataSingleton.getSync(this.getContext()).getNumberOfRecordings(Integer.parseInt(mParam1)) < 3){ //base case, have to allow entry for the first level at all times
            levelSelectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mParam1Int = Integer.parseInt(mParam1);
                    Intent messagePrompt = new Intent(LevelSelect.missionBoardContext, MessagePrompt.class);
                    messagePrompt.putExtra("missionNumber", mParam1Int);
                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                            0, v.getWidth(), v.getHeight());
                    startActivity(messagePrompt, options.toBundle());
                    getActivity().finish();
                }
            });
        } else if (prevLevelChecker > -1 && ChapterDataSingleton.getSync(this.getContext()).getNumberOfRecordings(prevLevelChecker) > 0 && ChapterDataSingleton.getSync(this.getContext()).getNumberOfRecordings(Integer.parseInt(mParam1)) < 3) {
            levelSelectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mParam1Int = Integer.parseInt(mParam1);
                    Intent messagePrompt = new Intent(LevelSelect.missionBoardContext, MessagePrompt.class);
                    messagePrompt.putExtra("missionNumber", mParam1Int);
                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                            0, v.getWidth(), v.getHeight());
                    startActivity(messagePrompt, options.toBundle());
                    getActivity().finish();
                }
            });
        } else {
            levelSelectButton.setClickable(false);
            levelSelectButton.setEnabled(false);
        }
        if(!ChapterDataSingleton.getSync(this.getContext()).getListOfMissionData().get((Integer.parseInt(mParam1))).getWithRecording()){
            ImageView leftStar = (ImageView) v.findViewById(R.id.starLeft);
            leftStar.setVisibility(View.INVISIBLE);
            ImageView middleStar = (ImageView) v.findViewById(R.id.starMiddle);
            middleStar.setVisibility(View.INVISIBLE);
            ImageView rightStar = (ImageView) v.findViewById(R.id.starRight);
            rightStar.setVisibility(View.INVISIBLE);
        }
        return v;
    }

    public void selectLevelScreen(View v){
        final ImageView opacityFilterSelect = (ImageView) v.findViewById(R.id.opacityFilterSelect);
        final ImageView starSelect = (ImageView) v.findViewById(R.id.starSelect);
        final TextView goalText = (TextView) v.findViewById(R.id.goalTextSelect);
        final TextView instructionsText = (TextView) v.findViewById(R.id.instructionsTextSelect);
        final Button playButton = (Button) v.findViewById(R.id.playButtonSelect);

        final TextView levelText = (TextView) v.findViewById(R.id.levelText);
        final ImageButton levelSelectButton = (ImageButton) v.findViewById(R.id.levelSelectButton);

        final Animation animationFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);

        levelSelectButton.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     opacityFilterSelect.setAnimation(animationFadeIn);
                                                     opacityFilterSelect.setVisibility(View.VISIBLE);
                                                     starSelect.setAnimation(animationFadeIn);
                                                     starSelect.setVisibility(View.VISIBLE);
                                                     goalText.setAnimation(animationFadeIn);
                                                     goalText.setVisibility(View.VISIBLE);
                                                     instructionsText.setAnimation(animationFadeIn);
                                                     instructionsText.setVisibility(View.VISIBLE);
                                                     playButton.setAnimation(animationFadeIn);
                                                     playButton.setVisibility(View.VISIBLE);

                                                     levelText.setAnimation(animationFadeOut);
                                                     levelText.setVisibility(View.INVISIBLE);
                                                     levelSelectButton.setEnabled(false);
                                                 }
                                             }
        );
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
