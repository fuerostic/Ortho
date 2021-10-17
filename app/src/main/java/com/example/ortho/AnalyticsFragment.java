package com.example.ortho;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
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
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class AnalyticsFragment extends Fragment {


    private LineChart monthlyBalanceLineChart;

    private LineDataSet monthlyBalanceDataset = new LineDataSet(null,null);

    private ArrayList<ILineDataSet> monthlyBalanceILineDataset = new ArrayList<>();

    private LineData monthlyBalanceLineData;

    private ArrayList<Entry> monthlyBalancePoints = new ArrayList<>();

    private BarChart expenseBar,savingsBar;
    
    private ArrayList<BarEntry> expenseEntries = new ArrayList<>();

    private ArrayList<BarEntry>savingsEntries = new ArrayList<>();

    private ArrayList<PieEntry> expensePieEntries = new ArrayList<>();

    private PieChart expensePieChart;

    private  float balance;

    private int num;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_analytics,container,false);

        expensePieChart = view.findViewById(R.id.expense_pie_chart_id);


        expensePieChart.setUsePercentValues(true);

        expensePieChart.getDescription().setEnabled(false);

        expensePieChart.setExtraOffsets(5,10,5,5);

        expensePieChart.setDragDecelerationEnabled(false);

        expensePieChart.setDragDecelerationFrictionCoef(.95f);

        expensePieChart.setDrawHoleEnabled(false);




        monthlyBalanceLineChart = view.findViewById(R.id.montly_linechart_id);

        expenseBar= view.findViewById(R.id.expense_bar_chart_id);

        savingsBar = view.findViewById(R.id.savings_bar_chart_id);

        


        return view;

    }

    @Override
    public void onStart() {

        final DailyData dailyData = new DailyData(Calendar.getInstance().getTime());

//        if(Integer.parseInt(dailyData.getDay())/10==0){
//
//            String d = "0" + dailyData.getDay();
//
//            dailyData.setDay(d);
//        }
//
//        if(Integer.parseInt(dailyData.getMonth())/10==0){
//
//            String m = "0" + dailyData.getMonth();
//
//            dailyData.setMonth(m);
//        }


        String uid = FirebaseAuth.getInstance().getUid();

        Log.d("hi", "date: " + dailyData.getDay()+ " " + dailyData.getMonth() +" "+ dailyData.getYear() + " " + uid);


        final DatabaseReference pieReference = FirebaseDatabase.getInstance().getReference(uid).child("expenseInfo").child(dailyData.getYear());

        expensePieEntries.clear();

        pieReference.child(dailyData.getMonth()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                expensePieEntries.clear();

                if(dataSnapshot.hasChildren()){

                    ExpenseInfo expenseInfo = dataSnapshot.getValue(ExpenseInfo.class);
                    Log.d("Hi", "onDataChange: "+ expenseInfo.getFood());


                    if(expenseInfo.getFood()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getFood(),"Food"));
                    }

                    if(expenseInfo.getRent()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getRent(),"Rent"));
                    }

                    if(expenseInfo.getTransportation()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getTransportation(),"Transportation"));
                    }

                    if(expenseInfo.getClothing()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getClothing(),"Clothing"));
                    }

                    if(expenseInfo.getCommunication()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getCommunication(),"Communication"));
                    }

                    if(expenseInfo.getBooks()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getBooks(),"Books"));
                    }

                    if(expenseInfo.getElectronics()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getElectronics(),"Electronics"));
                    }

                    if(expenseInfo.getProject()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getProject(),"Project"));
                    }

                    if(expenseInfo.getTreat()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getTreat(),"Treat"));
                    }

                    if(expenseInfo.getTuition()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getTuition(),"Tuition"));
                    }

                    if(expenseInfo.getEducation()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getEducation(),"Education"));
                    }

                    if(expenseInfo.getHangout()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getHangout(),"Hangout"));
                    }

                    if(expenseInfo.getTrip()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getTrip(),"Trip"));
                    }

                    if(expenseInfo.getUtilities()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getUtilities(),"Utilities"));
                    }

                    if(expenseInfo.getServices()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getServices(),"Services"));
                    }

                    if(expenseInfo.getFees()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getFees(),"Fees"));
                    }

                    if(expenseInfo.getTax()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getTax(),"Tax"));
                    }

                    if(expenseInfo.getOthers()>0){
                        expensePieEntries.add(new PieEntry((float) expenseInfo.getOthers(),"Others"));
                    }


                }

                Log.d("hi", "onDataChange: "+expensePieEntries.size());

                showExpensePieChart(expensePieEntries);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("calender").child(dailyData.getYear()).child(dailyData.getMonth());




        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Entry> monthlyBalancePoints = new ArrayList<>();

                double balance=0;

                int num=0;

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    DateInfo dateInfo = dataSnapshot1.getValue(DateInfo.class);

                    String parent = dataSnapshot1.getKey();

                    if(Integer.valueOf(parent)==num+1){

                        balance += ((dateInfo.getDeposit())-(dateInfo.getExpense()));

                        monthlyBalancePoints.add(new Entry((float)Integer.parseInt(parent), (float) balance));

                        expenseEntries.add(new BarEntry(Integer.parseInt(parent),(float)dateInfo.getExpense()));

                        savingsEntries.add(new BarEntry(Integer.parseInt(parent),(float)dateInfo.getDeposit()));

                        num = Integer.valueOf(parent);
                    }else{

                        for(int i=num+1;i<Integer.valueOf(parent);i++){

                            monthlyBalancePoints.add(new Entry((float)i, (float) balance));
                            expenseEntries.add(new BarEntry((float)i,0f));

                            savingsEntries.add(new BarEntry((float)i,0f));
                        }

                        balance += ((dateInfo.getDeposit())-(dateInfo.getExpense()));

                        monthlyBalancePoints.add(new Entry((float)Integer.parseInt(parent), (float) balance));

                        expenseEntries.add(new BarEntry(Integer.parseInt(parent),(float)dateInfo.getExpense()));

                        savingsEntries.add(new BarEntry(Integer.parseInt(parent),(float)dateInfo.getDeposit()));

                        num = Integer.valueOf(parent);

                    }


                    
                    

                }

                int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                if(num<d){


                        for(int i=num+1;i<=d;i++){

                            monthlyBalancePoints.add(new Entry((float)i, (float) balance));
                            expenseEntries.add(new BarEntry((float)i,0f));

                            savingsEntries.add(new BarEntry((float)i,0f));

                            num++;
                        }

                }

                if(d/10==0){

                    for(int i=num+1;i<=d+10;i++){

                        monthlyBalancePoints.add(new Entry((float)i, (float) balance));
                        expenseEntries.add(new BarEntry((float)i,0f));

                        savingsEntries.add(new BarEntry((float)i,0f));
                    }

                }




                showChart(monthlyBalancePoints);
                showExpenseBar(expenseEntries);
                showSavingsBar(savingsEntries);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onStart();
    }

    private void showExpensePieChart(ArrayList<PieEntry> expensePieEntries) {

        PieDataSet expensePieDataSet = new PieDataSet(expensePieEntries,"Expenses");

        expensePieDataSet.setSliceSpace(3f);

        expensePieDataSet.setSelectionShift(5f);

        expensePieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData expensePieData = new PieData(expensePieDataSet);

        expensePieData.setValueFormatter(new PercentFormatter());

        expensePieData.setValueTextSize(10f);


        expensePieChart.setData(expensePieData);

        expensePieChart.setEntryLabelColor(Color.BLACK);
        expensePieChart.setEntryLabelTextSize(7f);
        //expensePieChart.setEntryLabelTypeface(tf);

        expensePieData.setValueTextColor(Color.BLACK);
        expensePieDataSet.setValueLinePart1OffsetPercentage(90.f);
        expensePieDataSet.setValueLinePart1Length(1f);
        expensePieDataSet.setValueLinePart2Length(.2f);
        expensePieDataSet.setValueTextColor(Color.BLACK);
        expensePieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);



        expensePieChart.invalidate();

    }

    private void showSavingsBar(ArrayList<BarEntry> savingsEntries) {



        BarDataSet savingsDataset = new BarDataSet(savingsEntries,"Monthly Deposits");

        savingsDataset.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData savingsData = new BarData(savingsDataset);

        savingsBar.setData(savingsData);

        savingsBar.invalidate();

        savingsBar.setDrawBarShadow(false);
        savingsBar.setDrawValueAboveBar(false);
        savingsBar.setPinchZoom(false);


        savingsBar.getDescription().setEnabled(false);

        savingsBar.setDrawGridBackground(false);

        savingsBar.getAxisRight().setDrawLabels(false);


        savingsBar.getAxisLeft().setDrawGridLines(false);

        savingsBar.getXAxis().setDrawGridLines(false);




    }

    private void showExpenseBar(ArrayList<BarEntry> expenseEntries) {

        BarDataSet expenseDataset = new BarDataSet(expenseEntries,"Monthly Expense");

        expenseDataset.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData expenseData = new BarData(expenseDataset);

        expenseBar.setData(expenseData);

        expenseBar.invalidate();

        expenseBar.setDrawBarShadow(false);
        expenseBar.setDrawValueAboveBar(false);
        expenseBar.setPinchZoom(false);

        expenseBar.getDescription().setEnabled(false);

        expenseBar.setDrawGridBackground(false);

        expenseBar.getAxisRight().setDrawLabels(false);


        expenseBar.getAxisLeft().setDrawGridLines(false);

        expenseBar.getXAxis().setDrawGridLines(false);

    }

    private void showChart(ArrayList<Entry> monthlyBalancePoints) {

        monthlyBalanceDataset.setValues(monthlyBalancePoints);
        monthlyBalanceDataset.setLabel("Monthly Balance");
        monthlyBalanceDataset.setDrawValues(false);
        monthlyBalanceDataset.setLineWidth((float) 2.0);

        YAxis rightAxis = monthlyBalanceLineChart.getAxisRight();

        monthlyBalanceLineChart.getDescription().setEnabled(false);

        monthlyBalanceLineChart.setDrawGridBackground(false);

        monthlyBalanceLineChart.getAxisRight().setDrawLabels(false);


        monthlyBalanceLineChart.getAxisLeft().setDrawGridLines(false);

        monthlyBalanceLineChart.getXAxis().setDrawGridLines(false);

        monthlyBalanceILineDataset.clear();

        monthlyBalanceILineDataset.add(monthlyBalanceDataset);

        monthlyBalanceLineData = new LineData(monthlyBalanceILineDataset);

        monthlyBalanceLineChart.clear();

        monthlyBalanceLineChart.setData(monthlyBalanceLineData);

        monthlyBalanceLineChart.invalidate();


    }
}
