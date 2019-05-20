package edu.android.hashtravel;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {


    private static final String TAG = "fragmentRecycle";
    private Spinner continentSpinner, countrySpinner;
//    private String[] continents = {"Asia", "Europe", "America", "South America", "Africa", "Oceania"};
    private View view;
    private ImageButton buttonWrite;
    private String user;
    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart의 시작?");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume의 시작?");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause의 시작?");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop의 시작?");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView의 시작");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy의 시작");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach의 시작");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView의 생성");
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue("Hello, World!");

        continentSpinner = view.findViewById(R.id.continectSpinner);
        countrySpinner = view.findViewById(R.id.countrySpinner);
        buttonWrite = view.findViewById(R.id.buttonWrite);

        buttonWrite.setEnabled(false);
        if(getArguments() != null){
            user = getArguments().getString("userId");

        }else{
            user = null;
        }

        //TODO
        if(user == null){
            buttonWrite.setEnabled(false);
        }else{
            buttonWrite.setEnabled(true);
        }

        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWriteBoard();

            }
        });

        continentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              setSpinnerCountry(position, countrySpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


       return view;
    }

    private void getWriteBoard() {
        Intent intent = new Intent(view.getContext(), WriteBordActivity.class);
        startActivity(intent);
    }

    public void setSpinnerCountry(int position, Spinner countrySpinner) {
        ArrayAdapter<CharSequence> adpter;
        if(position == 1) {
            adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.Asia_Country, android.R.layout.simple_spinner_item);
        } else if(position == 2) {
            adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.Europe_Country, android.R.layout.simple_spinner_item);
        } else if(position == 3) {
            adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.America_, android.R.layout.simple_spinner_item);
        } else if(position == 4) {
            adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.SouthAmerica_Country, android.R.layout.simple_spinner_item);
        } else if(position == 5) {
            adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.Africa_Country, android.R.layout.simple_spinner_item);
        } else if(position == 6){
            adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.Oceania_, android.R.layout.simple_spinner_item);
        } else {
            adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.initialCountry, android.R.layout.simple_spinner_item);
        }

        adpter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        countrySpinner.setAdapter(adpter);
    }


}
