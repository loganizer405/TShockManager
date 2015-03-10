package co.tshock.manager.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "servers")
public class Server implements Parcelable {
	@DatabaseField(generatedId = true, columnName = "_id")
	private int id;

	@DatabaseField
	private String displayName;

	@DatabaseField
	private String ip;

	@DatabaseField
	private int port;

	@DatabaseField
	private String username;

	@DatabaseField
	private String password;

	@DatabaseField(columnName = "last_token")
	private String token;

	/**
	 * Construct a new server
	 * 
	 * @param displayName
	 * @param ip
	 * @param port
	 */
	public Server(String displayName, String ip, int port, String username,
			String password) {
		super();
		this.displayName = displayName;
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	/**
	 * Construct a new server with empty values
	 */
	public Server() {
		super();
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

/**
	 * Gets a human readable {@link String] of IP and port combined
	 * @return the host
	 */
	public String getHost() {
		return String.format("%s:%d", ip, port);
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Server [id=" + id + ", displayName=" + displayName + ", ip="
				+ ip + ", port=" + port + ", username=" + username
				+ ", password=" + password + ", getHost()=" + getHost() + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + id;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + port;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Server))
			return false;
		Server other = (Server) obj;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (id != other.id)
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (port != other.port)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	protected Server(Parcel in) {
		id = in.readInt();
		displayName = in.readString();
		ip = in.readString();
		port = in.readInt();
		username = in.readString();
		password = in.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(displayName);
		dest.writeString(ip);
		dest.writeInt(port);
		dest.writeString(username);
		dest.writeString(password);
	}

	public static final Parcelable.Creator<Server> CREATOR = new Parcelable.Creator<Server>() {
		public Server createFromParcel(Parcel in) {
			return new Server(in);
		}

		public Server[] newArray(int size) {
			return new Server[size];
		}
	};

}
