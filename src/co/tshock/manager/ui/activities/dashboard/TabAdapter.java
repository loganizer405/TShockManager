package co.tshock.manager.ui.activities.dashboard;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
	private List<Tab> tabs;

	public TabAdapter(FragmentManager fm, List<Tab> tabs) {
		super(fm);
		this.tabs = tabs;
	}

	@Override
	public int getCount() {
		return tabs.size();
	}

	@Override
	public Fragment getItem(int position) {
		return tabs.get(position).getFragment();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return tabs.get(position).getTitle();
	}
}
