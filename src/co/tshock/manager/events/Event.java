package co.tshock.manager.events;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an event that can be broadcast to the event listeners
 * 
 * @author koesie10
 */
public class Event {
	private EventType type;
	private Map<String, Object> data;
	private boolean cancelled;

	/**
	 * Creates a new event with an {@code EventType} and data
	 * 
	 * @param type
	 *            The type of the event
	 * @param data
	 *            The data of the event
	 */
	public Event(EventType type, Map<String, Object> data) {
		this.type = type;
		this.data = data;
	}

	/**
	 * Creates a new event with an {@code EventType} and empty data
	 * 
	 * @see Event#Event(EventType, Map)
	 * @param type
	 *            The type of the event
	 */
	public Event(EventType type) {
		this(type, new HashMap<String, Object>());
	}

	/**
	 * Returns the type of this {@code Event}
	 * 
	 * @return the type
	 */
	public EventType getType() {
		return type;
	}

	/**
	 * Returns the data in a {@code Map<String, Object>}
	 * 
	 * @return the data
	 */
	public Map<String, Object> getData() {
		return data;
	}

	/**
	 * @return Whether this event has been cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Set this event to cancelled. If cancelled is set to true, the event will
	 * not be broadcast to other players.
	 * 
	 * @param cancelled
	 *            Whether to cancel this event
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.type.toString() + ": " + this.data.toString();
	}

}
