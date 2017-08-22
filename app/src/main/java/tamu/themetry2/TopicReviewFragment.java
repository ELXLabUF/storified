package tamu.themetry2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TopicReviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TopicReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopicReviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Animation animationFadeIn;
    private Animation animationFadeOut;
    private TextView topicDescription;
    private ImageView opacityFilter;
    private Boolean topicDescriptionVisible = false;

    private OnFragmentInteractionListener mListener;

    public TopicReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopicReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopicReviewFragment newInstance(String param1, String param2) {
        TopicReviewFragment fragment = new TopicReviewFragment();
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

        View v = inflater.inflate(R.layout.fragment_topic_review, container, false);
        TextView tvLabelName = (TextView) v.findViewById(R.id.topicNameInDescription);
        tvLabelName.setText(UnitDataSingleton.getSync(getContext()).getNameOfUnit());

        //finding keywords in the description text and changing their style to bold and changing their color to light blue
        String unitDescriptionString = UnitDataSingleton.getSync(getContext()).getUnitDescription();
        SpannableStringBuilder unitDescription = new SpannableStringBuilder(UnitDataSingleton.getSync(getContext()).getUnitDescription());
        for(int i = 0; i < UnitDataSingleton.getSync(getContext()).getKeywordList().size(); i++){
            if(unitDescriptionString.contains(UnitDataSingleton.getSync(getContext()).getKeyword(i))){
                int startingIndex = unitDescriptionString.indexOf(UnitDataSingleton.getSync(getContext()).getKeyword(i), 0);
                int endingIndex = startingIndex + UnitDataSingleton.getSync(getContext()).getKeyword(i).length();
                unitDescription.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), startingIndex, endingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                unitDescription.setSpan(new ForegroundColorSpan(Color.parseColor("#80CBC4")), startingIndex, endingIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                unitDescription.replace(startingIndex, endingIndex, UnitDataSingleton.getSync(getContext()).getKeyword(i).toUpperCase(Locale.US));
            }
        }
        TextView tvLabelDescription = (TextView) v.findViewById(R.id.topicDescriptionInDescription);
        tvLabelDescription.setText(unitDescription);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(View v) {
        if (mListener != null) {
            mListener.onFragmentInteraction(v);
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
        void onFragmentInteraction(View v);
    }
}
