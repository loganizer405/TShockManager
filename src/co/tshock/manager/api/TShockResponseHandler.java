package co.tshock.manager.api;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import co.tshock.manager.events.Event;
import co.tshock.manager.events.EventManager;
import co.tshock.manager.events.EventType;

import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Handles TShock API responses
 * 
 * @author koesie10
 */
public class TShockResponseHandler extends JsonHttpResponseHandler {
	private static final String TAG = TShockResponseHandler.class.getSimpleName();
	private DataProcessor dataProcessor;
	private EventType eventType;

	/**
	 * Constructs a new response handler for TShock API responses
	 * 
	 * @param eventType
	 *            The event type this handler should handle
	 * @param dataProcessor
	 *            The {@link DataProcessor} that gets called when there is valid
	 *            data that should be processed
	 */
	public TShockResponseHandler(EventType eventType,
			DataProcessor dataProcessor) {
		this.dataProcessor = dataProcessor;
		this.eventType = eventType;
	}

	/**
	 * Constructs a new response handler for TShock API responses without a
	 * {@link DataProcessor}
	 * 
	 * @param eventType
	 *            The event type this handler should handle
	 * @see #TShockResponseHandler(co.tshock.manager.events.EventType, co.tshock.manager.api.TShockResponseHandler.DataProcessor)
	 */
	public TShockResponseHandler(EventType eventType) {
		this.eventType = eventType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSuccess(JSONObject object) {
		final Map<String, Object> data = new HashMap<String, Object>();
		data.put("eventType", this.eventType);
		try {
			int statusCode = object.getInt("status");
			data.put("statusCode", statusCode);
			if (object.has("error") || statusCode >= 400) {
				reportError(data, object.getString("error"));
				return;
			}
            String response = "";
			if (object.has("response")) {
				response = object.getString("response");
				data.put("response", response);
			}
			if (statusCode == 200) {
				if (this.dataProcessor != null) {
					dataProcessor.parseResponse(object, data);
				}
				EventManager.getInstance().notify(new Event(eventType, data));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void reportError(Map<String, Object> data, String response,
			Throwable e) {
		data.put("error", response);
		if (e != null) {
			data.put("exception", e);
		}
		if (!data.containsKey("eventType")) {
			data.put("eventType", this.eventType);
		}
		EventManager.getInstance().notify(new Event(EventType.ERROR, data));
		Log.e(TAG, response, e);
	}

	protected void reportError(Map<String, Object> data, String response) {
		reportError(data, response, null);
	}

	protected void reportError(String response) {
		reportError(new HashMap<String, Object>(), response, null);
	}

	protected void reportError(Throwable e, String response) {
		reportError(new HashMap<String, Object>(), response, e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onFailure(Throwable e, String response) {
		Log.e(TAG, response, e);
		reportError(e, response);
	}

	/**
	 * The processor that should parse valid data returned from a response
	 * 
	 * @author koesie10
	 */
	public static interface DataProcessor {
		/**
		 * Called when a valid response should be parsed
		 * 
		 * @param object
		 *            The data object that has been approved and is thus valid
		 * @param data
		 *            The data that will be passed to registered EventManagers
		 * @throws JSONException
		 *             When the data is not valid
		 * 
		 * @see Event
		 */
		public void parseResponse(JSONObject object, Map<String, Object> data)
				throws JSONException;
	}
}
