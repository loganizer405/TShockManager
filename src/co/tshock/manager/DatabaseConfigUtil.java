package co.tshock.manager;

import co.tshock.manager.data.models.Server;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

/**
 * Run this class using your JRE on your development box, not on your Android
 * device!
 * 
 * Add all classes that are put in the database to the classes array
 * 
 * Remove <code>implements {@link android.os.Parcelable}</code> temporarily from
 * {@link co.tshock.manager.data.models.Server} to let this program run correctly
 * 
 * Steps to run this are described at {@linkplain http
 * ://ormlite.com/javadoc/ormlite
 * -core/doc-files/ormlite_4.html#Config-Optimization}
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {
	private static final Class<?>[] classes = new Class[] { Server.class, };

	public static void main(String[] args) throws Exception {
		writeConfigFile("ormlite_config.txt", classes);
	}
}
