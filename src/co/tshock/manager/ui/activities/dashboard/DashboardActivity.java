package co.tshock.manager.ui.activities.dashboard;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import co.tshock.manager.Constants;
import co.tshock.manager.R;
import co.tshock.manager.data.models.Server;
import co.tshock.manager.api.TShockApi;
import co.tshock.manager.data.sqlite.DatabaseHelper;
import co.tshock.manager.events.Event;
import co.tshock.manager.events.EventListener;
import co.tshock.manager.events.EventManager;
import co.tshock.manager.events.EventType;
import co.tshock.manager.ui.activities.BaseActivity;
import co.tshock.manager.ui.fragments.dashboard.BanFragment;
import co.tshock.manager.ui.fragments.dashboard.GroupFragment;
import co.tshock.manager.ui.fragments.dashboard.PlayerFragment;
import co.tshock.manager.ui.fragments.dashboard.ServerFragment;
import co.tshock.manager.ui.fragments.dashboard.UserFragment;
import co.tshock.manager.ui.fragments.dashboard.WorldFragment;

public class DashboardActivity extends BaseActivity implements EventListener {

	private static final EventType[] types = new EventType[] { EventType.ERROR,
			EventType.TOKENTEST, EventType.CREATE_TOKEN };
	private Server server;
	private ProgressDialog progressDialog;

	private List<Tab> tabs;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		Intent intent = getIntent();
		if (intent == null) {
			throw new RuntimeException("You have not set a valid server");
		}

		server = intent.getParcelableExtra(Constants.ARGS_SERVER);

		if (server == null) {
			throw new RuntimeException("You have not set a valid server");
		}
		
		this.initializeTabs();

		this.getSupportActionBar().setTitle(server.getDisplayName());
		this.getSupportActionBar().setSubtitle(server.getHost());
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		TShockApi.setServer(server);

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(R.string.checking_token);
		progressDialog.setMessage(getString(R.string.wait));
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);

		if (server.getToken() != null && !server.getToken().equals("")) {
			TShockApi.tokentest();
		} else {
			TShockApi.createToken();
		}

		progressDialog.show();
	}

	protected void initializeTabs() {
		tabs = new ArrayList<Tab>();
		tabs.add(new Tab(getString(R.string.server), new ServerFragment()));
		tabs.add(new Tab(getString(R.string.user), new UserFragment()));
		tabs.add(new Tab(getString(R.string.ban), new BanFragment()));
		tabs.add(new Tab(getString(R.string.player), new PlayerFragment()));
		tabs.add(new Tab(getString(R.string.world), new WorldFragment()));
		tabs.add(new Tab(getString(R.string.group), new GroupFragment()));

		TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), tabs);

		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(adapter);

		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between pages, select the
						// corresponding tab.
						getSupportActionBar().setSelectedNavigationItem(
								position);
					}
				});

		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabReselected(ActionBar.Tab tab,
					FragmentTransaction ft) {

			}

			@Override
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				viewPager.setCurrentItem(tab.getPosition());

			}

			@Override
			public void onTabUnselected(ActionBar.Tab tab,
					FragmentTransaction ft) {

			}
		};

		for (Tab tab : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab.getTitle())
					.setTabListener(tabListener));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventManager.getInstance().registerListener(types, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		EventManager.getInstance().unregisterListener(types, this);
	}

	protected void onSuccessfulLogin() {
		progressDialog.dismiss();
		for (Tab tab : tabs) {
			tab.getFragment().onSuccessfulLogin(server);
		}
	}

	@Override
	public void onEvent(Event event) {
		switch (event.getType()) {
		case TOKENTEST:
			Log.i(TAG, "Token is still valid :)");
			onSuccessfulLogin();
			break;
		case CREATE_TOKEN:
			Log.i(TAG, "Token has been created");
			server.setToken(event.getData().get("token").toString());
			DatabaseHelper.getInstance(this).getServerDao().update(server);
			EventManager.getInstance().notify(
					new Event(EventType.SERVER_LIST_CHANGED));
			onSuccessfulLogin();
			break;
		case ERROR:
			EventType type = (EventType) event.getData().get("eventType");
			Exception exception = null;
			if (event.getData().containsKey("exception")) {
				exception = (Exception) event.getData().get("exception");
			}
			switch (type) {
			case TOKENTEST:
				if (event.getData().containsKey("statusCode")) {
					int statusCode = (Integer) event.getData()
							.get("statusCode");
					if (statusCode == 403) {
						Log.i(TAG, "Token is not valid anymore");
						progressDialog.dismiss();
						progressDialog.setTitle(R.string.creating_token);
						progressDialog.show();
						TShockApi.createToken();
						break;
					}
				}
			case CREATE_TOKEN:
				Log.i(TAG, "A token could not be created");
				progressDialog.dismiss();
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

						if (retryAllowed) {
							builder.setPositiveButton(R.string.retry,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											progressDialog.show();
											TShockApi.createToken();
										}
									});
						}

						builder.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										finish();
										dismiss();
									}
								});
						// Create the AlertDialog object and return it
						return builder.create();
					}
				};
				boolean retryAllowed = true;
				String message = getString(R.string.unknown_error);
				if (exception instanceof Exception) {
					// if we have an exception it means the onFailure() method
					// was called in TShockResponseHandler which means the
					// server could not be reached or returned a status code
					// higher than 400, which it should not do. For TShock the
					// status header is always: '200 Made by Jonas Gauffin'
					message = getString(R.string.server_unreachable);
				} else if (type.equals(EventType.CREATE_TOKEN)) {
					retryAllowed = false;
					if (event.getData().containsKey("statusCode")) {
						int statusCode = (Integer) event.getData().get(
								"statusCode");
						String error = (String) event.getData().get("error");
						/*
						 * 401: bad credentials 403: not allowed to use the API
						 */
						if (statusCode == 401 || statusCode == 403) {
							// show TShock's default error
							message = error;
						}
					} else {
						// this should not occur, so it is an unknown error
						message = getString(R.string.unknown_error);
					}
				}
				Bundle args = new Bundle();
				args.putString("message", message);
				args.putBoolean("retryAllowed", retryAllowed);
				errorDialog.setArguments(args);
				errorDialog.show(getSupportFragmentManager(), "token_error");
				break;
			}
			break;
		}

	}

}
