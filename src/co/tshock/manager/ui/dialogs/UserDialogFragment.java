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

public class UserDialogFragment extends BaseDialogFragment {
    private View view;
	private TextView nameTextView;
    private TextView idTextView;
    private TextView groupTextView;
    private Button deleteButton;
    private Button editButton;
	private User user;

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
		getDialog().setTitle(user.getName());
		view = inflater.inflate(R.layout.dialog_user, container, false);
        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        idTextView = (TextView) view.findViewById(R.id.idTextView);
        groupTextView = (TextView) view.findViewById(R.id.groupTextView);
        deleteButton = (Button) view.findViewById(R.id.deleteButton);
        editButton = (Button) view.findViewById(R.id.editButton);

        nameTextView.setText(user.getName());
        idTextView.setText(Integer.toString(user.getId()));
        groupTextView.setText(user.getGroup());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Call the server on "/v2/users/destroy"
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserDialogFragment dialog = new EditUserDialogFragment();
                Bundle args = new Bundle();
                args.putParcelable(Constants.ARGS_USER, user);
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "user_edit_" + user.getName());
            }
        });


		return view;
	}
}
