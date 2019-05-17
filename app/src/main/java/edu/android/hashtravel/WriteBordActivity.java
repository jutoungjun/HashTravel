package edu.android.hashtravel;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class WriteBordActivity extends AppCompatActivity {

    private Spinner spinnerContinent, spinnerCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_bord);
        getSupportActionBar().hide();

        spinnerContinent = findViewById(R.id.spinnerWrContinent);
        spinnerCountry = findViewById(R.id.spinnerWrCountry);

        spinnerContinent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                    adpter = ArrayAdapter.createFromResource(view.getContext(), R.array.empty, android.R.layout.simple_spinner_item);
                }

                adpter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinnerCountry.setAdapter(adpter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
