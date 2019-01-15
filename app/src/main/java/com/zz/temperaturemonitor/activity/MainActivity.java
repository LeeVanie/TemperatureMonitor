package com.zz.temperaturemonitor.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zz.temperaturemonitor.R;
import com.zz.temperaturemonitor.utils.CrossAxisScale;
import com.zz.temperaturemonitor.utils.FoldLinePoint;
import com.zz.temperaturemonitor.utils.SharedPreferencesUtils;
import com.zz.temperaturemonitor.utils.VerticalAxisScale;
import com.zz.temperaturemonitor.utils.bottompopfragmentmenu.BottomMenuFragment;
import com.zz.temperaturemonitor.utils.bottompopfragmentmenu.MenuItem;
import com.zz.temperaturemonitor.utils.bottompopfragmentmenu.MenuItemOnClickListener;
import com.zz.temperaturemonitor.view.CircleImageView;
import com.zz.temperaturemonitor.view.CustomPrograss;
import com.zz.temperaturemonitor.view.FoldLineView;
import com.zz.temperaturemonitor.view.TempCircleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fl_content)
    FoldLineView flContent;
    @BindView(R.id.tempCircle)
    TempCircleView tempCircle;
    @BindView(R.id.temp_max)
    TextView tempMax;
    @BindView(R.id.battery)
    TextView battery;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.stop_test)
    Button stopTest;
    @BindView(R.id.user_aventure)
    CircleImageView userAventure;
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.temp_threashod)
    TextView tempThreashod;

    private BatteryReceiver batteryReceiver;
    private IntentFilter intentFilter;
    private List<CrossAxisScale> crosssAxis = new ArrayList<CrossAxisScale>();
    private List<VerticalAxisScale> verticalAxis = new ArrayList<VerticalAxisScale>();
    private List<FoldLinePoint> foldLinePoints = new ArrayList<FoldLinePoint>();// 折线点
    private int currentDegree = 10; //当前温度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);
        ButterKnife.bind(this);

        initTempCircle();
        initTemp();

        //设置头像
        //userAventure.setBitmap();
        tempThreashod.setText("报警值：" + SharedPreferencesUtils.getString(this,
                "TEMPTHREASHOD", "37℃"));

        //注册广播接受者java代码
        intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //创建广播接受者对象
        batteryReceiver = new BatteryReceiver();
        //注册receiver
        registerReceiver(batteryReceiver, intentFilter);
    }

    private void initTempCircle() {

        tempCircle.setMinDegree(10);
        new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (MainActivity.class) {
                    for (int i = 10; i < 101; i++) {//因为270/3=90
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            // TODO 自动生成的 catch 块
                            e.printStackTrace();
                        }
                        tempCircle.setMaxDegree(i);
                        tempCircle.postInvalidate();
                    }
                }
            }
        }).start();

        //设置当前温度
        for (int j = 10; j <= (currentDegree - 10) * 3 + 10; j++) {
            if (j <= (currentDegree - 10) * 3 + 10) {//23*3+10=79
                tempCircle.setCurrentDegree(j);
            }
        }
        tempCircle.postInvalidate();
    }


    public void initTemp() {
        crosssAxis.add(new CrossAxisScale("8:00", 20));
        crosssAxis.add(new CrossAxisScale("9:00", 40));
        crosssAxis.add(new CrossAxisScale("10:00", 60));
        crosssAxis.add(new CrossAxisScale("11:00", 80));
        crosssAxis.add(new CrossAxisScale("12:00", 100));
        
        verticalAxis.add(new VerticalAxisScale("35℃", 20));
        verticalAxis.add(new VerticalAxisScale("36℃", 40));
        verticalAxis.add(new VerticalAxisScale("37℃", 60));
        verticalAxis.add(new VerticalAxisScale("38℃", 80));
        verticalAxis.add(new VerticalAxisScale("39℃", 100));

        foldLinePoints.add(new FoldLinePoint(30, "20℃", "12:00", true));
        foldLinePoints.add(new FoldLinePoint(50, "35℃", "11:00", true));
        foldLinePoints.add(new FoldLinePoint(40, "35℃", "10:00", true));
        foldLinePoints.add(new FoldLinePoint(60, "39℃", "9:00", true));
        foldLinePoints.add(new FoldLinePoint(90, "36℃", "8:00", true));
        foldLinePoints.add(new FoldLinePoint(30, "37℃", "7:00", true));
        foldLinePoints.add(new FoldLinePoint(30, "36℃", "12:00", true));
        foldLinePoints.add(new FoldLinePoint(50, "35℃", "11:00", true));
        foldLinePoints.add(new FoldLinePoint(40, "37℃", "10:00", true));
        foldLinePoints.add(new FoldLinePoint(60, "37℃", "9:00", true));
        foldLinePoints.add(new FoldLinePoint(90, "39℃", "8:00", true));
        foldLinePoints.add(new FoldLinePoint(30, "38℃", "7:00", true));
        foldLinePoints.add(new FoldLinePoint(30, "38℃", "12:00", true));
        foldLinePoints.add(new FoldLinePoint(50, "39℃", "11:00", true));
        foldLinePoints.add(new FoldLinePoint(40, "35℃", "10:00", true));
        foldLinePoints.add(new FoldLinePoint(60, "36℃", "9:00", true));
        foldLinePoints.add(new FoldLinePoint(90, "38℃", "8:00", true));
        foldLinePoints.add(new FoldLinePoint(30, "35℃", "7:00", true));
        flContent.initData(null, verticalAxis, foldLinePoints);
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


    @OnClick({R.id.menu, R.id.stop_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu:
                BottomMenuFragment bottomMenuFragment = new BottomMenuFragment();

                List<MenuItem> menuItemList = new ArrayList<MenuItem>();
                MenuItem menuItem1 = new MenuItem();
                menuItem1.setText("设置");
                menuItem1.setStyle(MenuItem.MenuItemStyle.COMMON);
                MenuItem menuItem2 = new MenuItem();
                menuItem2.setText("注销登录");
                menuItem2.setStyle(MenuItem.MenuItemStyle.STRESS);
                menuItem1.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, menuItem1) {
                    @Override
                    public void onClickMenuItem(View v, MenuItem menuItem) {
                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                menuItem2.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, menuItem1) {
                    @Override
                    public void onClickMenuItem(View v, MenuItem menuItem) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                menuItemList.add(menuItem1);
                menuItemList.add(menuItem2);

                bottomMenuFragment.setMenuItems(menuItemList);

                bottomMenuFragment.show(getFragmentManager(), "BottomMenuFragment");
                break;
            case R.id.stop_test:
                //停止测试
                CustomPrograss.show(this, "运行中，请稍等...", true, null);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(batteryReceiver);
        super.onDestroy();
    }

    /**
     * 广播接受者
     */
    class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            //判断它是否是为电量变化的Broadcast Action
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                //获取当前电量
                int level = intent.getIntExtra("level", 0);
                //电量的总刻度
                int scale = intent.getIntExtra("scale", 100);
                //把它转成百分比
                battery.setText("电量：" + ((level * 100) / scale) + "%");
            }
        }

    }
}
