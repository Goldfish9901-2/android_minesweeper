package org.goldfish.minesweeper_android_01;

import static org.goldfish.minesweeper_android_01.Controller.thrower;

import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * {@code AMAPRequestSender}单例模式 <br/>
 * 用于发送高德地图定位请求
 */
public class AMAPRequestSender {
	/**
	 * 单例模式
	 */
	private static AMAPRequestSender instance = null;
	private final EntranceActivity activity;
	/**
	 * 高德地图定位客户端
	 */
	private AMapLocationClient locationClient = null;
	private AMapLocation location;

	/**
	 * @param context {@code EntranceActivity}实例
	 */

	private AMAPRequestSender(EntranceActivity context) {
		this.activity = context;
		location = null;
		try {
			AMapLocationClient.updatePrivacyAgree(context, true);
			AMapLocationClient.updatePrivacyShow(context, true, true);

			locationClient = new AMapLocationClient(context);
			AMapLocationClientOption locationOption =
				new AMapLocationClientOption();
			locationOption.setOnceLocationLatest(true);
			locationOption.setBeidouFirst(true);
			locationClient.setLocationOption(locationOption);
			locationClient.setLocationListener(new GFLocationListener());
			locationClient.startLocation();
		} catch (Exception ignored) {
		}
	}

	/**
	 * 单例模式
	 * 该重载方法用于构造单例
	 *
	 * @param context {@code EntranceActivity}实例
	 * @return {@code AMAPRequestSender}实例
	 */

	public static AMAPRequestSender getInstance(EntranceActivity context) {
		if (instance == null) try {
			instance = new AMAPRequestSender(context);
		} catch (Exception e) {
			String message = "AMAPRequestSender: " + "cannot " + "create " +
				"instance";
			Log.w(AMAPRequestSender.class.toString(), message);
		}
		return (instance);
	}

	/**
	 * 单例模式
	 * 该重载方法用于获取单例
	 *
	 * @return {@code AMAPRequestSender}实例
	 * @throws NullPointerException 如果单例未创建
	 */

	public static AMAPRequestSender getInstance() {
		if (instance == null) {
			String message = "AMAPRequestSender: " + "instance " + "not " +
				"created";
			Log.w(thrower, message);
			throw new NullPointerException(message);
		}
		return instance;
	}

	public AMapLocation getLocation() {
		return location;
	}

	/**
	 * 开始定位
	 */

	public void requestLocation() {
		try {
			locationClient.startLocation();
		} catch (Exception e) {
			String message = "AMAPRequestSender: " + e.getMessage();
			Log.w(thrower, message, e);
		}
	}

	private class GFLocationListener implements AMapLocationListener {

		@Override
		public synchronized void onLocationChanged(AMapLocation location) {
			if (location == null) {
				Log.w(thrower, "onLocationChanged: " + "null");
				return;
			}
			if (location.getErrorCode() != 0) {
				Log.w(thrower, "onLocationChanged: " + "error");
				return;
			}
			try {
				EntranceRecorder.getInstance().updateLocation(location);
				activity.updateListeners();
				locationClient.stopLocation();
				AMAPRequestSender.this.location = location;
				System.out.println("location: " + location);
			} catch (Exception e) {
				Log.w(GFLocationListener.class.toString(), "onLocationChanged"
					, e);
			}
		}
	}
}


