package com.zz.temperaturemonitor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zz.temperaturemonitor.R;
import com.zz.temperaturemonitor.base.BaseActivity;
import com.zz.temperaturemonitor.calendar.bean.DateEntity;
import com.zz.temperaturemonitor.calendar.view.DataView;
import com.zz.temperaturemonitor.utils.CrossAxisScale;
import com.zz.temperaturemonitor.utils.FoldLinePoint;
import com.zz.temperaturemonitor.utils.VerticalAxisScale;
import com.zz.temperaturemonitor.view.FoldLineView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @类名: ${type_name}
 * @功能描述:
 * @作者: ${user}
 * @时间: ${date}
 * @最后修改者:
 * @最后修改内容:
 */
public class HistoryActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.ic_title)
    TextView icTitle;
    @BindView(R.id.anim_layout)
    LinearLayout animLayout;
    @BindView(R.id.week)
    DataView dataView;
    @BindView(R.id.fl_content)
    FoldLineView flContent;
    @BindView(R.id.temp_avar)
    TextView tempAvar;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.temp_max)
    TextView tempMax;
    @BindView(R.id.temp_min)
    TextView tempMin;

    private String month;
    private String day;

    private List<CrossAxisScale> crosssAxis = new ArrayList<CrossAxisScale>();
    private List<VerticalAxisScale> verticalAxis = new ArrayList<VerticalAxisScale>();
    private List<FoldLinePoint> foldLinePoints = new ArrayList<FoldLinePoint>();// 折线点

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected void initInjector() {
        icTitle.setText("监控记录");
    }

    @Override
    protected void initEventAndData() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        dataView.setOnSelectListener(new DataView.OnSelectListener() {
            @Override
            public void onSelected(DateEntity date) {
                String[] dataStr = date.date.split("-");
                month = dataStr[1];
                day = dataStr[2];
                
                //选中日期后作下一步处理

            }
        });

        dataView.getData(today);

        initTemp();
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


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }
}
