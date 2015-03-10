package co.tshock.manager.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import co.tshock.manager.R;
import co.tshock.manager.data.models.User;

/**
 * An adapter for {@link co.tshock.manager.data.models.User} objects
 *
 * @author koesie10
 */
public class UserAdapter extends BaseAdapter {
	private final String TAG = this.getClass().getSimpleName();

	private Context context;
	private List<User> users;

	public UserAdapter(Context context, List<User> users) {
		super();
		this.context = context;
		this.users = users;
	}

	/**
	 * A holder which saves the views so we have more speed ;)
	 * 
	 * @author koesie10
	 * 
	 */
	static class ViewHolder {
		private TextView name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		return users.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getItem(int position) {
		return users.get(position);
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

			view = inflater.inflate(R.layout.user_row, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) view
					.findViewById(R.id.nameTextView);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		User user = users.get(position);

		holder.name.setText(user.getName());

		return view;
	}

	public void setList(List<User> users) {
		this.users = users;
		this.notifyDataSetChanged();
	}

}
