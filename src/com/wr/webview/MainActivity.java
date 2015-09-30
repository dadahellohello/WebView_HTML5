package com.wr.webview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.samsung.zr.webviewtest.R;

public class MainActivity extends Activity {

	private WebView mWebView;

    // WebViewClient��Ҫ����WebView�������֪ͨ�������¼�
	private WebViewClient mWebViewClient = new WebViewClient() {
		// ����ҳ�浼��
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			mWebView.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
	};
	
    //	WebChromeClient��Ҫ����WebView����Javascript�ĶԻ�����վͼ�ꡢ��վtitle�����ؽ��ȵ�
	private WebChromeClient mChromeClient = new WebChromeClient() {

		private View myView = null;
		private CustomViewCallback myCallback = null;

		// ����Ȩ�� ����WebChromeClinet��ʵ�֣�
		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
			super.onGeolocationPermissionsShowPrompt(origin, callback);
		}

		// �������ݿ����������WebChromeClinet��ʵ�֣�
		@Override
		public void onExceededDatabaseQuota(String url,
				String databaseIdentifier, long currentQuota,
				long estimatedSize, long totalUsedQuota,
				WebStorage.QuotaUpdater quotaUpdater) {

			quotaUpdater.updateQuota(estimatedSize * 2);
		}

		// ���仺�������
		@Override
		public void onReachedMaxAppCacheSize(long spaceNeeded,
				long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {

			quotaUpdater.updateQuota(spaceNeeded * 2);
		}

		// Android ʹWebView֧��HTML5 Video��ȫ�������ŵķ���
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			if (myCallback != null) {
				myCallback.onCustomViewHidden();
				myCallback = null;
				return;
			}
			ViewGroup parent = (ViewGroup) mWebView.getParent();
			parent.removeView(mWebView);
			parent.addView(view);
			myView = view;
			myCallback = callback;
			mChromeClient = this;
		}

		@Override
		public void onHideCustomView() {
			if (myView != null) {
				if (myCallback != null) {
					myCallback.onCustomViewHidden();
					myCallback = null;
				}

				ViewGroup parent = (ViewGroup) myView.getParent();
				parent.removeView(myView);
				parent.addView(mWebView);
				myView = null;
			}
		}
	};

	private void initSettings() {

		requestWindowFeature(Window.FEATURE_NO_TITLE); // ���ñ�������ʽ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // ȫ��

		setContentView(R.layout.activity_main);
		mWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = mWebView.getSettings();

		webSettings.setJavaScriptEnabled(true); // ����Javascript�ű�
		webSettings.setDomStorageEnabled(true); // ����localStorage ��// essionStorage
		webSettings.setAppCacheEnabled(true); // ����Ӧ�ó��򻺴�
		String appCacheDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
		webSettings.setAppCachePath(appCacheDir);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		webSettings.setAppCacheMaxSize(1024 * 1024 * 1);// ���û����С���������1M
		webSettings.setAllowFileAccess(true);
		webSettings.setRenderPriority(RenderPriority.HIGH); // ������Ⱦ���ȼ�
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

		mWebView.setWebChromeClient(mChromeClient);
		mWebView.setWebViewClient(mWebViewClient);
	}

	// �����ҳ��ʷ��¼ goBack()��goForward()
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.initSettings();

		mWebView.loadUrl("http://192.168.1.230/html/player_vod_A000000.html");
		// mWebView.loadUrl("http://192.168.1.230/html/player_m3u8_A000000.html");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (null != mWebView) {
			mWebView.onPause();
		}
		super.onPause();
	}

}