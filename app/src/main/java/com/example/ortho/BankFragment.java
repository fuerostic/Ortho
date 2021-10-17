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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView totalBankBalance,totalBankLoan,totalDepositThisMonth,totalWithdrawnThisMonth;

    private OnFragmentInteractionListener mListener;

    private double bank_balance,bank_loan,monthly_deposit,monthly_withdraw;

    public BankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BankFragment newInstance(String param1, String param2) {
        BankFragment fragment = new BankFragment();
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

        View view = inflater.inflate(R.layout.fragment_bank, container, false);

        Button addBankButton,showBankListButton;


        totalBankBalance = view.findViewById(R.id.total_bank_balance_amount_textview);

        totalBankLoan = view.findViewById(R.id.total_bank_loan_amount_textview);

        totalDepositThisMonth = view.findViewById(R.id.total_bank_deposit_amount_textview);

        totalWithdrawnThisMonth = view.findViewById(R.id.total_bank_withdrawn_amount_textview);

        showBankListButton = view.findViewById(R.id.bank_list_shower_button);

        addBankButton = view.findViewById(R.id.bank_add_bank_button);


        addBankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),BankAddActivity.class);
                startActivity(intent);
            }
        });

        showBankListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),BankListActivity.class);
                startActivity(intent);

            }
        });

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void setInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
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


        final DailyData c = new DailyData(Calendar.getInstance().getTime());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String uid = user.getUid();




        DatabaseReference summaryReference = FirebaseDatabase.getInstance().getReference(uid).child("calender").child(c.getYear()).child(c.getMonth());


        summaryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                monthly_deposit=0;

                monthly_withdraw=0;
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    DateInfo dateInfo = dataSnapshot1.getValue(DateInfo.class);

                    // Toast.makeText(getContext(),"spent : "+ dateInfo.getExpense() + "deposit : " + dateInfo.getDeposit(),Toast.LENGTH_SHORT).show();

                    monthly_deposit+= dateInfo.getBankDeposit();
                    monthly_withdraw+=dateInfo.getBankWithdrawn();
                }

                totalDepositThisMonth.setText(String.valueOf(monthly_deposit));

                totalWithdrawnThisMonth.setText(String.valueOf(monthly_withdraw));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bank_balance=0;

        bank_loan =0;


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("Bank Accounts");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    BankAccountInfo bankAccountInfo = dataSnapshot1.getValue(BankAccountInfo.class);

                    bank_balance += bankAccountInfo.getBankBalance();

                    bank_loan += bankAccountInfo.getLoan();
                }

                totalBankBalance.setText(String.valueOf(bank_balance));
                totalBankLoan.setText(String.valueOf(bank_loan));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        super.onStart();
    }
}
