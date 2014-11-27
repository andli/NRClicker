package net.pooksoft.nrclicker;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.pooksoft.nrclicker.ui.LabeledNumberPicker;

import java.util.ArrayList;

/**
 * Created by andreas on 2014-09-01.
 */
public class RunnerFragment extends Fragment {

    private ArrayList<Integer> lnps;
    private ValueChangeListener vcListener;

    public RunnerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_runner, null);

        try {
            vcListener = (ValueChangeListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement ValueChangeListener");
        }

        lnps = new ArrayList<Integer>();
        lnps.add(R.id.lnpAgendasRunner);
        lnps.add(R.id.lnpBrainDamage);
        lnps.add(R.id.lnpCreditsRunner);
        lnps.add(R.id.lnpLinkStrength);
        lnps.add(R.id.lnpTags);

        for (int lnpId: lnps) {
            LabeledNumberPicker lnp = (LabeledNumberPicker) fragView.findViewById(lnpId);
            try {
                lnp.setOnValueChangeListener(vcListener);
            }
            catch (Exception e) {
                Log.d("test", e.getMessage());
            }
        }

        return fragView;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static RunnerFragment newInstance() {
        RunnerFragment fragment = new RunnerFragment();
        Bundle args = new Bundle();
        //args.putString("nextPlayerLabel", "Corp");
        fragment.setArguments(args);
        return fragment;
    }
}
