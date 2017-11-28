package com.two.emergencylending.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.two.emergencylending.utils.ShareUtil;
import com.two.emergencylending.utils.ToastAlone;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		ShareUtil.getInstance().getApi().handleIntent(getIntent(), this);
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		ShareUtil.getInstance().getApi().handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq baseReq) {
		ToastAlone.showShortToast(getApplicationContext(), "收到响应");
	}

	@Override
	public void onResp(BaseResp baseResp) {
		switch (baseResp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				ToastAlone.showShortToast(getApplicationContext(), "分享成功");
				// 分享成功
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				ToastAlone.showShortToast(getApplicationContext(), "分享取消");
				// 分享取消
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				ToastAlone.showShortToast(getApplicationContext(), "分享拒绝");
				// 分享拒绝
				break;
		}
		this.finish(); // 此功能为点击返回某程序，非停在微信的时候调用
	}
}
