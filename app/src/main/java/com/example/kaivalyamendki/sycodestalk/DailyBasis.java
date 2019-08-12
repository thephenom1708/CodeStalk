package com.example.kaivalyamendki.sycodestalk;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DailyBasis extends Fragment {


    public DailyBasis() {
        // Required empty public constructor
    }

    int year_x,month_x,date_x;
    int flag=0;
    static final int DIALOG_ID=0;

    private RecyclerView codeRecyclerView;
    private DatabaseReference assistantDatabase;
    private List<Code> codeList;
    private CodeAdapter codeAdapter;

    String date;

    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_basis, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn = (Button)getView().findViewById(R.id.btnDatePicker);
        codeRecyclerView = (RecyclerView)getView().findViewById(R.id.dailyBasis_RecyclerView);

        codeRecyclerView.setHasFixedSize(true);
        codeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        codeList = new ArrayList<>();
        codeAdapter = new CodeAdapter(codeList);

        codeRecyclerView.setAdapter(codeAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cal=Calendar.getInstance();

                DatePickerDialog dpd =  new DatePickerDialog(getActivity(),dpickerListener,year_x,month_x,date_x);
                dpd.show();

            }
        });

    }

    public void firebaseCodeSearch()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assistantDatabase = FirebaseDatabase.getInstance().getReference("CodeAssistant/" + user.getUid() + "/CodeInfo");

        assistantDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                flag = 0;
                for(DataSnapshot walker : dataSnapshot.getChildren()){
                    if(walker.getKey().equals(date))
                    {
                        flag = 1;
                        for(DataSnapshot codeWalker : walker.getChildren())
                        {
                            Code code = codeWalker.getValue(Code.class);
                            codeList.add(code);
                        }
                        codeAdapter.notifyDataSetChanged();
                    }
                }
                if(flag == 0){
                    Toast.makeText(getContext(), "No any content for this date...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dpickerListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x=year;
            month_x=month+1;
            date_x=dayOfMonth;

            String x,y;
            if(date_x < 10)
                x = "0" + String.valueOf(date_x);
            else
                x = String.valueOf(date_x);

            if(month_x < 10)
                y = "0" + String.valueOf(month_x);
            else
                y = String.valueOf(month_x);

                date = x + "-" + y + "-" + year_x;

            codeList.clear();
            codeAdapter.notifyDataSetChanged();
            firebaseCodeSearch();

            // TextView text=(TextView)findViewById(R.id.date);
            // text.setText(date_x);
            //Toast.makeText(getActivity(),+year_x+" / "+month_x+" / "+date_x+" ",Toast.LENGTH_SHORT).show();

        }
    };
}
