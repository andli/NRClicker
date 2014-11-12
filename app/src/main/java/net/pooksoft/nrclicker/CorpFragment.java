package net.pooksoft.nrclicker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by andreas on 2014-09-01.
 */
public class CorpFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number"; // placeholder

    public CorpFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_corp, null);

        return view;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CorpFragment newInstance(int sectionNumber) {
        CorpFragment fragment = new CorpFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
}
