package co.tshock.manager.ui.activities.dashboard;

import co.tshock.manager.ui.fragments.dashboard.BaseDashboardFragment;

public class Tab {
	private BaseDashboardFragment fragment;
	private String title;

	/**
	 * @return the fragment
	 */
	public BaseDashboardFragment getFragment() {
		return fragment;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param fragment
	 *            the fragment to set
	 */
	public void setFragment(BaseDashboardFragment fragment) {
		this.fragment = fragment;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public Tab(String title, BaseDashboardFragment fragment) {
		super();
		this.fragment = fragment;
		this.title = title;
	}
	
	public Tab(BaseDashboardFragment fragment) {
		this(fragment.getClass().getSimpleName(), fragment);
	}
}
