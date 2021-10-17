package com.example.ortho;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExpenseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView recentTransactionList;

    private OnFragmentInteractionListener mListener;

    private DatabaseReference databaseReference;

    private FirebaseUser user;

    private String uid;

    private List<TransactionData> transactionDataList;

    private CustomAdapter customAdapter;

    private TextView transactionListShow;

    private ProgressBar progressBar,expenseProgress;

    private TextView textView,depositedThisMonth,spentThisMonth,depositedToday,spentToday,balanceText;


    private double spent_month,spent_today,deposit_month,deposit_today;







    public ExpenseFragment() {
        // Required empty public constructor
    }

    public void setInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }




    // TODO: Rename and change types and number of parameters
    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);








    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        depositedThisMonth = view.findViewById(R.id.deposited_this_month_amount_textview);

        depositedToday = view.findViewById(R.id.deposited_today_amount_textview);

        spentThisMonth = view.findViewById(R.id.spent_this_month_amount_textview);

        spentToday = view.findViewById(R.id.spent_today_amount_textview);

        balanceText = view.findViewById(R.id.expense_progress_text);

        expenseProgress = view.findViewById(R.id.expense_progressbar);


        recentTransactionList = view.findViewById(R.id.transactionList);

        transactionDataList = new ArrayList<TransactionData>();

        customAdapter = new CustomAdapter(getActivity(), transactionDataList);

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();



        progressBar = view.findViewById(R.id.recent_transaction_loading_proressBar);

        progressBar.setVisibility(View.VISIBLE);

        textView = view.findViewById(R.id.no_recent_transaction_text);

        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("transactions");

        transactionListShow = view.findViewById(R.id.all_transaction_viewer_button);

        transactionListShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),TransactionListActivity.class);
                startActivity(intent);

            }
        });





        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void fillData(){





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



    @Override
    public void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                transactionDataList.clear();

                int count=0;

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    TransactionData transactionData = dataSnapshot1.getValue(TransactionData.class);
                    transactionDataList.add(transactionData);
                    count++;

                }



                if (transactionDataList.size()==0){

                    textView.setVisibility(View.VISIBLE);
                }else{

                    textView.setVisibility(View.GONE);
                }

                Collections.sort(transactionDataList);

                recentTransactionList.setAdapter(customAdapter);

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final DailyData c = new DailyData(Calendar.getInstance().getTime());


        DatabaseReference summaryReference = FirebaseDatabase.getInstance().getReference(uid).child("calender").child(c.getYear()).child(c.getMonth());


        summaryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spent_month=0;
                deposit_month=0 ;
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    DateInfo dateInfo = dataSnapshot1.getValue(DateInfo.class);

                   // Toast.makeText(getContext(),"spent : "+ dateInfo.getExpense() + "deposit : " + dateInfo.getDeposit(),Toast.LENGTH_SHORT).show();

                    spent_month+= dateInfo.getExpense();
                    deposit_month+=dateInfo.getDeposit();
                }

                depositedThisMonth.setText(String.valueOf(deposit_month));

                spentThisMonth.setText(String.valueOf(spent_month));


                expenseProgress.setProgress((int)(((deposit_month-spent_month)/deposit_month)*100));
                balanceText.setText("Balance:\n"+ String.valueOf(deposit_month-spent_month));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        summaryReference.child(c.getDay()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("deposit")){

                    DateInfo dateInfo = dataSnapshot.getValue(DateInfo.class);

                    deposit_today = dateInfo.getDeposit();
                    spent_today=dateInfo.getExpense();


                    depositedToday.setText(String.valueOf(deposit_today));

                    spentToday.setText(String.valueOf(spent_today));
                }
                else {

                    depositedToday.setText(String.valueOf(0.0));

                    spentToday.setText(String.valueOf(0.0));

                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        super.onStart();




    }
}


