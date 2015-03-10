package co.tshock.manager.ui.activities;

import co.tshock.manager.R;
import co.tshock.manager.ui.dialogs.SettingsDialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class BaseActivity extends ActionBarActivity {
	protected final String TAG = this.getClass().getSimpleName();
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			SettingsDialogFragment settingsDialogFragment = new SettingsDialogFragment();
			settingsDialogFragment.show(getSupportFragmentManager(), "settings");
			break;
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
