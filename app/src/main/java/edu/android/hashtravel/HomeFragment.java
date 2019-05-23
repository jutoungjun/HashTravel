package edu.android.hashtravel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private Fragment fragment;
    private ImageButton btnAsia, btnEurope, btnAmerica, btnAfrica, btnOceania, btnSouthAmerica;
    private View view;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        btnAsia = view.findViewById(R.id.btnAsia);
        btnEurope = view.findViewById(R.id.btnEurope);
        btnAmerica = view.findViewById(R.id.btnAmerica);
        btnAfrica = view.findViewById(R.id.btnAfrica);
        btnOceania = view.findViewById(R.id.btnOceania);
        btnSouthAmerica = view.findViewById(R.id.btnSouthAmerica);

        // TODO
        btnAsia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new DashboardFragment();
                view = inflater.inflate(R.layout.fragment_dashboard, container, false);
            }
        });

        btnEurope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnAmerica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnAfrica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnOceania.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnSouthAmerica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }

}
