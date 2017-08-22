package tamu.themetry2;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewMainMenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewMainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewMainMenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NewMainMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewMainMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewMainMenuFragment newInstance(String param1, String param2) {
        NewMainMenuFragment fragment = new NewMainMenuFragment();
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
        View v = inflater.inflate(R.layout.fragment_new_main_menu, container, false);
        TextView menuSelectText = (TextView) v.findViewById(R.id.menuSelectText);
        ImageButton menuSelectButton = (ImageButton) v.findViewById(R.id.menuSelectButton);
        switch(mParam1){
            case "current story":
                menuSelectText.setText("Episodes");
                menuSelectButton.setBackground(getResources().getDrawable(R.drawable.menu_story));
                menuSelectButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent levelSelect = new Intent(NewMainMenu.mContext, LevelSelect.class);
                        levelSelect.putExtra("startedFrom", "missionBoard");

                        SharedPreferences preferences = NewMainMenu.mContext.getSharedPreferences("prefName", NewMainMenu.mContext.MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("levelType", "currentLevel");
                        edit.apply();

                        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                                0, v.getWidth(), v.getHeight());
                        startActivityForResult(levelSelect, 1, options.toBundle());
                    }
                });
                break;
            case "past stories":
                menuSelectText.setText("Past Stories");
                menuSelectButton.setBackground(getResources().getDrawable(R.drawable.menu_story));
                menuSelectButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent levelSelect = new Intent(NewMainMenu.mContext, LevelSelect.class);
                        levelSelect.putExtra("startedFrom", "missionBoard");

                        SharedPreferences preferences = NewMainMenu.mContext.getSharedPreferences("prefName", NewMainMenu.mContext.MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("levelType", "pastLevels");
                        edit.apply();

                        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                                0, v.getWidth(), v.getHeight());
                        startActivityForResult(levelSelect, 1, options.toBundle());
                    }
                });
                break;
            case "review":
                menuSelectText.setText("Vocabulary");
                menuSelectButton.setBackground(getResources().getDrawable(R.drawable.menu_review));
                menuSelectButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(NewMainMenu.mContext, TopicReview.class);
                        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                                0, v.getWidth(), v.getHeight());
                        startActivity(intent, options.toBundle());
                    }
                });
                break;
            case "recordings":
                menuSelectText.setText("Recordings");
                menuSelectButton.setBackground(getResources().getDrawable(R.drawable.menu_recordings));
                menuSelectButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(NewMainMenu.mContext, ChapterCollection.class);
                        intent.putExtra("unitSelected", NewMainMenu.unitSelected);
                        intent.putExtra("updateData", true);
                        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                                0, v.getWidth(), v.getHeight());
                        startActivity(intent, options.toBundle());
                    }
                });
                break;
            /*
            case "sync":
                menuSelectText.setText("Send Audio");
                menuSelectButton.setBackground(getResources().getDrawable(R.drawable.sync_button));
                menuSelectButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //ADD SYNC FUNCTIONALITY HERE
                    }
                });
                break;
            */
        }
        return v;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
