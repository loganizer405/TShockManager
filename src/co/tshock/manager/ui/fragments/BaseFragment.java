package co.tshock.manager.ui.fragments;

import android.support.v4.app.Fragment;
import android.view.MenuItem;
import co.tshock.manager.R;
import co.tshock.manager.ui.dialogs.SettingsDialogFragment;

public class BaseFragment extends Fragment {
    // Cast needed for Android Studio/IntelliJ
    protected final String TAG = ((Object)this).getClass().getSimpleName();
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			SettingsDialogFragment settingsDialogFragment = new SettingsDialogFragment();
			settingsDialogFragment.show(this.getFragmentManager(), "settings");
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
