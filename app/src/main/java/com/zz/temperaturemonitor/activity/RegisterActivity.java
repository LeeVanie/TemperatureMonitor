package com.zz.temperaturemonitor.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zz.temperaturemonitor.R;
import com.zz.temperaturemonitor.base.BaseActivity;
import com.zz.temperaturemonitor.utils.CodeUtils;
import com.zz.temperaturemonitor.utils.ToastUtils;
import com.zz.temperaturemonitor.view.CustomPrograss;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @类名: ${type_name}
 * @功能描述: RegisterActivity
 * @作者: ${user}
 * @时间: ${date}
 * @最后修改者:
 * @最后修改内容:
 */
public class RegisterActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.ic_title)
    TextView icTitle;
    @BindView(R.id.anim_layout)
    LinearLayout animLayout;
    @BindView(R.id.register_name)
    EditText registerName;
    @BindView(R.id.register_psd)
    EditText registerPsd;
    @BindView(R.id.et_yzm)
    EditText etYzm;
    @BindView(R.id.image_yzm)
    ImageView imageYzm;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private String codeStr;
    private String name, psd;
    private Bitmap mBitmap;
    
    
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initInjector() {
        icTitle.setText(R.string.text_register);
    }

    @Override
    protected void initEventAndData() {
        mBitmap = CodeUtils.getInstance().createBitmap();
        imageYzm.setImageBitmap(mBitmap);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @OnClick({R.id.back, R.id.image_yzm, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.image_yzm: 
                mBitmap = CodeUtils.getInstance().createBitmap();
                imageYzm.setImageBitmap(mBitmap);
                break;
            case R.id.btn_submit:
                if (isRegister() && yanzhengma()) {
                    //提交注册名、密码
                    CustomPrograss.show(this, "注册中...", true, null);
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }else {
                    return;
                }
                break;
        }
    }


    public boolean yanzhengma() {
        codeStr = etYzm.getText().toString().trim();
        Log.e("codeStr", codeStr);
        if (null == codeStr || TextUtils.isEmpty(codeStr)) {
            ToastUtils.showToast(this, "请输入验证码");
            return false;
        }
        String code = CodeUtils.getInstance().getCode();
        Log.e("code", code);
        if (code.equalsIgnoreCase(codeStr)) {
            ToastUtils.showToast(this, "验证码正确");
            return true;
        } else {
            ToastUtils.showToast(this, "验证码错误");
            etYzm.setText("");
            return false;
        }
    }


    public boolean isRegister(){
        name = registerName.getText().toString().trim();
        psd = registerPsd.getText().toString().trim();
        if ("".equals(name) && "".equals(psd)){
            ToastUtils.showToast(this, "请填写完整...");
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        if (mBitmap.isRecycled()){
            mBitmap.recycle();
        }
        super.onDestroy();
    }
}
