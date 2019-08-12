package com.example.kaivalyamendki.sycodestalk;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingContests extends Fragment {

    ProgressDialog progressDialog;
    public UpcomingContests() {
        // Required empty public constructor
    }

    WebView upcomingContests;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_upcoming_contests, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        upcomingContests = (WebView)getView().findViewById(R.id.upcomingContests);
        String url="https://clist.by/";
        upcomingContests.getSettings().setJavaScriptEnabled(true);
        upcomingContests.loadUrl(url);

        if(upcomingContests.isInLayout()){
            progressDialog.dismiss();
        }
    }
}
