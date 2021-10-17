package com.example.ortho;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.clans.fab.FloatingActionButton;
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

public class WishListFragment extends Fragment {


    private ExpandableListView expandableListView;

    private DatabaseReference databaseReference;

    private FirebaseUser user;

    private String uid;

    private List<WishInfo> wishInfoList;

    private WishListAdapter wishListAdapter;

    private TextView achieved, totalWish,noWish;

    private ProgressBar progressBar;

    private int wishCount, completedCount;

    private GifImageView gifImageView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_wishlist,container,false);

        final FloatingActionButton floatingActionButton = view.findViewById(R.id.floating_add_wish_plan);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),AddWishActivity.class);

                startActivity(intent);
            }
        });

        achieved = view.findViewById(R.id.wish_fragment_wish_achieved_textview);

        progressBar = view.findViewById(R.id.wish_loading_progressBar);

        progressBar.setVisibility(View.VISIBLE);

        noWish = view.findViewById(R.id.no_wish_text);

        totalWish = view.findViewById(R.id.wish_fragment_total_wish);

        expandableListView = view.findViewById(R.id.wish_fragment_expandable_listView);

        wishInfoList = new ArrayList<>();


        gifImageView = view.findViewById(R.id.no_wish_image);



        wishListAdapter = new WishListAdapter(getContext(), (ArrayList<WishInfo>) wishInfoList,getFragmentManager());

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("Wish List");

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition==wishInfoList.size()-1){
                    floatingActionButton.setVisibility(View.INVISIBLE);
                }else {
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                if(groupPosition==wishInfoList.size()-1){
                    floatingActionButton.setVisibility(View.VISIBLE);
                }

            }
        });



        expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if( scrollState==SCROLL_STATE_SETTLING){

                    floatingActionButton.setVisibility(View.VISIBLE);

                }else if(scrollState==SCROLL_STATE_DRAGGING ){
                    floatingActionButton.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

//                int lastItem = firstVisibleItem + visibleItemCount;
//                if (lastItem == totalItemCount) {
//
//                    floatingActionButton.setVisibility(View.INVISIBLE);
//                }else {
//                    floatingActionButton.setVisibility(View.VISIBLE);
//                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                wishInfoList.clear();
                wishCount=0;
                completedCount=0;

                wishInfoList.clear();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    WishInfo wishInfo = dataSnapshot1.getValue(WishInfo.class);
                    wishInfoList.add(wishInfo);
                    wishCount++;
                    if(wishInfo.getWishValue()<=wishInfo.getValueGot()){
                        completedCount++;
                    }
                }

                progressBar.setVisibility(View.GONE);

                if(wishInfoList.size()==0){

                    noWish.setVisibility(View.VISIBLE);

                    gifImageView.setVisibility(View.VISIBLE);




                }else {

                    noWish.setVisibility(View.GONE);

                    gifImageView.setVisibility(View.GONE);
                }

                expandableListView.setAdapter(wishListAdapter);
                totalWish.setText("Total Wish: "+ wishCount);
                achieved.setText("Wish Completed: "+ completedCount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }


}
