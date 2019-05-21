package edu.android.hashtravel;


import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {


    private static final String TAG = "fragmentRecycle";
    private Spinner continentSpinner, countrySpinner;
//    private String[] continents = {"Asia", "Europe", "America", "South America", "Africa", "Oceania"};


    private ImageButton buttonWrite;
    private String user;
    public DashboardFragment() {
        // Required empty public constructor
    }

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<DashBoard, DashBoardViewHolder> mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView의 생성");
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = view.findViewById(R.id.dashBoardrecyclerView);
        mRecyclerView.setHasFixedSize(true);

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<DashBoard>()
                .setQuery(postsQuery, DashBoard.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<DashBoard, DashBoardViewHolder>(options) {

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }


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

                Log.i(TAG,postKey);
                Log.i(TAG, getUid());
                // TODO
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailDashboardView.class);
                        intent.putExtra(DetailDashboardView.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.bindToPost(model, getUid(),new View.OnClickListener() {
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
    } // end onActivityCreated()

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


    private void getWriteBoard() {
        Intent intent = new Intent(getActivity(), WriteBordActivity.class);
        startActivity(intent);
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

    @Override
    public void onStart() {
        super.onStart();
        if(mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public Query getQuery(DatabaseReference databaseReference){
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("posts")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentPostsQuery;

    }

}
