package com.example.tictacjournalofficial.fragments;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.calendar.CalendarAdapter;
import com.example.tictacjournalofficial.database.JournalsDatabase;
import com.example.tictacjournalofficial.databinding.FragmentInsightBinding;
import com.example.tictacjournalofficial.entities.ColorCount;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsightFragment extends Fragment implements CalendarAdapter.OnItemListener {

    private PieChart chart;
    private JournalsDatabase db;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private Typeface font;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentInsightBinding binding = FragmentInsightBinding.inflate(inflater, container, false);

        calendarRecyclerView = binding.calendarRecyclerView;
        monthYearText = binding.monthYearTV; // update this to your TextView ID in fragment_insight.xml
        selectedDate = LocalDate.now();
        setMonthView();

        binding.previousMonthButton.setOnClickListener(view -> previousMonthAction());

        binding.nextMonthButton.setOnClickListener(view -> nextMonthAction());

        font = ResourcesCompat.getFont(getContext(), R.font.ubuntu_regular);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Get an instance of Firestore
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

        // Assuming "my_journals" is the collection group ID
                firestoreDB.collectionGroup("my_journals")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                HashMap<String, Integer> colorCountMap = new HashMap<>();

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String color = document.getString("color");
                                    if (color != null) {
                                        Integer count = colorCountMap.get(color);
                                        if (count == null) {
                                            count = 0;
                                        }
                                        colorCountMap.put(color, count + 1);
                                    }
                                }

                                List<ColorCount> colorCounts = new ArrayList<>();
                                for (Map.Entry<String, Integer> entry : colorCountMap.entrySet()) {
                                    colorCounts.add(new ColorCount(entry.getKey(), entry.getValue()));
                                }
                                loadMoodData(colorCounts);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        });



        //customize the pie chart
        chart = view.findViewById(R.id.moodChart);
        Legend legend = chart.getLegend();
        legend.setTypeface(ResourcesCompat.getFont(getContext(), R.font.ubuntu_regular)); // Set the font here
        legend.setTextSize(15f);
        legend.setTextColor(Color.WHITE);

        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
       // legend.setDrawInside(false);
        legend.setXOffset(0f); // Adjust this value as needed
        legend.setYOffset(30f);

        db = JournalsDatabase.getDatabase(getContext());
        db.journalDao().getColorCounts().observe(getViewLifecycleOwner(), colorCounts -> loadMoodData(colorCounts));


    }

    private void loadMoodData(List<ColorCount> colorCounts) {
        HashMap<String, Integer> colorCountMap = new HashMap<>();
        for (ColorCount colorCount : colorCounts) {
            colorCountMap.put(colorCount.getColor(), colorCount.getCount());
        }

        // Pass the mood data to the setupChart() method
        setupChart(colorCountMap);
        Log.d(TAG, "Color Count Map: " + colorCountMap.toString());
        //debug
        Log.d(TAG, "Color Counts: " + colorCounts.toString());  // Log colorCounts directly



    }


    private void setupChart(HashMap<String, Integer> colorCount) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        // Create PieEntry for each mood
        for (HashMap.Entry<String, Integer> entry : colorCount.entrySet()) {
            String colorCode = entry.getKey();
            Integer count = entry.getValue();

            String label = getLabelFromColorCode(colorCode);
            //debug
            Log.d(TAG, "Color Code: " + colorCode + ", Label: " + label);

            entries.add(new PieEntry(count, label));
            colors.add(Color.parseColor(colorCode));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Moods");
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTypeface(font); // Set font type here

        chart.setData(pieData);
        chart.getDescription().setText("Moods Pie Chart");
        chart.setCenterText("Moods");
        chart.setExtraOffsets(-80f, 0f, 0f, 0f);

        //chart.setDrawHoleEnabled(true); // This line is not necessary if the hole is already enabled
        chart.setHoleRadius(20f); // Set the hole radius to 20% of the chart's radius
        chart.invalidate();
       // chart.animate();



        Log.d(TAG, "Entries: " + entries.toString());
        Log.d(TAG, "Colors: " + colors.toString());

    }

    private String getLabelFromColorCode(String colorCode) {
        switch (colorCode.toLowerCase()) {
            case "#fdbe3b":
                return "Excited";
            case "#ff4842":
                return "Angry";
            case "#3a52fc":
                return "Sad";
            case "#000000":
                return "Depressed";
            case "#333333":
                return "Fine";
            default:
                return "Unknown";
        }
    }

    //calendar code

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction()
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction()
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
