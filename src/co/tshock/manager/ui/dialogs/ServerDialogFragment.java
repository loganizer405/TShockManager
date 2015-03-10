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
import co.tshock.manager.data.sqlite.DatabaseHelper;
import co.tshock.manager.events.Event;
import co.tshock.manager.events.EventManager;
import co.tshock.manager.events.EventType;
import co.tshock.manager.util.Utils;

public class ServerDialogFragment extends BaseDialogFragment {
	private View view;
	private Button saveButton;
	private EditText ipEditText;
	private EditText portEditText;
	private EditText displayNameEditText;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private DatabaseHelper dbHelper;
	private Server server;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		setStyle(style, theme);

		if (getArguments() != null
				&& getArguments().containsKey(Constants.ARGS_SERVER)) {
			server = getArguments().getParcelable(Constants.ARGS_SERVER);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		dbHelper = DatabaseHelper.getInstance(activity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(R.string.server);
		view = inflater.inflate(R.layout.dialog_server, container, false);
		saveButton = (Button) view.findViewById(R.id.saveButton);
		ipEditText = (EditText) view.findViewById(R.id.ipEditText);
		portEditText = (EditText) view.findViewById(R.id.portEditText);
		displayNameEditText = (EditText) view
				.findViewById(R.id.displayNameEditText);
		usernameEditText = (EditText) view.findViewById(R.id.usernameEditText);
		passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);

		passwordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		passwordEditText
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

		if (server != null) {
			ipEditText.setText(server.getIp());
			portEditText.setText(Integer.toString(server.getPort()));
			displayNameEditText.setText(server.getDisplayName());
			usernameEditText.setText(server.getUsername());
			passwordEditText.setText(server.getPassword());
		} else {
			server = new Server();
		}

		return view;
	}

	private void save() {
		// The server should be added here
		String ip = ipEditText.getText().toString();
		int port = Utils.parseInt(portEditText.getText().toString(), 7878);
		String displayName = displayNameEditText.getText().toString();
		if (displayName == null || displayName.trim().length() == 0) {
			displayName = String.format("%s:%d", ip, port);
		}
		String username = usernameEditText.getText().toString();
		String password = passwordEditText.getText().toString();
		server.setIp(ip);
		server.setPort(port);
		server.setDisplayName(displayName);
		server.setUsername(username);
		server.setPassword(password);
		dbHelper.getServerDao().createOrUpdate(server);
		EventManager.getInstance().notify(
				new Event(EventType.SERVER_LIST_CHANGED));
		dismiss();
	}
}
