package com.example.ariix.sharetest;

import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Dialog dialog;
    private IWXAPI api;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        api = WXAPIFactory.createWXAPI(this, ShareConsts.SHARE_WX_APP_ID);
        setContentView(R.layout.activity_main);
        Button shareButton = findViewById(R.id.shareToButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserPortraitPopupWindow();
            }
        });
    }



    /**
     * show user portrait popup window.
     *
     */
    private void showUserPortraitPopupWindow(){
        dialog = new Dialog(MainActivity.this, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(MainActivity.this).inflate(R.layout.share_dialog, null);
        //初始化控件
        View weChat = inflate.findViewById(R.id.ic_we_chat);
        weChat.setOnClickListener(this);
        setSameWidth(weChat);


        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if(dialogWindow == null){
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_we_chat:
                WXWebpageObject webPage = new WXWebpageObject();
                webPage.webpageUrl = "http://ariix.com/blog/top-mlm-for-2017-by-business-for-home";

                WXMediaMessage msg = new WXMediaMessage(webPage);
                msg.title = "ARIIX RECOGNIZED AS A LEGITIMATE AND STABLE DIRECT SELLING COMPANY BY BUSINESS FOR";
                msg.description = "We've got the right stuff! Business for Home ranks ARIIX one of top 100 MLM Companies.";

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("text");
                req.message = msg;
                req.scene = mTargetScene;
                api.sendReq(req);
                break;
            default:
                break;
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void setSameWidth(View view) {
        int windowWidth = WindowUtils.getScreenWidth(MainActivity.this);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = windowWidth / 5;
        params.width = windowWidth / 5;
        view.setLayoutParams(params);
    }
}
