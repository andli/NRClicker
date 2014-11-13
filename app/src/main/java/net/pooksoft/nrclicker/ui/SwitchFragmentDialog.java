package net.pooksoft.nrclicker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import net.pooksoft.nrclicker.R;

/**
 * Created by andreas on 2014-11-12.
 */
public class SwitchFragmentDialog extends DialogFragment {

    public SwitchFragmentDialog() {}

    public static SwitchFragmentDialog newInstance(String nextPlayerLabel) {
        SwitchFragmentDialog frag = new SwitchFragmentDialog();
        Bundle args = new Bundle();
        args.putString("nextPlayerLabel", nextPlayerLabel);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
         String msgString = "Switch to " + getArguments().getString("nextPlayerLabel") + "?";

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msgString)//R.string.dialog_fire_missiles)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}