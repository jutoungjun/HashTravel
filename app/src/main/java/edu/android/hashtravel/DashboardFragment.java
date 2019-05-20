package edu.android.hashtravel;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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



    private Spinner continentSpinner, countrySpinner;
//    private String[] continents = {"Asia", "Europe", "America", "South America", "Africa", "Oceania"};
    private View view;
    private ImageButton buttonWrite;
    public DashboardFragment() {
        // Required empty public constructor
    }
    FirebaseUser user;

    @Override
    public void onStart() {
        super.onStart();

        //TODO
        Bundle bundle = getArguments();
        if(bundle != null){
            user = bundle.getParcelable("userId");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue("Hello, World!");

        continentSpinner = view.findViewById(R.id.continectSpinner);
        countrySpinner = view.findViewById(R.id.countrySpinner);
        buttonWrite = view.findViewById(R.id.buttonWrite);

        //TODO
        if(user != null){
            buttonWrite.setEnabled(true);

        }else{
            buttonWrite.setEnabled(false);


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
