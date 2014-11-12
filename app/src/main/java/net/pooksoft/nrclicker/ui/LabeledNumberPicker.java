package net.pooksoft.nrclicker.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import net.pooksoft.nrclicker.R;
import net.pooksoft.nrclicker.ui.VerticalTextView;

import java.util.LinkedList;

/**
 * Created by andreas on 2014-07-31.
 */
public class LabeledNumberPicker extends LinearLayout implements NumberPicker.OnValueChangeListener {

    private static final int CHANGE_TIMEOUT = 2000;
    NumberPicker np;
    VerticalTextView label;
    TextView log;
    LinkedList loggedValues;
    final Handler changeHandler = new Handler();

    private int NUM_LOG_VALS;
    private boolean CHANGE_RUNNING = false;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            stepLogger(np.getValue());
            CHANGE_RUNNING = false;
      /* and here comes the "trick" */
            //changeHandler.postDelayed(this, 100);
        }
    };


    public LabeledNumberPicker(Context context) {
        this(context, null);
    }

    public LabeledNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        NUM_LOG_VALS = 5;
        loggedValues = new LinkedList();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LabeledNumberPicker, 0, 0);
        String titleText = a.getString(R.styleable.LabeledNumberPicker_labelText);
        int rangeMax = a.getInteger(R.styleable.LabeledNumberPicker_rangeMax, 0);
        int rangeMin = a.getInteger(R.styleable.LabeledNumberPicker_rangeMin, 0);
        float fontSize = a.getFloat(R.styleable.LabeledNumberPicker_fontSize, 0);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.labeled_number_picker, this, true);
        label = (VerticalTextView)findViewById(R.id.label);
        log = (TextView)findViewById(R.id.logText);
        np = (NumberPicker)findViewById(R.id.numpick);

        label.setTextSize(fontSize);
        setLabelText(titleText);
        np.setMinValue(rangeMin);
        np.setMaxValue(rangeMax);
        np.setValue(0);
        np.setOnValueChangedListener(this);

    }

    public LabeledNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLabelText(String text) {
        label.setText(text);
    }

    public void setMinValue(int minVal) {
        np.setMinValue(minVal);
    }

    public void setMaxValue(int maxVal) {
        np.setMaxValue(maxVal);
    }

    public void setWrapSelectorWheel(boolean b) {
        np.setWrapSelectorWheel(b);
    }

    public void setValue(int i) {
        np.setValue(i);
    }

    private void stepLogger(int newValue) {
        if (loggedValues.size() == 0 ||
                newValue != Integer.parseInt(loggedValues.peekFirst().toString())) {
            loggedValues.addFirst(newValue);

            log.setText("");
            for (int i = 0; i < NUM_LOG_VALS && i != loggedValues.size(); i++) {
                log.append(loggedValues.get(i).toString() + "\n");
            }
            log.append("...");
        }
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i2) {
        if (!CHANGE_RUNNING) {
            CHANGE_RUNNING = true;
            changeHandler.postDelayed(runnable, CHANGE_TIMEOUT);
        }
    }


}
