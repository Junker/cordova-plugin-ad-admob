//Copyright (c) 2014 Sang Ki Kwon (Cranberrygame)
//Email: cranberrygame@yahoo.com
//Homepage: http://cranberrygame.github.io
//License: MIT (http://opensource.org/licenses/MIT)
package com.cranberrygame.cordova.plugin.ad.admob;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import android.app.Activity;
import android.util.Log;
//
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.os.Build;
import android.provider.Settings;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.os.Handler;
//
import java.util.*;//Random

interface Plugin {
	public CordovaWebView getWebView();
	public CordovaInterface getCordova();
	public CallbackContext getCallbackContextKeepCallback();
}

interface PluginDelegate {
	public void _setUp(String bannerAdUnit, String interstitialAdUnit, String rewardedVideoAdUnit, boolean isOverlap, boolean isTest);
	public void _preloadBannerAd();
	public void _showBannerAd(String position, String size);
	public void _reloadBannerAd();
	public void _hideBannerAd();
	public void _preloadInterstitialAd();
	public void _showInterstitialAd();
	public void _preloadRewardedVideoAd();
	public void _showRewardedVideoAd();	
    public void onPause(boolean multitasking);
    public void onResume(boolean multitasking);
    public void onDestroy();
}

public class AdMobPlugin extends CordovaPlugin implements PluginDelegate, Plugin {
	protected static final String LOG_TAG = "AdMobPlugin";	
	protected CallbackContext callbackContextKeepCallback;
	//
	protected PluginDelegate pluginDelegate;
	//
	public String email;
	public String licenseKey;
	public boolean validLicenseKey;
	protected String TEST_BANNER_AD_UNIT = "ca-app-pub-3940256099942544/6300978111";
	protected String TEST_INTERSTITIAL_AD_UNIT = "ca-app-pub-3940256099942544/1033173712";	
	protected String TEST_REWARDED_VIDEO_AD_UNIT = "ca-app-pub-3940256099942544/5224354917";	


    @Override
	public void pluginInitialize() {
		super.pluginInitialize();
		//
    }	
	
	//@Override
	//public void onCreate(Bundle savedInstanceState) {//build error
	//	super.onCreate(savedInstanceState);
	//	//
	//}
	
	//@Override
	//public void onStart() {//build error
	//	super.onStart();
	//	//
	//}
	
  	@Override
    public void onPause(boolean multitasking) {		
        super.onPause(multitasking);
		pluginDelegate.onPause(multitasking);		
    }
      
    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        pluginDelegate.onResume(multitasking);
    }
  	
	//@Override
	//public void onStop() {//build error
	//	super.onStop();
	//	//
	//}
	
    @Override
    public void onDestroy() {
        super.onDestroy();
		pluginDelegate.onDestroy();		
    }
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		
		if (action.equals("setUp")) {
			setUp(action, args, callbackContext);

			return true;
		}			
		else if (action.equals("preloadBannerAd")) {
			preloadBannerAd(action, args, callbackContext);
			
			return true;
		}
		else if (action.equals("showBannerAd")) {
			showBannerAd(action, args, callbackContext);

			return true;
		}
		else if (action.equals("reloadBannerAd")) {
			reloadBannerAd(action, args, callbackContext);
			
			return true;
		}			
		else if (action.equals("hideBannerAd")) {
			hideBannerAd(action, args, callbackContext);
			
			return true;
		}
		else if (action.equals("preloadInterstitialAd")) {
			preloadInterstitialAd(action, args, callbackContext);
			
			return true;
		}
		else if (action.equals("showInterstitialAd")) {
			showInterstitialAd(action, args, callbackContext);
						
			return true;
		}
		else if (action.equals("preloadRewardedVideoAd")) {
			preloadRewardedVideoAd(action, args, callbackContext);
			
			return true;
		}
		else if (action.equals("showRewardedVideoAd")) {
			showRewardedVideoAd(action, args, callbackContext);
						
			return true;
		}		
		
		return false; // Returning false results in a "MethodNotFound" error.
	}
	

	private void setUp(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		//Activity activity=cordova.getActivity();
		//webView
		//args.length()
		//args.getString(0)
		//args.getString(1)
		//args.getInt(0)
		//args.getInt(1)
		//args.getBoolean(0)
		//args.getBoolean(1)
		//JSONObject json = args.optJSONObject(0);
		//json.optString("bannerAdUnit")
		//json.optString("interstitialAdUnit")
		//JSONObject inJson = json.optJSONObject("inJson");
		//final String bannerAdUnit = args.getString(0);
		//final String interstitialAdUnit = args.getString(1);				
		//final boolean isOverlap = args.getBoolean(2);				
		//final boolean isTest = args.getBoolean(3);
		//final String[] zoneIds = new String[args.getJSONArray(4).length()];
		//for (int i = 0; i < args.getJSONArray(4).length(); i++) {
		//	zoneIds[i] = args.getJSONArray(4).getString(i);
		//}			
		//Log.d(LOG_TAG, String.format("%s", bannerAdUnit));			
		//Log.d(LOG_TAG, String.format("%s", interstitialAdUnit));
		//Log.d(LOG_TAG, String.format("%b", isOverlap));
		//Log.d(LOG_TAG, String.format("%b", isTest));		
		final String bannerAdUnit = args.getString(0);
		final String interstitialAdUnit = args.getString(1);				
		final String rewardedVideoAdUnit = args.getString(2);				
		final boolean isOverlap = args.getBoolean(3);				
		final boolean isTest = args.getBoolean(4);				
		Log.d(LOG_TAG, String.format("%s", bannerAdUnit));			
		Log.d(LOG_TAG, String.format("%s", interstitialAdUnit));
		Log.d(LOG_TAG, String.format("%s", rewardedVideoAdUnit));
		Log.d(LOG_TAG, String.format("%b", isOverlap));
		Log.d(LOG_TAG, String.format("%b", isTest));
		
		callbackContextKeepCallback = callbackContext;
		
		if(isOverlap)
			pluginDelegate = new AdMobOverlap(this);
		else
			pluginDelegate = new AdMobSplit(this);
		
		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				_setUp(bannerAdUnit, interstitialAdUnit, rewardedVideoAdUnit, isOverlap, isTest);
			}
		});
	}
	
	private void preloadBannerAd(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		cordova.getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				_preloadBannerAd();
			}
		});
	}

	private void showBannerAd(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		final String position = args.getString(0);
		final String size = args.getString(1);
		Log.d(LOG_TAG, String.format("%s", position));
		Log.d(LOG_TAG, String.format("%s", size));
	
		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				_showBannerAd(position, size);
			}
		});
	}

	private void reloadBannerAd(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		cordova.getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				_reloadBannerAd();
			}
		});
	}
	
	private void hideBannerAd(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		cordova.getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				_hideBannerAd();
			}
		});
	}

	private void preloadInterstitialAd(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		cordova.getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				_preloadInterstitialAd();
			}
		});
	}

	private void showInterstitialAd(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		cordova.getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				_showInterstitialAd();
			}
		});
	}
	
	private void preloadRewardedVideoAd(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		cordova.getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				_preloadRewardedVideoAd();
			}
		});
	}

	private void showRewardedVideoAd(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		cordova.getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				_showRewardedVideoAd();
			}
		});
	}

	//cranberrygame start: Plugin

	public CordovaWebView getWebView() {
		return webView;
	}

	public CordovaInterface getCordova() {
		return cordova;
	}

	public CallbackContext getCallbackContextKeepCallback() {
		return callbackContextKeepCallback;
	}

	public void _setUp(String bannerAdUnit, String interstitialAdUnit, String rewardedVideoAdUnit, boolean isOverlap, boolean isTest) {
			if (isTest) {				
				bannerAdUnit = TEST_BANNER_AD_UNIT;
				interstitialAdUnit = TEST_INTERSTITIAL_AD_UNIT;
				rewardedVideoAdUnit = TEST_REWARDED_VIDEO_AD_UNIT;
			}
			
		pluginDelegate._setUp(bannerAdUnit, interstitialAdUnit, rewardedVideoAdUnit, isOverlap, isTest);
	}
	
	public void _preloadBannerAd() {
		pluginDelegate._preloadBannerAd();           	
	}
		
	public void _showBannerAd(String position, String size) {
		pluginDelegate._showBannerAd(position, size);		
	}
	
	public void _reloadBannerAd() {
		pluginDelegate._reloadBannerAd();
	}
	
	public void _hideBannerAd() {
		pluginDelegate._hideBannerAd();
	}
		
	public void _preloadInterstitialAd() {
		pluginDelegate._preloadInterstitialAd();
	}
	
	public void _showInterstitialAd() {
		pluginDelegate._showInterstitialAd();
	}
	
	public void _preloadRewardedVideoAd() {
		pluginDelegate._preloadRewardedVideoAd();
	}
	
	public void _showRewardedVideoAd() {
		pluginDelegate._showRewardedVideoAd();
	}	

	//cranberrygame end: AdMobPluginDelegate
}

