package org.goldfish.minesweeper_android_01;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.google.firebase.FirebaseApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.TimerTask;
import java.util.UUID;

public class UpLoader extends TimerTask {
	static int count = 9555;
	final AppCompatActivity activity;
	final String URL = "jdbc:mysql://112.124.63.147:3306/userdb";
	final String user = "userdb";
	final String password = "Bdbg2181";
	final String tableName = "test1";
	final int delay_sec = 30;
	final String query;
	final String insert;
	String DEVICE_ID;

	public UpLoader(AppCompatActivity activity) {
		query = "SELECT * FROM " + tableName;
		String insert_temp = "INSERT INTO " + tableName;
		insert_temp += " (longtitude," + "latitude," + "device_id) ";
		insert_temp += "VALUES (?,?,?)";
		insert = insert_temp;
		this.activity = activity;
		DEVICE_ID = getUUID(activity);
		System.out.println(DEVICE_ID);
		FirebaseApp.initializeApp(activity);
	}


	public String getUUID(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
			"device_prefs", Context.MODE_PRIVATE);
		String uuid = sharedPreferences.getString("device_id", null);

		if (uuid == null) {
			System.out.println("writing uuid");
			uuid = UUID.randomUUID().toString();
			sharedPreferences.edit().putString("device_id", uuid).apply();
		}


		return uuid;
	}

	@Override
	public void run() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Properties props = new Properties();
			props.setProperty("useSSL", false + "");
			props.setProperty("useServerPrepStmts", String.valueOf(false));
			props.setProperty("loggerLevel", "DEBUG");
			props.setProperty("user", user);
			props.setProperty("password", password);

			try (Connection connection = DriverManager.getConnection(URL,
				props)) {
				try (PreparedStatement statement =
					     connection.prepareStatement(query)) {
					try (ResultSet resultSet = statement.executeQuery()) {
						while (resultSet.next()) {
							System.out.print(resultSet.getObject(1) + "\t");
						}
					}
				}
				try (PreparedStatement statement =
					     connection.prepareStatement(insert)) {

					AMapLocation location =
						AMAPRequestSender.getInstance().getLocation();
					if (location == null) {
						System.out.println("location is null");
						return;
					}
					double longtitude = location.getLongitude();
					double latitude = location.getLatitude();
					statement.setDouble(1, longtitude);
					statement.setDouble(2, latitude);
					statement.setString(3, DEVICE_ID);
					statement.executeUpdate();
					System.out.println("inserted");
				}
			}
		} catch (SQLException | ClassNotFoundException exception) {
			exception.printStackTrace(System.out);
		}

	}

}
