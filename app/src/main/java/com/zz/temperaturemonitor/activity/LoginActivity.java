package com.zz.temperaturemonitor.activity;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;
import com.zz.temperaturemonitor.R;
import com.zz.temperaturemonitor.base.BaseActivity;
import com.zz.temperaturemonitor.utils.ToastUtils;
import com.zz.temperaturemonitor.view.CustomPrograss;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @类名: ${type_name}
 * @功能描述: LoginActivity
 * @作者: ${user}
 * @时间: ${date}
 * @最后修改者:
 * @最后修改内容:
 */
public class LoginActivity extends BaseActivity {


    @BindView(R.id.login_name)
    EditText loginName;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.login_register)
    Button loginRegister;
    @BindView(R.id.login_login)
    Button loginLogin;
    
    private String loginname;
    private String loginpsd;
    
    public static final int REQUEST_PERMISSIONS = 0;
    
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initInjector() {
        MPermissions.requestPermissions(this, REQUEST_PERMISSIONS,
                Manifest.permission.READ_PHONE_STATE, 
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {

        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    
    @PermissionGrant(REQUEST_PERMISSIONS)
    public void requestSdcardSuccess() {
        
    }

    @PermissionDenied(REQUEST_PERMISSIONS)
    public void requestSdcardFailed() {
        finish();
    }
    
    
    @OnClick({R.id.login_register, R.id.login_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_register:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.login_login:
                if (!isLoginEmpty()){
                    //进入登录页面
                    CustomPrograss.show(this, "登录中...", true, null);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                break;
        }
    }

    public boolean isLoginEmpty() {
        loginname = loginName.getText().toString().trim();
        loginpsd = loginPassword.getText().toString().trim();
        if ("".equals(loginname) && "".equals(loginpsd)) {
            ToastUtils.showToast(this, "请填写完整...");
            return false;
        }
        return true;
    }

    //记录用户首次点击返回键的时间
    private long firstTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                Toast.makeText(this, "再次点击返回键将会退出应用...", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
