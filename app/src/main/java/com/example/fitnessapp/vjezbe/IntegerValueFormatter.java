package com.example.fitnessapp.vjezbe;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class IntegerValueFormatter implements IAxisValueFormatter  {

    private final List<String> labels = new ArrayList<>();

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // Format the value as an integer
        return String.valueOf((int) value);
    }

}
