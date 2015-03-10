package co.tshock.manager.data.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import co.tshock.manager.R;
import co.tshock.manager.data.models.Server;

/**
 * An adapter for {@link Server} objects
 * 
 * @author koesie10
 */
public class ServerAdapter extends BaseAdapter {
	private final String TAG = this.getClass().getSimpleName();

	private Context context;
	private List<Server> servers;

	public ServerAdapter(Context context, List<Server> servers) {
		super();
		this.context = context;
		this.servers = servers;
	}

	/**
	 * A holder which saves the views so we have more speed ;)
	 * 
	 * @author koesie10
	 * 
	 */
	static class ViewHolder {
		private TextView displayName;
		private TextView host;
		private TextView username;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		return servers.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getItem(int position) {
		return servers.get(position);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.server_row, parent, false);
			holder = new ViewHolder();
			holder.displayName = (TextView) view
					.findViewById(R.id.displayNameTextView);
			holder.host = (TextView) view.findViewById(R.id.hostTextView);
			holder.username = (TextView) view
					.findViewById(R.id.usernameTextView);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		Server server = servers.get(position);

		holder.displayName.setText(server.getDisplayName());
		holder.host.setText(String.format("%s:%d", server.getIp(),
				server.getPort()));
		holder.username.setText(server.getUsername());

		return view;
	}

	public void setList(List<Server> servers) {
		this.servers = servers;
		this.notifyDataSetChanged();
	}

}
