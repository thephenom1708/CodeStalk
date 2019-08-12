package com.example.kaivalyamendki.sycodestalk;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContestInfo extends Fragment {


    public ContestInfo() {
        // Required empty public constructor
    }

    String name, rank, totalQuestion, totalSub, contestDomain;

    EditText contestName, contestRank;
    NumberPicker totalQue, totalSubmissions;
    Spinner domain;
    Button contestInfoSubmit;
    ArrayAdapter<CharSequence> adapterDomain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contest_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contestName = (EditText)getView().findViewById(R.id.contestName);
        contestRank = (EditText)getView().findViewById(R.id.contestRank);
        totalQue = (NumberPicker)getView().findViewById(R.id.numQuestions);
        totalSubmissions = (NumberPicker)getView().findViewById(R.id.numSubmissions);
        domain = (Spinner)getView().findViewById(R.id.spinnerContestDomain);
        contestInfoSubmit = (Button)getView().findViewById(R.id.contestInfoSubmit);

        totalQue.setMinValue(1);
        totalQue.setMaxValue(50);
        totalQue.setWrapSelectorWheel(true);

        totalSubmissions.setMinValue(0);
        totalSubmissions.setMaxValue(50);
        totalSubmissions.setWrapSelectorWheel(true);

        totalQue.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                totalQuestion = String.valueOf(newVal);
            }
        });

        totalSubmissions.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                totalSub = String.valueOf(newVal);
            }
        });


        adapterDomain = ArrayAdapter.createFromResource(getActivity(), R.array.Competitions, android.R.layout.simple_spinner_item);
        adapterDomain.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        domain.setAdapter(adapterDomain);

        contestInfoSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 name = contestName.getText().toString();
                 rank = contestRank.getText().toString();
                 contestDomain = domain.getSelectedItem().toString();

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                String contestDate = format.format(Calendar.getInstance().getTime());

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference assistantDatabase = FirebaseDatabase.getInstance().getReference("CodeAssistant");

                Contest contest = new Contest(name, contestDomain, totalQuestion, totalSub, rank);

                assistantDatabase.child(user.getUid()).child("ContestInfo").child(contestDate).push().setValue(contest);

                Toast.makeText(getContext(), "Contest Info Submitted Successfully...",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
