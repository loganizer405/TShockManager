package co.tshock.manager.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import co.tshock.manager.Constants;
import co.tshock.manager.R;
import co.tshock.manager.data.models.Server;
import co.tshock.manager.data.models.User;
import co.tshock.manager.data.sqlite.DatabaseHelper;
import co.tshock.manager.events.Event;
import co.tshock.manager.events.EventManager;
import co.tshock.manager.events.EventType;
import co.tshock.manager.util.Utils;

public class EditUserDialogFragment extends BaseDialogFragment {
	private View view;
	private Button saveButton;
	private EditText userEditText;
	private EditText passwordEditText;
	private EditText groupEditText;
    private User user;
    private boolean isNew = true;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		setStyle(style, theme);

		if (getArguments() != null
				&& getArguments().containsKey(Constants.ARGS_USER)) {
			user = getArguments().getParcelable(Constants.ARGS_USER);
            isNew = false;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(R.string.server);
		view = inflater.inflate(R.layout.dialog_user_edit, container, false);
		saveButton = (Button) view.findViewById(R.id.saveButton);
		userEditText = (EditText) view.findViewById(R.id.nameEditText);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        groupEditText = (EditText) view.findViewById(R.id.groupEditText);

		groupEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		groupEditText
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent keyEvent) {
						save();
						return true;
					}

				});

		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				save();
			}
		});

		if (user != null) {
			userEditText.setText(user.getName());
            userEditText.setEnabled(false);
            passwordEditText.setEnabled(true);
            groupEditText.setText(user.getGroup());
		} else {
			user = new User();
		}

		return view;
	}

	private void save() {
		// The server should be added here
		String user = userEditText.getText().toString();
		String password = passwordEditText.getText().toString();
        String group = groupEditText.getText().toString();
		this.user.setName(user);
        this.user.setPassword(password);
        this.user.setGroup(group);

        if (isNew) {
            // TODO Call the server on "/v2/users/create"
        } else {
            // TODO Call the server on "/v2/users/update"
        }
		dismiss();
	}
}
