package net.pooksoft.nrclicker;

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
public class CorpFragment extends Fragment {

    private ArrayList<Integer> lnps;
    private ValueChangeListener vcListener;

    public CorpFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_corp, null);

        try {
            vcListener = (ValueChangeListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement ValueChangeListener");
        }

        lnps = new ArrayList<Integer>();
        lnps.add(R.id.lnpAgendasCorp);
        lnps.add(R.id.lnpBadPublicity);
        lnps.add(R.id.lnpCreditsCorp);

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
    public static CorpFragment newInstance() {
        CorpFragment fragment = new CorpFragment();
        Bundle args = new Bundle();
        //args.putString("nextPlayerLabel", "Runner");
        fragment.setArguments(args);
        return fragment;
    }

}
