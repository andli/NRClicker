package net.pooksoft.nrclicker;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.pooksoft.nrclicker.ui.LabeledNumberPicker;

/**
 * Created by andreas on 2014-09-01.
 */
public class CorpFragment extends Fragment {

    private View fragView;

    public CorpFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.fragment_corp, null);
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

    public void clearValues() {
        ViewGroup layout;
        try {
            layout = (ViewGroup) fragView.findViewById(R.id.FragmentContainer);

            int count = layout.getChildCount();
            for (int i = 0; i <= count; i++) {
                View v = layout.getChildAt(i);
                if (v instanceof LabeledNumberPicker) {
                    ((LabeledNumberPicker) v).reset();
                }
            }
        }
        catch (NullPointerException e) {
            Log.d("test", e.toString());
        }
    }
}
