package co.tshock.manager.api;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import co.tshock.manager.data.models.Server;
import co.tshock.manager.data.models.User;
import co.tshock.manager.events.Event;
import co.tshock.manager.events.EventType;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Class to make requests to the TShock API
 * 
 * @author koesie10
 */
public class TShockApi {
	private static Server server;
	private static final String TAG = TShockApi.class.getSimpleName();

	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void tokentest() {
		get(EventType.TOKENTEST);
	}

	/**
	 * Creates a token
	 * 
	 * This token is needed to authenticate most of the other calls, so this
	 * should be called first.
	 */
	public static void createToken() {
		get(EventType.CREATE_TOKEN, new TShockResponseHandler.DataProcessor() {
			@Override
			public void parseResponse(JSONObject object,
					Map<String, Object> data) throws JSONException {
				String token = object.getString("token");
				server.setToken(token);
				data.put("token", token);
				data.put("username", server.getUsername());
				data.put("password", server.getPassword());
				Log.i(TAG, String.format("Successfully authenticated with %s",
						server.getToken()));
			}
		}, server.getUsername(), server.getPassword());
	}

	/**
	 * This call destroys the token that is currently in use
	 * 
	 * The token has been made with {@link #createToken()}
	 */
	public static void destroyToken() {
		get(EventType.DESTROY_TOKEN, new TShockResponseHandler.DataProcessor() {

			@Override
			public void parseResponse(JSONObject object,
					Map<String, Object> data) throws JSONException {
				server.setToken(null);
				Log.i(TAG, "Successfully destroyed token");
			}
		}, server.getToken());
	}

	/**
	 * Asks the server for the status of the server.
	 * 
	 * 
	 * This returns the following values in the data map of the {@link Event}:
	 * <ul>
	 * <li>String name</li>
	 * <li>int port</li>
	 * <li>int playerCount</li>
	 * <li>int maxPlayer</li>
	 * <li>String world</li>
	 * <li>String uptime</li>
	 * <li>boolean serverPassword</li>
	 * </ul>
	 * 
	 * @see co.tshock.manager.events.Event#getData()
	 */
	public static void status() {
		get(EventType.SERVER_STATUS, new TShockResponseHandler.DataProcessor() {

			@Override
			public void parseResponse(JSONObject object,
					Map<String, Object> data) throws JSONException {
				data.put("name", object.getString("name"));
				data.put("port", object.getInt("port"));
				data.put("playerCount", object.getInt("playercount"));
				data.put("maxPlayer", object.getInt("maxplayers"));
				data.put("world", object.getString("world"));
				data.put("uptime", object.getString("uptime"));
				data.put("serverPassword", object.getBoolean("serverpassword"));
			}
		});
	}

	/**
	 * Broadcasts a message to everyone on the server.
	 * 
	 * @param message
	 *            The message to broadcast
	 */
	public static void serverBroadcast(final String message) {
		RequestParams params = new RequestParams();
		params.put("msg", message);
		get(EventType.SERVER_BROADCAST,
                new TShockResponseHandler.DataProcessor() {

                    @Override
                    public void parseResponse(JSONObject object,
                                              Map<String, Object> data) throws JSONException {
                        data.put("message", message);

                    }
                }, params);
	}

    public static void serverRawCmd(final String cmd) {
        RequestParams params = new RequestParams();
        params.put("cmd", cmd);
        get(EventType.SERVER_RAWCMD, new TShockResponseHandler.DataProcessor() {
            @Override
            public void parseResponse(JSONObject object, Map<String, Object> data) throws JSONException {
                data.put("cmd", cmd);
            }
        }, params);
    }
	
	/**
	 * Turns off the server, you may get an ERROR when the server shuts down too early and it cannot
     * give a response.
	 */
	public static void serverOff() {
		RequestParams params = new RequestParams();
		params.put("confirm", "true");
		get(EventType.SERVER_OFF, params);
	}
	
	/**
	 * Restart off the server, you may get an ERROR when the server shuts down too early and
     * it cannot give a response.
	 */
	public static void serverRestart() {
		RequestParams params = new RequestParams();
		params.put("confirm", "true");
		get(EventType.SERVER_RESTART, params);
	}

    public static void userActiveList() {
        get(EventType.USER_ACTIVE_LIST, new TShockResponseHandler.DataProcessor() {

            @Override
            public void parseResponse(JSONObject object, Map<String, Object> data) throws JSONException {
                String rawActiveUsers = object.getString("activeusers");
                String[] activeUsers = rawActiveUsers.split("\t");
                ArrayList<User> users = new ArrayList<User>();
                for (String activeUser : activeUsers) {
                    users.add(new User(activeUser));
                }
                data.put("activeUsers", users);
            }
        });
    }

    public static void userRead(String name, final DirectResponseHandler handler) {
        get(EventType.USER_READ, new TShockResponseHandler.DataProcessor() {
            @Override
            public void parseResponse(JSONObject object, Map<String, Object> data)
                    throws JSONException {
                String group = object.getString("group");
                int id = object.getInt("id");
                String name = object.getString("name");
                data.put("user", new User(group, id, name));
                if (handler != null) {
                    handler.onResponse(data);
                }
            }
        });
    }

	
	

	/**
	 * @return the server
	 */
	public static Server getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public static void setServer(Server server) {
		TShockApi.server = server;
	}

	/**
	 * Gets the current base URL generated on the fly with the help of the IP
	 * and port
	 * 
	 * @return the base URL to make requests to without trailing slash
	 * @see #setServer(Server server)
	 * @see #getServer()
	 */
	public static String getBaseUrl() {
		return String.format("http://%s:%d", server.getIp(), server.getPort());
	}

	public static void get(EventType type,
			TShockResponseHandler.DataProcessor dataProcessor,
			RequestParams params, Object... args) {
		get(type.getUrl(args), params, new TShockResponseHandler(type,
				dataProcessor));
	}

	public static void get(EventType type,
			TShockResponseHandler.DataProcessor dataProcessor, Object... args) {
		get(type.getUrl(args), null, new TShockResponseHandler(type,
				dataProcessor));
	}

	public static void get(EventType type,
			TShockResponseHandler.DataProcessor dataProcessor,
			RequestParams params) {
		get(type.getUrl(), params, new TShockResponseHandler(type,
				dataProcessor));
	}

	public static void get(EventType type,
			TShockResponseHandler.DataProcessor dataProcessor) {
		get(type.getUrl(), null, new TShockResponseHandler(type, dataProcessor));
	}

	public static void get(EventType type, RequestParams params) {
		get(type.getUrl(), params, new TShockResponseHandler(type));
	}

	public static void get(EventType type, Object... args) {
		get(type.getUrl(args), null, new TShockResponseHandler(type));
	}

	protected static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		url = getAbsoluteUrl(url);
		if (params == null) {
			params = new RequestParams();
		}
		if (server.getToken() != null && !server.getToken().equals("")) {
			params.put("token", server.getToken());
		}
		Log.i(TAG,
				"Sending a new request: (url=" + url + ", params="
						+ params.toString() + ",responseHandler="
						+ responseHandler.getClass().getSimpleName() + ")");
		client.get(url, params, responseHandler);
	}

	@SuppressLint("DefaultLocale")
	private static String getAbsoluteUrl(String relativeUrl) {
		return getBaseUrl() + relativeUrl;
	}

    public static interface DirectResponseHandler {
        public void onResponse(Map<String, Object> data);
    }
}
