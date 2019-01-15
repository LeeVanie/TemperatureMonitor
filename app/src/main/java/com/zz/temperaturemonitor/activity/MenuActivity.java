package com.zz.temperaturemonitor.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zz.temperaturemonitor.R;
import com.zz.temperaturemonitor.base.BaseActivity;
import com.zz.temperaturemonitor.utils.SharedPreferencesUtils;
import com.zz.temperaturemonitor.view.CustomPrograss;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @类名: ${type_name}
 * @功能描述:
 * @作者: ${user}
 * @时间: ${date}
 * @最后修改者:
 * @最后修改内容:
 */
public class MenuActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.ic_title)
    TextView icTitle;
    @BindView(R.id.anim_layout)
    LinearLayout animLayout;
    @BindView(R.id.history)
    TextView history;
    @BindView(R.id.temp_threashod)
    TextView tempThreashod;
    @BindView(R.id.btn_bj)
    ToggleButton btnBj;
    @BindView(R.id.scan_device)
    TextView scanDevice;
    @BindView(R.id.main)
    LinearLayout main;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_menu;
    }

    @Override
    protected void initInjector() {
        icTitle.setText("设置");
    }

    @Override
    protected void initEventAndData() {
        btnBj.setChecked(SharedPreferencesUtils.getBoolean(this, "btnBj", false));
        btnBj.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferencesUtils.setBoolean(MenuActivity.this, "btnBj", isChecked);
            }
        });
    }

    @OnClick({R.id.back, R.id.history, R.id.temp_threashod, R.id.btn_bj, R.id.scan_device})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.history:
                startActivity(new Intent(this, HistoryActivity.class));
                finish();
                break;
            case R.id.temp_threashod:
                showPopuPicture();
                break;
            case R.id.scan_device:
                //查找设备
                CustomPrograss.show(this, "正在扫描附近设备...", true, null);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * 显示图片弹窗
     */
    PopupWindow window = null;

    private void showPopuPicture() {

        View popupView = this.getLayoutInflater().inflate(R.layout.edit_dialog, null);
        final EditText editText = (EditText) popupView.findViewById(R.id.edit);
        Button btn_cancle = (Button) popupView.findViewById(R.id.cancle);
        Button btn_ok = (Button) popupView.findViewById(R.id.ok);
        window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        window.showAtLocation(main, Gravity.CENTER, 0, 0);
        window.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 如果点击了popupwindow的外部，popupwindow也会消失
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    window.dismiss();
                    return true;
                }
                return false;
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                window.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                String temp = editText.getText().toString();
                if (null == temp || "".equals(temp)){
                    return;
                }
                tempThreashod.setText(temp + "℃");
                SharedPreferencesUtils.saveString(MenuActivity.this, "TEMPTHREASHOD", temp + "℃");
            }
        });

    }

}
