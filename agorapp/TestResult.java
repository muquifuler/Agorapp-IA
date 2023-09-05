package com.agora.agorapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.agora.agorapp.databinding.TestBinding;
import com.agora.agorapp.databinding.TestResultBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class TestResult extends Fragment {
    private TestResultBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = TestResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PieChart pieChart = binding.pieChart;

        Description description = new Description();
        description.setText("");

        pieChart.setDescription(description);

        ArrayList<PieEntry> entradas = new ArrayList<>();

        float perfil[] = MainActivity.getPerfil();

        entradas.add(new PieEntry(perfil[0], "PSOE"));
        entradas.add(new PieEntry(perfil[1], "PP"));
        entradas.add(new PieEntry(perfil[2], "VOX"));
        entradas.add(new PieEntry(perfil[3], "SUMAR"));

        int[] colors = new int[]{
                ContextCompat.getColor(getContext(), R.color.PSOE),
                ContextCompat.getColor(getContext(), R.color.PP),
                ContextCompat.getColor(getContext(), R.color.VOX),
                ContextCompat.getColor(getContext(), R.color.SUMAR)
        };

        PieDataSet dataSet = new PieDataSet(entradas, "");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(20f);

        Legend legend = pieChart.getLegend();
        legend.setTextSize(20f);
        legend.setFormSize(18f);

        pieChart.setEntryLabelTextSize(20f);

        PercentValueFormatter percentValueFormatter = new PercentValueFormatter();
        dataSet.setValueFormatter(percentValueFormatter);

        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
