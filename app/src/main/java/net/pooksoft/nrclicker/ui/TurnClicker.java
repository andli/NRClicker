package net.pooksoft.nrclicker.ui;

import android.app.Activity;
import android.app.FragmentManager;
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
import android.widget.RadioButton;

import net.pooksoft.nrclicker.R;

import java.util.ArrayList;

public class TurnClicker extends LinearLayout implements View.OnClickListener {

    private FragmentManager fMgr;
    private String nextPlayerLabel;
    private Vibrator myVib;

    public TurnClicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        myVib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        TypedArray xmlAttrs = context.obtainStyledAttributes(attrs, R.styleable.TurnClicker, 0, 0);
        int numClicks = xmlAttrs.getInteger(R.styleable.TurnClicker_numClicks, 0);
        this.nextPlayerLabel = xmlAttrs.getString(R.styleable.TurnClicker_nextPlayerLabel);
        xmlAttrs.recycle();

        fMgr = null;
        try {
            fMgr = ((Activity) context).getFragmentManager();
        } catch (Exception e) {
            Log.d("test", e.toString());
        }


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.turn_clicker, this, true);

        ViewGroup clickGroup = (ViewGroup) findViewById(R.id.clickGroup);

        for (int i = 0; i < numClicks; i++) {
            ClickerRadioButton radioButton = (ClickerRadioButton) inflater.inflate(R.layout.clicker_radiobutton, null);
            radioButton.setId(i);
            radioButton.setOnClickListener(this);
            radioButton.setPadding(0, 0, 0, 0);

            clickGroup.addView(radioButton);
        }

        ClickerRadioButton lastButton = (ClickerRadioButton) clickGroup.getChildAt(clickGroup.getChildCount() - 1);

        lastButton.setOnCheckedChangeListener(new ClickerRadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton RadioButton, boolean isChecked) {
                if (isChecked) {
                    SwitchFragmentDialog switchFragmentDialog = SwitchFragmentDialog.newInstance(nextPlayerLabel);

                    if (fMgr != null) {
                        /*FragmentTransaction ft = fMgr.beginTransaction();
                        switchFragmentDialog.show(ft, null);*/
                        switchFragmentDialog.show(fMgr, "fragment_edit_name");
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
        LinearLayout clickGroup = (LinearLayout) findViewById(R.id.clickGroup);
        int num = clickGroup.getChildCount();
        ArrayList<RadioButton> listOfRadioButtons = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            RadioButton rb = (RadioButton) clickGroup.getChildAt(i);
            listOfRadioButtons.add(rb);
            rb.setChecked(false);
        }
    }

    private int getPx(float dps) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    public void clearLastButton() {
        LinearLayout clickGroup = (LinearLayout) findViewById(R.id.clickGroup);
        ClickerRadioButton rb = (ClickerRadioButton) clickGroup.getChildAt(clickGroup.getChildCount() - 1);
        rb.setChecked(false);

    }
}
