package com.xml.parsing.demo.activities.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author dipenp
 *
 */
public class NetworkUtils {

	/**
	 * @param _context
	 * @return checking whether network is available or not
	 */
	public static boolean isNetworkAvailable(Context _context) {
		
		try {
			ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (connectivity != null) {
				NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
				if (networkInfo != null && (networkInfo.isConnected()) && networkInfo.isAvailable()) {
					return true;
				}

			}	
		} catch (Exception e) {}
		
		return false;
	}
}
