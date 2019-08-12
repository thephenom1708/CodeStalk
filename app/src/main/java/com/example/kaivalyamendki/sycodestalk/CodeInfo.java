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
public class CodeInfo extends Fragment {


    public CodeInfo() {
        // Required empty public constructor
    }

    EditText codeName, description;
    Spinner domain, status, level;
    Button codeInfoSubmit;
    ArrayAdapter<CharSequence> adapterDomain,adapterStatus,adapterLevel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        codeName = (EditText)getView().findViewById(R.id.codeName);
        description = (EditText)getView().findViewById(R.id.codeDescription);
        domain = (Spinner)getView().findViewById(R.id.spinnerDomain);
        status = (Spinner)getView().findViewById(R.id.spinnerStatus);
        level = (Spinner)getView().findViewById(R.id.spinnerLevel);
        codeInfoSubmit = (Button)getView().findViewById(R.id.codeInfoSubmit);

        adapterDomain = ArrayAdapter.createFromResource(getActivity(), R.array.Domain, android.R.layout.simple_spinner_item);
        adapterDomain.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        domain.setAdapter(adapterDomain);

        adapterLevel = ArrayAdapter.createFromResource(getActivity(),R.array.Level,android.R.layout.simple_spinner_item);
        adapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level.setAdapter(adapterLevel);

        adapterStatus = ArrayAdapter.createFromResource(getActivity(),R.array.Status,android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(adapterStatus);

        codeInfoSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeDomain = domain.getSelectedItem().toString();
                String codeStatus = status.getSelectedItem().toString();
                String codeLevel = level.getSelectedItem().toString();
                String name = codeName.getText().toString();
                String desc = description.getText().toString();

                Code code = new Code(name, codeDomain, codeLevel, codeStatus, desc);

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                String codeDate = format.format(Calendar.getInstance().getTime());

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference assistantDatabase = FirebaseDatabase.getInstance().getReference("CodeAssistant");
                assistantDatabase.child(user.getUid()).child("CodeInfo").child(codeDate).push().setValue(code);

                Toast.makeText(getContext(), "Code Info Submitted Successfully...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code_info, container, false);
    }

}
