package net.pooksoft.nrclicker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import net.pooksoft.nrclicker.MainActivity;
import net.pooksoft.nrclicker.R;

/**
 * Created by andreas on 2014-11-12.
 */
public class SwitchFragmentDialog extends DialogFragment {

    private TextView mCountdownView;
    private AlertDialog alertDialog;
    private CountDownTimer switchTimer;
    private boolean autoSwitch;

    public SwitchFragmentDialog() {
    }

    public static SwitchFragmentDialog newInstance(String nextPlayerLabel) {
        SwitchFragmentDialog frag = new SwitchFragmentDialog();
        Bundle args = new Bundle();
        args.putString("nextPlayerLabel", nextPlayerLabel);
        frag.setArguments(args);
        frag.setCancelable(false);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String msgString = "Switch to " + getArguments().getString("nextPlayerLabel") + "?";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        autoSwitch = prefs.getBoolean("auto_switch_player_enabled", false);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msgString);//R.string.dialog_fire_missiles)

        if (autoSwitch) {
            builder.setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (autoSwitch) {
                        switchTimer.cancel();
                    }
                    ((MainActivity) getActivity()).dontStartNextTurn();
                }
            });
        } else {
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (autoSwitch) {
                        switchTimer.cancel();
                    }
                    ((MainActivity) getActivity()).startNextTurn();
                }
            })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (autoSwitch) {
                                switchTimer.cancel();
                            }
                            ((MainActivity) getActivity()).dontStartNextTurn();
                        }
                    });
        }

        if (autoSwitch) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_switch_dialog, null);
            mCountdownView = (TextView) view.findViewById(R.id.countdownTimer);
            builder.setView(view);
        }

        // Create the AlertDialog object and return it
        alertDialog = builder.create();
        return alertDialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (autoSwitch) {
            switchTimer = new CountDownTimer(2150, 1000) { // Add 150ms to compensate for discarded onTick events
                @Override
                public void onTick(long l) {
                    mCountdownView.setText("Switching in: " + ((int) Math.round(l / 1000.0)) + "...");
                }

                @Override
                public void onFinish() {
                    getDialog().dismiss();
                    ((MainActivity) getActivity()).startNextTurn();
                }
            };
            switchTimer.start();
        }
    }
}