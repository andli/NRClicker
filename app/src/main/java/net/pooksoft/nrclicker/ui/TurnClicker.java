package net.pooksoft.nrclicker.ui;

import android.content.Context;
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
public class TurnClicker extends LinearLayout implements ToggleButton.OnCheckedChangeListener {

    private String playerLabel;

    public TurnClicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray xmlAttrs = context.obtainStyledAttributes(attrs, R.styleable.TurnClicker, 0, 0);
        int numClicks = xmlAttrs.getInteger(R.styleable.TurnClicker_numClicks, 0);
        String label = xmlAttrs.getString(R.styleable.TurnClicker_label);
        xmlAttrs.recycle();

        this.playerLabel = label;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.turn_clicker, this, true);

        ToggleButton[] buttons = new ToggleButton[numClicks];

        for (int i = 0; i < numClicks; i++) {
            buttons[i] = new ToggleButton(context);
            buttons[i].setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            buttons[i].setText(Integer.toString(i));
            buttons[i].setTextOn(Integer.toString(i));
            buttons[i].setTextOff(Integer.toString(i));
            buttons[i].setTextSize(24f);
            if (i == 0) {
                buttons[i].setChecked(true);
            } else {
                buttons[i].setChecked(false);
            }
            buttons[i].setClickable(true);
            buttons[i].setMinHeight(70);
            buttons[i].setMinWidth(80);

            // if it's the last button, add a listener that spawns a question
            if (i == numClicks - 1) {
                buttons[i].setOnCheckedChangeListener(this);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Log.d("test", "onCheckedChanged"); //TODO: make this trigger!
        if (b) {
            SwitchFragmentDialog switchFragmentDialog = new SwitchFragmentDialog();
            Bundle bundle = new Bundle();
            bundle.putString("playerLabel", playerLabel);
            switchFragmentDialog.setArguments(bundle);
        }
    }
}
