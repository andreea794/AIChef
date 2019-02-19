package com.teamalpha.aichef;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AddIngredientDialogFragment extends DialogFragment{

    private OnYesNoClick yesNoClick = null;

    public static AddIngredientDialogFragment newInstance(String ingredient) {
        AddIngredientDialogFragment dialogFragment = new AddIngredientDialogFragment();
        Bundle args = new Bundle();
        args.putString("ingredient", ingredient);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public void setOnYesNoClick(OnYesNoClick yesNoClik) {
        this.yesNoClick = yesNoClik;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        String ingredient = getArguments().getString("ingredient");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Add " + ingredient + " to your list?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(yesNoClick != null)
                            yesNoClick.onYesClicked();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        if (yesNoClick != null)
                            yesNoClick.onNoClicked();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface OnYesNoClick {
        void onYesClicked();
        void onNoClicked();
    }
}
