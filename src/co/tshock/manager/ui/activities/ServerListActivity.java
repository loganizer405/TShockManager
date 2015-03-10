package co.tshock.manager.ui.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import co.tshock.manager.Constants;
import co.tshock.manager.R;
import co.tshock.manager.data.models.Server;
import co.tshock.manager.data.adapter.ServerAdapter;
import co.tshock.manager.data.sqlite.DatabaseHelper;
import co.tshock.manager.events.Event;
import co.tshock.manager.events.EventListener;
import co.tshock.manager.events.EventManager;
import co.tshock.manager.events.EventType;
import co.tshock.manager.ui.activities.dashboard.DashboardActivity;
import co.tshock.manager.ui.dialogs.ServerDialogFragment;

public class ServerListActivity extends BaseActivity {

	private ListView listView;
	private ServerAdapter listAdapter;
	private List<Server> servers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.mainListView);
		servers = DatabaseHelper.getInstance(this).getServerDao().queryForAll();
		listAdapter = new ServerAdapter(this, servers);

		listView.setAdapter(listAdapter);
		EventManager.getInstance().registerListener(
				EventType.SERVER_LIST_CHANGED, new EventListener() {
					@Override
					public void onEvent(Event event) {
						refreshList();
					}
				});

		registerForContextMenu(listView);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Server server = (Server) parent.getItemAtPosition(position);
				Intent intent = new Intent(ServerListActivity.this, DashboardActivity.class);
				intent.putExtra(Constants.ARGS_SERVER, server);
				startActivity(intent);
			}

		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.mainListView) {
			getMenuInflater().inflate(R.menu.server_item, menu);
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			Server server = servers.get(info.position);
			menu.setHeaderTitle(server.getDisplayName());
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		Server server = servers.get(info.position);
		switch (item.getItemId()) {
		case R.id.action_edit:
			ServerDialogFragment editServerDialogFragment = new ServerDialogFragment();
			Bundle args = new Bundle();
			args.putParcelable(Constants.ARGS_SERVER, server);
			editServerDialogFragment.setArguments(args);
			editServerDialogFragment.show(getSupportFragmentManager(),
					"edit_server");
			return true;
		case R.id.action_remove:
			DatabaseHelper.getInstance(this).getServerDao().delete(server);
			refreshList();
			return true;
		}
		return true;
	}

	private void refreshList() {
		servers = DatabaseHelper.getInstance(this).getServerDao().queryForAll();
		listAdapter.setList(servers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			ServerDialogFragment addServerDialogFragment = new ServerDialogFragment();
			addServerDialogFragment.show(getSupportFragmentManager(),
					"add_server");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.server_list, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (DatabaseHelper.getInstance(this) != null) {
			DatabaseHelper.getInstance(this).close();
		}
	}
}
