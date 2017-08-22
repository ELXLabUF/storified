package tamu.themetry2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
 * {@link ThemeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ThemeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThemeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton btn;

    private OnFragmentInteractionListener mListener;

    public ThemeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThemeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThemeFragment newInstance(String param1, String param2) {
        ThemeFragment fragment = new ThemeFragment();
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
        View v = inflater.inflate(R.layout.fragment_theme, container, false);
        ((TextView)v.findViewById(R.id.themeName)).setText(mParam1);
        updateThemeImageButton(mParam1, v);
        return v;
    }

    public void updateThemeImageButton(final String mParam1, View v){
        final ImageView opacityImage = (ImageView) v.findViewById(R.id.opacityFilterTheme);
        final Button deselectThemeButton = (Button) v.findViewById(R.id.deselectThemeButton);
        final Button selectThemeButton = (Button) v.findViewById(R.id.selectThemeButton);
        final Animation animationFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);

        deselectThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Theme.myVP.setEnabled(true);
                btn.setEnabled(true);
                deselectThemeButton.setEnabled(false);
                selectThemeButton.setEnabled(false);
                opacityImage.startAnimation(animationFadeOut);
                opacityImage.setVisibility(v.INVISIBLE);
                deselectThemeButton.startAnimation(animationFadeOut);
                deselectThemeButton.setVisibility(v.INVISIBLE);
                selectThemeButton.startAnimation(animationFadeOut);
                selectThemeButton.setVisibility(v.INVISIBLE);
            }
        });

        selectThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(v, mParam1);
            }
        });

        if(mParam1.equals("Spy")){
            Drawable tempImage = ContextCompat.getDrawable(getActivity(), R.drawable.spy_image);
            btn = (ImageButton)v.findViewById(R.id.themeImage);
            btn.setImageDrawable(tempImage);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Theme.myVP.setEnabled(false);
                    btn.setEnabled(false);
                    deselectThemeButton.setEnabled(true);
                    selectThemeButton.setEnabled(true);
                    opacityImage.startAnimation(animationFadeIn);
                    opacityImage.setVisibility(v.VISIBLE);
                    deselectThemeButton.startAnimation(animationFadeIn);
                    deselectThemeButton.setVisibility(v.VISIBLE);
                    selectThemeButton.startAnimation(animationFadeIn);
                    selectThemeButton.setVisibility(v.VISIBLE);
                    //mListener.onFragmentInteraction(v, mParam1);
                }
            });
        }
        else if(mParam1.equals("Adventurer")){
            Drawable tempImage = ContextCompat.getDrawable(getActivity(), R.drawable.adventurer_image);
            btn = (ImageButton)v.findViewById(R.id.themeImage);
            btn.setImageDrawable(tempImage);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Theme.myVP.setEnabled(false);
                    btn.setEnabled(false);
                    deselectThemeButton.setEnabled(true);
                    selectThemeButton.setEnabled(true);
                    opacityImage.startAnimation(animationFadeIn);
                    opacityImage.setVisibility(v.VISIBLE);
                    deselectThemeButton.startAnimation(animationFadeIn);
                    deselectThemeButton.setVisibility(v.VISIBLE);
                    selectThemeButton.startAnimation(animationFadeIn);
                    selectThemeButton.setVisibility(v.VISIBLE);
                    //mListener.onFragmentInteraction(v, mParam1);
                }
            });
        }
        else if(mParam1.equals("Journalist")){
            Drawable tempImage = ContextCompat.getDrawable(getActivity(), R.drawable.journalist_image);
            btn = (ImageButton)v.findViewById(R.id.themeImage);
            btn.setImageDrawable(tempImage);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Theme.myVP.setEnabled(false);
                    btn.setEnabled(false);
                    deselectThemeButton.setEnabled(true);
                    selectThemeButton.setEnabled(true);
                    opacityImage.startAnimation(animationFadeIn);
                    opacityImage.setVisibility(v.VISIBLE);
                    deselectThemeButton.startAnimation(animationFadeIn);
                    deselectThemeButton.setVisibility(v.VISIBLE);
                    selectThemeButton.startAnimation(animationFadeIn);
                    selectThemeButton.setVisibility(v.VISIBLE);
                    //mListener.onFragmentInteraction(v, mParam1);
                }
            });
        }
        else {
            Drawable tempImage = ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_star_big_off);
            btn = (ImageButton)v.findViewById(R.id.themeImage);
            btn.setImageDrawable(tempImage);
        }
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
        void onFragmentInteraction(View v, String mParam1);
    }
}
