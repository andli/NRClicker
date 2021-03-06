package net.pooksoft.nrclicker.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import net.pooksoft.nrclicker.R;
import net.pooksoft.nrclicker.ValueChangeListener;

import java.lang.reflect.Field;
import java.util.LinkedList;

/**
 * Created by andreas on 2014-07-31.
 */
public class LabeledNumberPicker extends LinearLayout implements NumberPicker.OnValueChangeListener {

    private static final int CHANGE_TIMEOUT = 2000;
    final Handler changeHandler = new Handler();
    NumberPicker np;
    VerticalTextView label;
    TextView log;
    LinkedList<Integer> loggedValues;
    private int startValue;
    private boolean danger;
    private ValueChangeListener activityListener;

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

    public LabeledNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        NUM_LOG_VALS = 5;
        loggedValues = new LinkedList<Integer>();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LabeledNumberPicker, 0, 0);
        String titleText = a.getString(R.styleable.LabeledNumberPicker_labelText);
        int rangeMax = a.getInteger(R.styleable.LabeledNumberPicker_rangeMax, 0);
        int rangeMin = a.getInteger(R.styleable.LabeledNumberPicker_rangeMin, 0);
        danger = a.getBoolean(R.styleable.LabeledNumberPicker_danger, false);
        startValue = a.getInteger(R.styleable.LabeledNumberPicker_startValue, 0);
        float fontSize = a.getFloat(R.styleable.LabeledNumberPicker_fontSize, 0);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.labeled_number_picker, this, true);
        label = (VerticalTextView) findViewById(R.id.label);
        log = (TextView) findViewById(R.id.logText);
        np = (NumberPicker) findViewById(R.id.numpick);

        label.setTextSize(fontSize);
        setLabelText(titleText);
        np.setMinValue(rangeMin);
        np.setMaxValue(rangeMax);
        np.setValue(startValue);
        np.setOnValueChangedListener(this);

    }

    public LabeledNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException e) {
                    Log.w("setNumberPickerTextColor", e);
                } catch (IllegalAccessException e) {
                    Log.w("setNumberPickerTextColor", e);
                } catch (IllegalArgumentException e) {
                    Log.w("setNumberPickerTextColor", e);
                }
            }
        }
        return false;
    }

    public int getValue() {
        return np.getValue();
    }

    public void setLabelText(String text) {
        label.setText(text);
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

    private void clearLogger() {
        loggedValues.clear();
        log.setText("");
        log.append("...");
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
        if (!CHANGE_RUNNING) {
            CHANGE_RUNNING = true;
            changeHandler.postDelayed(runnable, CHANGE_TIMEOUT);
        }

        if (danger) {
            if (newVal > 0) {
                setNumberPickerTextColor(numberPicker, android.graphics.Color.argb(255, 255, 0, 0));
            } else {
                setNumberPickerTextColor(numberPicker, label.getTextColors().getDefaultColor());
            }
        }


        try {
            this.activityListener.onValueUpdated(this.getId(), newVal);
        } catch (NullPointerException e) {
            Log.d("test", "onValueChange in lnp, no target listener");
        }
    }

    public void setOnValueChangeListener(ValueChangeListener listener) {
        this.activityListener = listener;
    }

    public void reset() {
        np = (NumberPicker) findViewById(R.id.numpick);
        np.setValue(this.startValue);
        setNumberPickerTextColor(np, label.getTextColors().getDefaultColor());
        clearLogger();
    }

}
