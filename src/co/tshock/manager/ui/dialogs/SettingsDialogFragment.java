package co.tshock.manager.ui.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import co.tshock.manager.R;

public class SettingsDialogFragment extends BaseDialogFragment {
	private View view;
	private Button saveButton;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		setStyle(style, theme);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(R.string.action_settings);
		view = inflater.inflate(R.layout.dialog_settings, container, false);
		saveButton = (Button) view.findViewById(R.id.saveButton);

		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// All settings should be saved here
				Log.i(TAG, "Save settings here");
			}
		});

		return view;
	}
}
