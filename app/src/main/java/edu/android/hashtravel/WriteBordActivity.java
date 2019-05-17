package edu.android.hashtravel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class WriteBordActivity extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Spinner spinnerCategory, spinnerContinent, spinnerCountry;
    private String category, continent, country;
    private EditText textSubject, textDesc, textTag;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_bord);
        getSupportActionBar().hide();

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerContinent = findViewById(R.id.spinnerWrContinent);
        spinnerCountry = findViewById(R.id.spinnerWrCountry);

        textSubject = findViewById(R.id.textSubject);
        textDesc = findViewById(R.id.textDesc);
        textTag = findViewById(R.id.textTag);


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





    } // end onCreate()

    public void onClickInputDashBoard(View view) {
        category = spinnerCategory.getSelectedItem().toString();
        continent = spinnerContinent.getSelectedItem().toString();
        country = spinnerCountry.getSelectedItem().toString();

        // TODO
        String key = mDatabase.child("posts").push().getKey();

        DashBoard dashBoard = new DashBoard(null,category, continent, country, textSubject.getText().toString(), textDesc.getText().toString(), textTag.getText().toString(), 0, null);

        Map<String, Object> postValues = dashBoard.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        // TODO userId값으로 넣기
        childUpdates.put("/posts/" + "Lsdjfklsjfdklsjdf/" +key, postValues);


        mDatabase.updateChildren(childUpdates);

        Toast.makeText(this, "입력 완료", Toast.LENGTH_SHORT).show();

    }
}

