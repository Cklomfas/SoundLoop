package com.capstone.soundloop;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class AddProjectFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle thisBundle) {
		super.onCreateDialog(thisBundle);
		
		Builder builder = new Builder(getActivity());
		
		
		return builder.create();
	}
	
}
