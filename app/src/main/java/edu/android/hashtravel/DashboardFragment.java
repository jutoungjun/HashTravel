package edu.android.hashtravel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private static DashboardFragment instance = null;

    public DashboardFragment() {
        // Required empty public constructor ?
    }

    public static DashboardFragment getInstance() {
        if(instance == null) {
            instance = new DashboardFragment();
        }
        return instance;
    }

    private static final String TAG = "fragmentRecycle";
    private Spinner categorySpinner, continentSpinner, countrySpinner;
    private String[] continents = {"All","Asia", "Europe", "America", "Africa", "Oceania", "South America"};


    private DatabaseReference mDatabase;
    private DatabaseReference mRef;
    private Query mQuery;
    private ValueEventListener mListener;
    private ChildEventListener mQueryListener;

    private FirebaseRecyclerAdapter<DashBoard, DashBoardViewHolder> mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mManager;
    private String category, continent, country;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = view.findViewById(R.id.dashBoardRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        categorySpinner = view.findViewById(R.id.categorySpinner);
        continentSpinner = view.findViewById(R.id.continentSpinner);
        countrySpinner = view.findViewById(R.id.countrySpinner);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = categorySpinner.getSelectedItem().toString();
                Log.i(TAG, "태그선택" + category);
                if (mAdapter != null) {
                    mAdapter.stopListening();
                }
                cleanBasicListener();
                cleanBasicQuery();

                if(category.equals("전체")) {
                    postView(0);
                } else {
                    postView(1);
                }

                mAdapter.startListening();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        continentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSpinnerCountry(position, countrySpinner);
                continent = continentSpinner.getSelectedItem().toString();

                if (mAdapter != null) {
                    mAdapter.stopListening();
                }
                cleanBasicListener();
                cleanBasicQuery();

                if(category.equals("전체")) {
                    postView(0);
                } else {
                    postView(1);
                }
                mAdapter.startListening();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");

        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mManager);

    } // end onActivityCreated()

    private void postView(int type) {
        switch (type) {
            case 0:
                // Set up FirebaseRecyclerAdapter with the Query
                mQuery = basicQuery();
                break;
            case 1:
                mQuery = categoryQuery();
                break;
        }

        setData(mQuery);
    }

    public void postContinentView(String btnContinent){
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
        cleanBasicListener();
        cleanBasicQuery();

        Log.i(TAG, "postContinentView " + btnContinent);

        Query cquery = mDatabase.child("posts").orderByChild("continent").equalTo(btnContinent);
        setData(cquery);
        mAdapter.startListening();

        // TODO 스피너 지정해주기

//        categorySpinner.setSelection(0);
//        if(btnContinent.equals(continents[1])) {
//            continentSpinner.setSelection(1);
//        } else if (btnContinent.equals(continents[2])) {
//            continentSpinner.setSelection(2);
//        } else if (btnContinent.equals(continents[3])) {
//            continentSpinner.setSelection(3);
//        } else if (btnContinent.equals(continents[4])) {
//            continentSpinner.setSelection(4);
//        } else if (btnContinent.equals(continents[5])) {
//            continentSpinner.setSelection(5);
//        } else if (btnContinent.equals(continents[6])) {
//            continentSpinner.setSelection(6);
//        }
//        countrySpinner.setSelection(0);

    }

    private void setData(Query query) {
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<DashBoard>()
                .setQuery(query, DashBoard.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<DashBoard, DashBoardViewHolder>(options) {
            @Override
            public DashBoardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new DashBoardViewHolder(inflater.inflate(R.layout.dashboard_item, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(DashBoardViewHolder viewHolder, int position, final DashBoard model) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailDashboardView.class);
                        intent.putExtra(DetailDashboardView.EXTRA_POST, model);
                        startActivity(intent);
                    }
                });

                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference gpostRef = mDatabase.child("posts").child(postRef.getKey());
                        // Run two transactions
                        onlikeClicked(gpostRef);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    private void onlikeClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                DashBoard d = mutableData.getValue(DashBoard.class);
                if(d == null) {
                    return Transaction.success(mutableData);
                }

                mutableData.setValue(d);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }

    public void setSpinnerCountry(int position, Spinner countrySpinner) {
        ArrayAdapter<CharSequence> adpter;
        if(position == 1) {
            adpter = ArrayAdapter.createFromResource(getActivity(), R.array.Asia_Country, android.R.layout.simple_spinner_item);
        } else if(position == 2) {
            adpter = ArrayAdapter.createFromResource(getActivity(), R.array.Europe_Country, android.R.layout.simple_spinner_item);
        } else if(position == 3) {
            adpter = ArrayAdapter.createFromResource(getActivity(), R.array.America_, android.R.layout.simple_spinner_item);
        } else if(position == 4) {
            adpter = ArrayAdapter.createFromResource(getActivity(), R.array.SouthAmerica_Country, android.R.layout.simple_spinner_item);
        } else if(position == 5) {
            adpter = ArrayAdapter.createFromResource(getActivity(), R.array.Africa_Country, android.R.layout.simple_spinner_item);
        } else if(position == 6){
            adpter = ArrayAdapter.createFromResource(getActivity(), R.array.Oceania_, android.R.layout.simple_spinner_item);
        } else {
            adpter = ArrayAdapter.createFromResource(getActivity(), R.array.initialCountry, android.R.layout.simple_spinner_item);
        }

        adpter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        countrySpinner.setAdapter(adpter);
    }


//    public String getUid() {
//        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
//    }

    public Query basicQuery() {
        Query query = mDatabase.child("posts").limitToFirst(100);
        return query;
    }

    public Query categoryQuery(){
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Log.i(TAG, "카테고리" + category);
        Query categoryQuery = mDatabase.child("posts").orderByChild("category").equalTo(category);
        return categoryQuery;
    }

    public void cleanBasicListener() {
        if (mRef != null) {
            mRef.removeEventListener(mListener);
        }
    }

    public void cleanBasicQuery(){
        if (mQuery != null && mQueryListener != null) {
            mQuery.removeEventListener(mQueryListener);
        }
    }
}