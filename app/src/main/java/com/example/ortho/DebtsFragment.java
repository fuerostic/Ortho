package com.example.ortho;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DebtsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DebtsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebtsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView recentDebtsListView;

    private RecentDebtsHistoryAdapter recentDebtsHistoryAdapter;

    private List<DebtsInfo> debtsInfoList;

    private DatabaseReference databaseReference;

    private FirebaseUser user;

    private String uid;

    private OnFragmentInteractionListener mListener;

    private TextView debtsHistoryShow;

    private TextView textView;

    private ProgressBar progressBar;

    private TextView debtsGiven,debtsTaken,debtsPayable,debtsReceivable;

    private double debts_given,debts_taken,debts_payable,debts_receivable;


    public DebtsFragment() {
        // Required empty public constructor
    }
    public void setInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DebtsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DebtsFragment newInstance(String param1, String param2) {
        DebtsFragment fragment = new DebtsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_debts, container, false);

        recentDebtsListView = view.findViewById(R.id.debts_history_recent);

        debtsInfoList = new ArrayList<DebtsInfo>();

        recentDebtsHistoryAdapter = new RecentDebtsHistoryAdapter(getActivity(),debtsInfoList);

        user = FirebaseAuth.getInstance().getCurrentUser();

        debtsGiven = view.findViewById(R.id.total_debts_given_amount_textview);

        debtsTaken = view.findViewById(R.id.total_debts_taken_amount_textview);

        debtsPayable = view.findViewById(R.id.total_debts_payable_amount_textview);

        debtsReceivable = view.findViewById(R.id.total_debts_receivable_amount_textview);



        uid = user.getUid();

        textView = view.findViewById(R.id.no_recent_debt_text);

        progressBar = view.findViewById(R.id.recent_debt_loading_progressbar);

        progressBar.setVisibility(View.VISIBLE);


        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("Debts");

        debtsHistoryShow = view.findViewById(R.id.all_transaction_viewer_button);

        debtsHistoryShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(getActivity(),DebtLlistActivity.class);

                startActivity(intent);

            }
        });









        return view;
    }

    @Override
    public void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                debtsInfoList.clear();

                debts_given=0;

                debts_taken=0;

                debts_payable=0;

                debts_receivable=0;

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    DebtsInfo debtsInfo1 = dataSnapshot1.getValue(DebtsInfo.class);
                    debtsInfoList.add(debtsInfo1);

                    if(debtsInfo1.getDebtType().equals("Given")){

                        debts_given += debtsInfo1.getAmount();

                        debts_receivable += (debtsInfo1.getAmount()-debtsInfo1.getDebtGot());
                    }else if(debtsInfo1.getDebtType().equals("Taken")){

                        debts_taken += debtsInfo1.getAmount();

                        debts_payable += (debtsInfo1.getAmount()-debtsInfo1.getDebtGot());
                    }
                }

                progressBar.setVisibility(View.GONE);

                if(debtsInfoList.size()==0){

                    textView.setVisibility(View.VISIBLE);
                }else {

                    textView.setVisibility(View.GONE);
                }

                debtsGiven.setText(String.valueOf(debts_given));

                debtsTaken.setText(String.valueOf(debts_taken));

                debtsPayable.setText(String.valueOf(debts_payable));

                debtsReceivable.setText(String.valueOf(debts_receivable));

                recentDebtsListView.setAdapter(recentDebtsHistoryAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
