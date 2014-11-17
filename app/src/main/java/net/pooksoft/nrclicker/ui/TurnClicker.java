package net.pooksoft.nrclicker.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import net.pooksoft.nrclicker.R;

import java.util.ArrayList;

/**
 * Created by andreas on 2014-11-11.
 */
public class TurnClicker extends LinearLayout implements View.OnClickListener {

    private FragmentManager fMgr;
    private String nextPlayerLabel;
    private int numClicks;
    private Vibrator myVib;

    public TurnClicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        myVib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        TypedArray xmlAttrs = context.obtainStyledAttributes(attrs, R.styleable.TurnClicker, 0, 0);
        numClicks = xmlAttrs.getInteger(R.styleable.TurnClicker_numClicks, 0);
        this.nextPlayerLabel = xmlAttrs.getString(R.styleable.TurnClicker_nextPlayerLabel);
        xmlAttrs.recycle();

        fMgr = null;
        try {
            fMgr = ((Activity) context).getFragmentManager();
        }
        catch (Exception e) {
            Log.d("test", e.toString());
        }


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.turn_clicker, this, true);

        ViewGroup clickGroup = (ViewGroup) findViewById(R.id.clickGroup);

        for (int i = 0; i < numClicks; i++) {
            inflater.inflate(R.layout.toggle_button, clickGroup, true);

            if (numClicks % 2 == 0) {
                // TODO: offset maybe?
            }
        }

        // if it's the last button, add a listener that spawns a question
        int numButtons = clickGroup.getChildCount();
        for (int j = 0; j < numButtons; j++) {
            ToggleButton tb = (ToggleButton) clickGroup.getChildAt(j);
            tb.setOnClickListener(this);
            //getApplicationContext().getSystemService(VIBRATOR_SERVICE);
        }

        ToggleButton lastTb = (ToggleButton) clickGroup.getChildAt(numButtons - 1);

        lastTb.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton ToggleButton, boolean isChecked) {
                if (isChecked) {
                    SwitchFragmentDialog switchFragmentDialog = SwitchFragmentDialog.newInstance(nextPlayerLabel);

                    if (fMgr != null) {
                        FragmentTransaction ft = fMgr.beginTransaction();
                        switchFragmentDialog.show(ft, null);
                    } else {
                        Log.d("test", "fMgr is null");
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        myVib.vibrate(25);
    }

    public void clearButtons() {
        RadioGroup clickGroup = (RadioGroup) findViewById(R.id.clickGroup);
        int num = clickGroup.getChildCount();
        ArrayList<ToggleButton> listOfRadioButtons = new ArrayList<ToggleButton>();
        for (int i = 0; i < num; i++) {
            ToggleButton tb = (ToggleButton)clickGroup.getChildAt(i);
            listOfRadioButtons.add(tb);
            tb.setChecked(false);
        }
    }

    private int getPx(float dps) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }
}
