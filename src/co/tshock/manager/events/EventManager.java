package co.tshock.manager.events;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

/**
 * Manages all event listener and is the class to notify the listeners of new
 * events
 * 
 * @author koesie10
 */
public class EventManager {
	private static EventManager instance;
	private final String TAG = this.getClass().getSimpleName();

	/**
	 * Gets the current EventManager singleton
	 * 
	 * @return Current EventManager in use
	 */
	public static EventManager getInstance() {
		if (instance == null) {
			instance = new EventManager();
		}
		return instance;
	}

	private HashMap<EventType, ArrayList<EventListener>> listeners;

	/**
	 * Registers the specified listener to the given event type
	 * 
	 * @param type
	 *            Event type to register
	 * @param listener
	 *            Listener to register
	 */
	public void registerListener(EventType type, EventListener listener) {
		if (listeners.get(type) == null) {
			this.listeners.put(type, new ArrayList<EventListener>());
		}
		this.listeners.get(type).add(listener);
	}

	/**
	 * Registers the specified listener to the given event types
	 * 
	 * @see #registerListener(EventType, EventListener)
	 * @param types
	 *            Event types to register
	 * @param listener
	 *            Listener to register
	 */
	public void registerListener(EventType[] types, EventListener listener) {
		for (EventType type : types) {
			this.registerListener(type, listener);
		}
	}

	/**
	 * Registers the specified listeners to the given event type
	 * 
	 * @see #registerListener(EventType, EventListener)
	 * @param type
	 *            Event type to register
	 * @param listeners
	 *            Listeners to register
	 */
	public void registerListeners(EventType type, EventListener[] listeners) {
		for (EventListener listener : listeners) {
			this.registerListener(type, listener);
		}
	}

	/**
	 * Registers the specified listeners to the given event types
	 * 
	 * @see #registerListener(EventType, EventListener)
	 * @param types
	 *            Event types to register
	 * @param listeners
	 *            Listeners to register
	 */
	public void registerListeners(EventType[] types, EventListener[] listeners) {
		for (EventListener listener : listeners) {
			this.registerListener(types, listener);
		}
	}

	/**
	 * Unregisters the specified listener from the given event type
	 * 
	 * @param type
	 *            Event type from which the listener has to be unregistered
	 * @param listener
	 *            Listener to unregister
	 */
	public void unregisterListener(EventType type, EventListener listener) {
		if (listeners.get(type) == null) {
			return;
		}
		this.listeners.get(type).remove(listener);
	}

	/**
	 * Unregisters the specified listener from the given event types
	 * 
	 * @param types
	 *            Event types from which the listener has to be unregistered
	 * @param listener
	 *            Listener to unregister
	 */
	public void unregisterListener(EventType[] types, EventListener listener) {
		for (EventType type : types) {
			this.unregisterListener(type, listener);
		}
	}

	/**
	 * Unregisters the specified listeners from the given event type
	 * 
	 * @param type
	 *            Event type from which the listeners have to be unregistered
	 * @param listeners
	 *            Listeners to unregister
	 */
	public void unregisterListeners(EventType type, EventListener[] listeners) {
		for (EventListener listener : listeners) {
			this.unregisterListener(type, listener);
		}
	}

	/**
	 * Unregisters the specified listeners from the given event types
	 * 
	 * @param types
	 *            Event types from which the listeners have to be unregistered
	 * @param listeners
	 *            Listeners to unregister
	 */
	public void unregisterListeners(EventType[] types, EventListener[] listeners) {
		for (EventListener listener : listeners) {
			this.unregisterListener(types, listener);
		}
	}

	/**
	 * Notify all listeners which are registered to the type of this event
	 * 
	 * @param event
	 *            The event to broadcast to the listeners
	 */
	public void notify(Event event) {
		ArrayList<EventListener> typeListeners = listeners.get(event.getType());
		if (typeListeners == null) {
            Log.i(TAG, "Notifying 0 listeners of event " + event.toString());
			return;
		}
		Log.i(TAG, "Notifying " + typeListeners.size() + " listeners of event " + event.toString());
		for (EventListener listener : typeListeners) {
			listener.onEvent(event);
			if (event.isCancelled()) {
				return;
			}
		}
	}

	/**
	 * This method exists to defeat instantiation from another class
	 */
	private EventManager() {
		this.listeners = new HashMap<EventType, ArrayList<EventListener>>();
	}
}
