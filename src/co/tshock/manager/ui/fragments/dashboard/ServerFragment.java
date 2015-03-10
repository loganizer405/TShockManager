package co.tshock.manager.ui.fragments.dashboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import co.tshock.manager.R;
import co.tshock.manager.data.models.Server;
import co.tshock.manager.api.TShockApi;
import co.tshock.manager.events.Event;
import co.tshock.manager.events.EventListener;
import co.tshock.manager.events.EventManager;
import co.tshock.manager.events.EventType;

public class ServerFragment extends BaseDashboardFragment implements
		EventListener {
	private static final EventType[] types = new EventType[] { EventType.ERROR,
			EventType.SERVER_STATUS, EventType.SERVER_BROADCAST, EventType.SERVER_RAWCMD };

	private TextView serverNameTextView;
	private TextView serverPortTextView;
	private TextView serverPlayersTextView;
	private TextView serverWorldTextView;
	private TextView serverUptimeTextView;
	private TextView serverPasswordTextView;
	private Button serverOffButton;
	private Button serverRestartButton;
    private EditText serverBroadcastEditText;
    private Button serverBroadcastButton;
    private EditText serverRawCmdEditText;
    private Button serverRawCmdButton;
	private ServerStatus serverStatus;

	@Override
	public void onSuccessfulLogin(Server server) {
		TShockApi.status();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			TShockApi.status();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.server, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_server, container, false);

		this.serverNameTextView = (TextView) v
				.findViewById(R.id.serverNameTextView);
		this.serverPortTextView = (TextView) v
				.findViewById(R.id.serverPortTextView);
		this.serverPlayersTextView = (TextView) v
				.findViewById(R.id.serverPlayersTextView);
		this.serverWorldTextView = (TextView) v
				.findViewById(R.id.serverWorldTextView);
		this.serverUptimeTextView = (TextView) v
				.findViewById(R.id.serverUptimeTextView);
		this.serverPasswordTextView = (TextView) v
				.findViewById(R.id.serverPasswordTextView);
		this.serverOffButton = (Button) v.findViewById(R.id.serverOffButton);
		this.serverRestartButton = (Button) v.findViewById(R.id.serverRestartButton);
        this.serverBroadcastEditText = (EditText) v.findViewById(R.id.serverBroadcastEditText);
        this.serverBroadcastButton = (Button) v.findViewById(R.id.serverBroadcastButton);
        this.serverRawCmdEditText = (EditText) v.findViewById(R.id.serverRawCmdEditText);
        this.serverRawCmdButton = (Button) v.findViewById(R.id.serverRawCmdButton);

		this.serverOffButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Ask for a confirmation
				// TODO Show a progress dialog
				TShockApi.serverOff();
				getActivity().finish();
			}
		});

		this.serverRestartButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Ask for a confirmation
				// TODO Show a progress dialog
				TShockApi.serverRestart();
				getActivity().finish();
			}
		});

        this.serverBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Show a progress dialog
                String message = serverBroadcastEditText.getText().toString();
                TShockApi.serverBroadcast(message);
            }
        });

        this.serverRawCmdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Show a progress dialog
                // TODO Show the message returned from the server
                String cmd = serverRawCmdEditText.getText().toString();
                if (!cmd.startsWith("/")) {
                    cmd = "/" + cmd;
                }
                TShockApi.serverRawCmd(cmd);
            }
        });

		refreshInterface();

		return v;
	}

	protected void refreshInterface() {
		if (serverStatus == null) {
			return;
		}
		this.serverNameTextView.setText(serverStatus.getName());
		this.serverPortTextView
				.setText(Integer.toString(serverStatus.getPort()));
		this.serverPlayersTextView.setText(getString(
				R.string.server_players_text, serverStatus.getPlayerCount(),
				serverStatus.getMaxPlayers()));
		this.serverWorldTextView.setText(serverStatus.getWorld());
		this.serverUptimeTextView.setText(serverStatus.getUptime());
		this.serverPasswordTextView
				.setText(getString(serverStatus.isPassword() ? R.string.enabled
						: R.string.disabled));
	}

	@Override
	public void onResume() {
		super.onResume();
		EventManager.getInstance().registerListener(types, this);
	}

	@Override
	public void onPause() {
		super.onPause();
		EventManager.getInstance().unregisterListener(types, this);
	}

	@Override
	public void onEvent(Event event) {
		switch (event.getType()) {
		case SERVER_STATUS:
			String name = event.getData().get("name").toString();
			int port = (Integer) event.getData().get("port");
			int playerCount = (Integer) event.getData().get("playerCount");
			int maxPlayers = (Integer) event.getData().get("maxPlayer");
			String world = event.getData().get("world").toString();
			String uptime = event.getData().get("uptime").toString();
			boolean password = (Boolean) event.getData().get("serverPassword");
			if (name.trim().equals("")) {
				name = getString(R.string.na);
			}
			if (serverStatus == null) {
				serverStatus = new ServerStatus();
			}
			serverStatus.setName(name);
			serverStatus.setPort(port);
			serverStatus.setPlayerCount(playerCount);
			serverStatus.setMaxPlayers(maxPlayers);
			serverStatus.setWorld(world);
			serverStatus.setUptime(uptime);
			serverStatus.setPassword(password);
			refreshInterface();
			break;
		case ERROR:
			EventType type = (EventType) event.getData().get("eventType");
			Exception exception = null;
			if (event.getData().containsKey("exception")) {
				exception = (Exception) event.getData().get("exception");
			}
			switch (type) {
			case SERVER_STATUS:
				DialogFragment errorDialog = new DialogFragment() {
					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						Bundle args = getArguments();
						String message = null;
						boolean retryAllowed = true;
						if (args != null) {
							message = args.getString("message");
							retryAllowed = args.getBoolean("retryAllowed");
						}
						if (message == null) {
							message = getString(R.string.unknown_error);
						}
						// Use the Builder class for convenient dialog
						// construction
						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setMessage(message);

						builder.setPositiveButton(R.string.retry,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										TShockApi.createToken();
									}
								});

						builder.setNegativeButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dismiss();
									}
								});
						// Create the AlertDialog object and return it
						return builder.create();
					}
				};
				break;
			}
			break;
		}

	}

	private class ServerStatus {
		private String name;
		private int port;
		private int playerCount;
		private int maxPlayers;
		private String world;
		private String uptime;
		private boolean password;

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the port
		 */
		public int getPort() {
			return port;
		}

		/**
		 * @return the playerCount
		 */
		public int getPlayerCount() {
			return playerCount;
		}

		/**
		 * @return the maxPlayers
		 */
		public int getMaxPlayers() {
			return maxPlayers;
		}

		/**
		 * @return the world
		 */
		public String getWorld() {
			return world;
		}

		/**
		 * @return the uptime
		 */
		public String getUptime() {
			return uptime;
		}

		/**
		 * @return the password
		 */
		public boolean isPassword() {
			return password;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @param port
		 *            the port to set
		 */
		public void setPort(int port) {
			this.port = port;
		}

		/**
		 * @param playerCount
		 *            the playerCount to set
		 */
		public void setPlayerCount(int playerCount) {
			this.playerCount = playerCount;
		}

		/**
		 * @param maxPlayers
		 *            the maxPlayers to set
		 */
		public void setMaxPlayers(int maxPlayers) {
			this.maxPlayers = maxPlayers;
		}

		/**
		 * @param world
		 *            the world to set
		 */
		public void setWorld(String world) {
			this.world = world;
		}

		/**
		 * @param uptime
		 *            the uptime to set
		 */
		public void setUptime(String uptime) {
			this.uptime = uptime;
		}

		/**
		 * @param password
		 *            the password to set
		 */
		public void setPassword(boolean password) {
			this.password = password;
		}

	}
}
