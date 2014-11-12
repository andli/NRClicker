package net.pooksoft.nrclicker.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import net.pooksoft.nrclicker.R;

/**
 * Created by andreas on 2014-11-11.
 */
public class TurnClicker extends LinearLayout {

    private FragmentManager fMgr;
    private String playerLabel;

    public TurnClicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray xmlAttrs = context.obtainStyledAttributes(attrs, R.styleable.TurnClicker, 0, 0);
        int numClicks = xmlAttrs.getInteger(R.styleable.TurnClicker_numClicks, 0);
        String label = xmlAttrs.getString(R.styleable.TurnClicker_label);
        xmlAttrs.recycle();

        fMgr = null;
        try {
            fMgr = ((Activity) context).getFragmentManager();
        }
        catch (Exception e) {
            Log.d("test", e.toString());
        }

        this.playerLabel = label;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.turn_clicker, this, true);

        ViewGroup clickGroup = (ViewGroup) findViewById(R.id.clickGroup);
        ToggleButton[] buttons = new ToggleButton[numClicks];

        for (int i = 0; i < numClicks; i++) {
            buttons[i] = new ToggleButton(context);
            buttons[i].setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            buttons[i].setText(Integer.toString(i+1));
            buttons[i].setTextOn(Integer.toString(i+1));
            buttons[i].setTextOff(Integer.toString(i+1));
            buttons[i].setTextSize(24f);
            buttons[i].setClickable(true);
            buttons[i].setHeight(170);
            buttons[i].setWidth(180);

            // if it's the last button, add a listener that spawns a question
            if (i == numClicks - 1) {
                Log.d("test", "setOnCheckedChangeListener for " + Integer.toString(i));
                buttons[i].setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                        if (isChecked) {
                            SwitchFragmentDialog switchFragmentDialog = SwitchFragmentDialog.newInstance(playerLabel);

                            Log.d("test", "Dialog here");
                            if (fMgr != null) {
                                switchFragmentDialog.show(fMgr, "HEJ");
                            } else {
                                Log.d("test", "fMgr is null");
                            }
                        }
                    }
                });
            }

            clickGroup.addView(buttons[i]);
        }
    }
}
