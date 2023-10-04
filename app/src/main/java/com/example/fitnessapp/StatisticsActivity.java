package com.example.fitnessapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.interfaces.IFitnessApi;
import com.example.fitnessapp.models.ExerciseUser;
import com.example.fitnessapp.models.MonthlyTrainingStatistic;
import com.example.fitnessapp.models.YearlyTrainingStatistic;
import com.example.fitnessapp.vjezbe.IntegerValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatisticsActivity extends AppCompatActivity {

    private static final String TAG = "StatisticsActivity";
    private BarChart barChart;
    private IFitnessApi fitnessApi;

   // private TextView maxWeightExerciseTextView;
    private ToggleButton toggleButton;

    private boolean isMonthly = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        barChart = findViewById(R.id.barChart);
        toggleButton = findViewById(R.id.toggleButton);

       // maxWeightExerciseTextView = findViewById(R.id.maxWeightExerciseTextView);



        fitnessApi = RetrofitService.getClient().create(IFitnessApi.class);


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isMonthly = isChecked;
                if (isMonthly) {
                    fetchMonthlyStatistics();
                } else {
                    fetchYearlyStatistics();
                }
            }
        });

        // Fetch initial statistics data based on the default selection
        if (isMonthly) {
            fetchMonthlyStatistics();
        } else {
            fetchYearlyStatistics();
        }
    }
    private void fetchMonthlyStatistics() {
        Call<List<MonthlyTrainingStatistic>> call = fitnessApi.getTrainingsPerMonth();
        call.enqueue(new Callback<List<MonthlyTrainingStatistic>>() {
            @Override
            public void onResponse(Call<List<MonthlyTrainingStatistic>> call, Response<List<MonthlyTrainingStatistic>> response) {
                if (response.isSuccessful()) {
                    List<MonthlyTrainingStatistic> monthlyStatistics = response.body();
                    displayMonthlyStatistics(monthlyStatistics);
                } else {
                    Log.e(TAG, "Failed to fetch monthly statistics");
                }
            }

            @Override
            public void onFailure(Call<List<MonthlyTrainingStatistic>> call, Throwable t) {
                Log.e(TAG, "Error fetching monthly statistics", t);
            }
        });
    }
    private void fetchYearlyStatistics() {
        Call<List<YearlyTrainingStatistic>> call = fitnessApi.getTrainingsPerYear();
        call.enqueue(new Callback<List<YearlyTrainingStatistic>>() {
            @Override
            public void onResponse(Call<List<YearlyTrainingStatistic>> call, Response<List<YearlyTrainingStatistic>> response) {
                if (response.isSuccessful()) {
                    List<YearlyTrainingStatistic> yearlyStatistics = response.body();
                    displayYearlyStatistics(yearlyStatistics);
                } else {
                    Log.e(TAG, "Failed to fetch yearly statistics");
                }
            }

            @Override
            public void onFailure(Call<List<YearlyTrainingStatistic>> call, Throwable t) {
                Log.e(TAG, "Error fetching yearly statistics", t);
            }
        });
    }



    private void displayMonthlyStatistics(List<MonthlyTrainingStatistic> statistics) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < statistics.size(); i++) {
            MonthlyTrainingStatistic stat = statistics.get(i);
            entries.add(new BarEntry(i, stat.getCount()));
            labels.add(stat.getYear() + "-" + stat.getMonth());
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Broj treninga po mjesecu");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS); // You can set colors as desired

        BarData barData = new BarData(barDataSet);
        barData.setValueTextSize(15f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setLabelCount(labels.size());
        xAxis.setTextSize(15f);

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();

        leftAxis.setTextSize(15f); // Set the text size as desired
        leftAxis.setGranularity(1.0f);
        leftAxis.setGranularityEnabled(true);

        rightAxis.setTextSize(15f); // Set the text size as desired
        rightAxis.setGranularity(1.0f);
        rightAxis.setGranularityEnabled(true);


        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getDescription().setText("Statistika mjesečnih treninga");
        barChart.getDescription().setTextSize(15f);
        barChart.invalidate();
    }

    private void displayYearlyStatistics(List<YearlyTrainingStatistic> statistics) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < statistics.size(); i++) {
            YearlyTrainingStatistic stat = statistics.get(i);
            entries.add(new BarEntry(i, stat.getCount()));
            labels.add(String.valueOf(stat.getYear()));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Broj godišnjih treninga");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS); // You can set colors as desired

        BarData barData = new BarData(barDataSet);
        barData.setValueTextSize(15f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setLabelCount(labels.size());
        xAxis.setTextSize(15f);

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();

        leftAxis.setTextSize(15f); // Set the text size as desired
        leftAxis.setGranularity(1.0f);
        leftAxis.setGranularityEnabled(true);

        rightAxis.setTextSize(15f); // Set the text size as desired
        rightAxis.setGranularity(1.0f);
        rightAxis.setGranularityEnabled(true);


        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getDescription().setText("Statistika godišnjih treninga");
        barChart.getDescription().setTextSize(15f);
        barChart.invalidate();
    }

}