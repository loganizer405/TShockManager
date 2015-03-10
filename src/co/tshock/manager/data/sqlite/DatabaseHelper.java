package co.tshock.manager.data.sqlite;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import co.tshock.manager.R;
import co.tshock.manager.data.models.Server;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private final static String TAG = DatabaseHelper.class.getSimpleName();
	public static final String TABLE_SERVER = "servers";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_DISPLAYNAME = "display_name";

	private static final String DATABASE_NAME = "tshock.db";
	/**
	 * Must be updated when changing the database schema
	 */
	private static final int DATABASE_VERSION = 2;

	private RuntimeExceptionDao<Server, Integer> serverDao;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		Log.i(TAG, "onCreate");
		try {
			TableUtils.createTable(connectionSource, Server.class);
		} catch (SQLException e) {
			Log.e(TAG, "Can't create database", e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			Log.i(TAG, "onUpgrade");
			TableUtils.dropTable(connectionSource, Server.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(TAG, "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}
	
	public RuntimeExceptionDao<Server, Integer> getServerDao() {
		if (serverDao == null) {
			serverDao = getRuntimeExceptionDao(Server.class);
		}
		return serverDao;
	}
	
	@Override
	public void close() {
		super.close();
		serverDao = null;
		databaseHelper = null;
	}

	private static DatabaseHelper databaseHelper = null;

	public static DatabaseHelper getInstance(Context context) {
		if (databaseHelper == null) {
			databaseHelper = new DatabaseHelper(context);
		}
		return databaseHelper;
	}

}
