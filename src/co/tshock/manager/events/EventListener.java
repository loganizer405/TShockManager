package co.tshock.manager.events;

/**
 * Represents an event listener
 * 
 * @author koesie10
 */
public interface EventListener {
	/**
	 * This method is called when an event is broadcast to which this listener
	 * has been registered
	 * 
	 * @param event
	 *            The broadcast event
	 */
	public void onEvent(Event event);
}
