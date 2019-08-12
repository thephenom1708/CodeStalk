package com.example.kaivalyamendki.sycodestalk;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TotalAnalysis extends Fragment {


    public TotalAnalysis() {
        // Required empty public constructor
    }

    PieChart pieChart;
    BarChart statusChart, levelChart;
    LineChart lineChart;

    int codeCount=0;
    long dateCount=0;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference assistantDatabase;
    List<String> dateList = new ArrayList<>();
    List<Code> codeList = new ArrayList<>();
    List<Contest> contestList = new ArrayList<>();

    static float implementation=0f, strings=0f, sorting=0f, searching=0f, graphTheory=0f, greedy=0f,
            dynamic=0f, bitManip=0f, recursion=0f, gameTheory=0f, np=0f;

    int easy=0, medium=0, hard=0;
    int submitted=0, partially=0, notSubmitted=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_total_analysis, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //Toast.makeText(getContext(), String.valueOf(implementation), Toast.LENGTH_SHORT).show();

        pieChart = (PieChart)getView().findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(true);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        statusChart = (BarChart)getView().findViewById(R.id.statusChart);
        levelChart = (BarChart)getView().findViewById(R.id.levelChart);
        lineChart = (LineChart)getView().findViewById(R.id.lineChart);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);


        assistantDatabase = FirebaseDatabase.getInstance().getReference("CodeAssistant/" + user.getUid() + "/CodeInfo");

        assistantDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dateCount = 0;
                ArrayList<Entry> chartList = new ArrayList<>();
                for(DataSnapshot walker : dataSnapshot.getChildren()) {
                    dateList.add(walker.getKey());
                    codeCount = 0;
                    for(DataSnapshot codewalker : walker.getChildren())
                    {
                        codeCount++;
                        Code code = codewalker.getValue(Code.class);
                        if (code.getDomain().equals("Implementation")) {
                            implementation++;
                        } else if (code.getDomain().equals("Strings")) {
                            strings++;
                        } else if (code.getDomain().equals("Sorting")) {
                            sorting++;
                        } else if (code.getDomain().equals("Searching")) {
                            searching++;
                        } else if (code.getDomain().equals("Graph Theory")) {
                            graphTheory++;
                        } else if (code.getDomain().equals("Greedy")) {
                            greedy++;
                        } else if (code.getDomain().equals("Dynamic Programming")) {
                            dynamic++;
                        } else if (code.getDomain().equals("Bit Manipulation")) {
                            bitManip++;
                        } else if (code.getDomain().equals("Recursion")) {
                            recursion++;
                        } else if (code.getDomain().equals("Game Theory")) {
                            gameTheory++;
                        } else if (code.getDomain().equals("NP Complete")) {
                            np++;
                        }

                        if (code.getLevel().equals("Easy")) {
                            easy++;
                        } else if (code.getLevel().equals("Medium")) {
                            medium++;
                        } else if (code.getLevel().equals("Hard")) {
                            hard++;
                        }

                        if (code.getStatus().equals("Submitted")) {
                            submitted++;
                        } else if (code.getStatus().equals("Partially Submitted")) {
                            partially++;
                        } else if (code.getStatus().equals("Not Submitted")) {
                            notSubmitted++;
                        }
                        codeList.add(code);
                    }

                    chartList.add(new Entry(dateCount, codeCount));
                    dateCount++;

                    //Toast.makeText(getContext(), String.valueOf(implementation), Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getContext(), String.valueOf(implementation), Toast.LENGTH_SHORT).show();

                YAxis yAxis = lineChart.getAxisLeft();
                yAxis.setAxisMaximum(20f);
                yAxis.setAxisMinimum(0f);

                LineDataSet chartDataSet = new LineDataSet(chartList, "Coding Analysis on Daily Basis");
                chartDataSet.setFillAlpha(110);
                chartDataSet.setColor(Color.RED);
                chartDataSet.setLineWidth(3f);
                chartDataSet.setValueTextSize(10f);
                chartDataSet.setValueTextColor(Color.GREEN);
                /*ArrayList<LineDataSet> chartDataSetList = new ArrayList<>();
                chartDataSetList.add(chartDataSet);*/
                LineData chartData = new LineData(chartDataSet);
                lineChart.setData(chartData);

                ArrayList<PieEntry> values = new ArrayList<>();

                values.add(new PieEntry(implementation, "Implementation"));
                values.add(new PieEntry(strings, "Strings"));
                values.add(new PieEntry(sorting, "Sorting"));
                values.add(new PieEntry(searching, "Searching"));
                values.add(new PieEntry(graphTheory, "Graph Theory"));
                values.add(new PieEntry(greedy, "Greedy"));
                values.add(new PieEntry(dynamic, "Dynamic Programming"));
                values.add(new PieEntry(bitManip, "Bit Manipulation"));
                values.add(new PieEntry(recursion, "Recursion"));
                values.add(new PieEntry(gameTheory, "Game Theory"));
                values.add(new PieEntry(np, "NP Complete"));

                PieDataSet dataSet = new PieDataSet(values, "Domains");
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                PieData pieData = new PieData(dataSet);
                pieData.setValueTextSize(10f);
                pieData.setValueTextColor(Color.YELLOW);

                pieChart.getDescription().setEnabled(false);
                pieChart.setData(pieData);


                ArrayList<BarEntry> statusbarValues = new ArrayList<>();
                float barWidth = 7f;
                float spaceForBar = 10f;

                statusbarValues.add(new BarEntry(0, submitted));
                statusbarValues.add(new BarEntry(spaceForBar, partially));
                statusbarValues.add(new BarEntry(2*spaceForBar, notSubmitted));


                BarDataSet statusDataset = new BarDataSet(statusbarValues, "Status Bar Graph");
                statusDataset.setColors(ColorTemplate.MATERIAL_COLORS);
                statusDataset.setDrawValues(true);

                BarData statusData = new BarData(statusDataset);
                statusData.setBarWidth(barWidth);

                statusData.setValueTextColor(Color.YELLOW);
                statusChart.setFitBars(true);
                statusChart.setData(statusData);
                statusChart.invalidate();
                statusChart.animateY(500);



                ArrayList<BarEntry> levelBarValues = new ArrayList<>();
                barWidth = 9f;
                spaceForBar = 10f;

                levelBarValues.add(new BarEntry(0, easy));
                levelBarValues.add(new BarEntry(spaceForBar, medium));
                levelBarValues.add(new BarEntry(2*spaceForBar, hard));


                BarDataSet levelDataset = new BarDataSet(levelBarValues, "Difficulty Level Bar Graph");
                levelDataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
                levelDataset.setDrawValues(true);

                BarData levelData = new BarData(levelDataset);
                levelData.setBarWidth(barWidth);

                levelData.setValueTextColor(Color.YELLOW);
                levelChart.setFitBars(true);
                levelChart.setData(levelData);
                levelChart.invalidate();
                levelChart.animateY(500);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}
