package com.example.miniplus_dev;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class CustomMarker extends MarkerView {
    private TextView tvContent;

    public CustomMarker (Context context) {
        super(context, R.layout.layout_custommarker);
        tvContent = (TextView) findViewById(R.id.aaa);
    }
    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        canvas.translate(270 , posY - 10);
        Log.e("위치", posX + "   " + posY);
        super.draw(canvas);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        BarEntry barEntry = (BarEntry) e;
        float[] values = barEntry.getYVals();
        tvContent.setText((int)values[0] + " / " + (int)values[1]);
        super.refreshContent(e, highlight);
    }
}
