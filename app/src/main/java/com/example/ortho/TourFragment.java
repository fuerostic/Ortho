package com.example.ortho;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING;

public class TourFragment extends Fragment {

    private FloatingActionButton addTourButton;

    private ProgressBar progressBar;

    private FloatingActionMenu floatingActionMenu;

    private ListView tourListView;

    private FirebaseUser user;

    private String uid;

    private DatabaseReference databaseReference;

    private List<TourInfo> tourInfoList;

    private TourListAdapter tourListAdapter;

    private TextView textView;

    private GifImageView gifImageView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_tour,container,false);

        addTourButton = view.findViewById(R.id.add_tour_button);

        tourInfoList = new ArrayList<TourInfo>();

        user = FirebaseAuth.getInstance().getCurrentUser();

        textView = view.findViewById(R.id.no_tour_text);

        progressBar = view.findViewById(R.id.tour_loading_progressbar);

        progressBar.setVisibility(View.VISIBLE);

        floatingActionMenu = view.findViewById(R.id.tour_floating_action_menu);

        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("Tour Plan");

        tourListAdapter = new TourListAdapter(getActivity(),tourInfoList,getFragmentManager());

        tourListView = view.findViewById(R.id.tour_list_id);
        gifImageView  = view.findViewById(R.id.no_tour_image);



        addTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(),AddTourActivity.class);

                startActivity(intent);
            }
        });


        tourListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                 if(scrollState==SCROLL_STATE_DRAGGING ){
                    floatingActionMenu.setVisibility(View.INVISIBLE);
                }else {

                     floatingActionMenu.setVisibility(View.VISIBLE);
                 }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });




        return view;


    }

    @Override
    public void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                tourInfoList.clear();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    TourInfo tourInfo = dataSnapshot1.getValue(TourInfo.class);
                    tourInfoList.add(tourInfo);
                }

                if(tourInfoList.size()==0){

                    textView.setVisibility(View.VISIBLE);
                    gifImageView.setVisibility(View.VISIBLE);

                }else{

                    textView.setVisibility(View.GONE);
                    gifImageView.setVisibility(View.GONE);
                }

                progressBar.setVisibility(View.GONE);

                tourListView.setAdapter(tourListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }
}
