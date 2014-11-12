package net.pooksoft.nrclicker;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

/**
 * Created by andreas on 2014-09-01.
 */
public class RunnerFragment extends Fragment {

    private static final String PLAYER_LABEL = "Runner";

    public RunnerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_runner, null);

        //LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.fragment_runner, container, false);

        return view;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static RunnerFragment newInstance() {
        RunnerFragment fragment = new RunnerFragment();
        Bundle args = new Bundle();
        args.putString(PLAYER_LABEL, "Runner");
        fragment.setArguments(args);
        return fragment;
    }
}
