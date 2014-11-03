package com.example.cody;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class LineGraph {

    public static Intent execute(Context context) {

        int[] x = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        int[] values = new int[]{1, 11, 24, 15, 36, 12, 45, 8, 9, 16, 5, 6, 1, 2, 3, 4, 5, 16, 17, 14};
        TimeSeries series = new TimeSeries("Line1");
        for (int i = 0; i < x.length; i++) {
            series.add(x[i], values[i]);
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);

        renderer.setColor(Color.GREEN);
        renderer.setPointStyle(PointStyle.SQUARE);
        renderer.setFillPoints(true);

        Intent intent = ChartFactory.getLineChartIntent(context, dataset, mRenderer, "Mel_Chart");
        return intent;

    }
}

