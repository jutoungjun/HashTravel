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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private static DashboardFragment instance = null;
    public DashboardFragment() {
        // Required empty public constructor ?
    }
    public static DashboardFragment getInstance() {
        if (instance == null) {
            instance = new DashboardFragment();
        }
        return instance;
    }
    private static final String TAG = "fragmentRecycle";
    private Spinner categorySpinner, continentSpinner, countrySpinner;
    private String[] continents = {"All", "Asia", "Europe", "America", "Africa", "Oceania", "South America"};
    private DatabaseReference mDatabase;
    private DatabaseReference mRef;
    private Query mQuery;
    private ValueEventListener mListener;
    private ChildEventListener mQueryListener;
    private FirebaseRecyclerAdapter<DashBoard, DashBoardViewHolder> mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mManager;
    private String category, continent, country;
    private Button btnSearch;
    private EditText editSearch;
    //
    private MyRecycleAdapter adapter;
    private List<DashBoard> mList = new ArrayList<>();
    class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder> {
        public MyRecycleAdapter(List<DashBoard> list) {
            mList = list;
        }
        @NonNull
        @Override
        public MyRecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_item, viewGroup, false);
            return new MyViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            holder.textSubject.setText(mList.get(position).getSubject());
            holder.textHashTag.setText(mList.get(position).getHashTag());
            holder.textUserId.setText(mList.get(position).getUsername());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), DetailDashboardActivity.class);
                    intent.putExtra(DetailDashboardActivity.EXTRA_POST, mList.get(position));
                    startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount() {
            return mList.size();
        }
        private class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView textSubject, textHashTag, textUserId;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textSubject = itemView.findViewById(R.id.textSubject);
                textHashTag = itemView.findViewById(R.id.textHashTag);
                textUserId = itemView.findViewById(R.id.textUserId);
            }
        }
    }
    // 여기까지
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        btnSearch = view.findViewById(R.id.btnSearch);
        editSearch = view.findViewById(R.id.editSearch);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecyclerView = view.findViewById(R.id.dashBoardRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        continentSpinner = view.findViewById(R.id.continentSpinner);
        countrySpinner = view.findViewById(R.id.countrySpinner);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String searchKey = editSearch.getText().toString();
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new MyRecycleAdapter(mList);
                mDatabase.child("posts").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        DashBoard dashBoard = dataSnapshot.getValue(DashBoard.class);
                        adapter.notifyDataSetChanged();
                        if(dashBoard.getSubject().contains(searchKey) || dashBoard.getDescription().contains(searchKey)) {
                            mList.add(dashBoard);
                        }
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                adapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(adapter);

                mList = new ArrayList<>();
                editSearch.setText("");

            }
        });
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
                setData(basicQuery());
                mAdapter.notifyDataSetChanged();

                mRecyclerView.setAdapter(mAdapter);

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

//                postCateContiView(category, continent);
//                if (mAdapter != null) {
//                    mAdapter.stopListening();
//                }
//                cleanBasicListener();
//                cleanBasicQuery();

                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new MyRecycleAdapter(mList);
                mDatabase.child("posts").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        DashBoard dashBoard = dataSnapshot.getValue(DashBoard.class);
                        adapter.notifyDataSetChanged();
                        Log.i(TAG, category + " " + continent);
                        if(category.equals("전체")) {
                            if(dashBoard.getContinent().equals(continent)) {
                                mList.add(dashBoard);
                            }
                        }
                        if(dashBoard.getCategory().equals(category) && dashBoard.getContinent().equals(continent)) {
                            mList.add(dashBoard);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        if (mAdapter != null) {
                            mAdapter.stopListening();
                        }
                        cleanBasicListener();
                        cleanBasicQuery();

                    }
                });
                setData(basicQuery());
                mAdapter.notifyDataSetChanged();

                adapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setAdapter(mAdapter);

                mList = new ArrayList<>();
                mAdapter.startListening();
//                mList = new ArrayList<>();
//                if(category.equals("전체")) {
//                    postView(0);
//                } else {
//                    postView(1);
//                }
//                mAdapter.startListening();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country = countrySpinner.getSelectedItem().toString();
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new MyRecycleAdapter(mList);
                mDatabase.child("posts").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        DashBoard dashBoard = dataSnapshot.getValue(DashBoard.class);
                        adapter.notifyDataSetChanged();
                        Log.i(TAG, category + " " + continent);
//                        if(category.equals("전체"))
                        if(dashBoard.getCategory().equals(category) && dashBoard.getContinent().equals(continent) && dashBoard.getCountry().equals(country)) {
                            mList.add(dashBoard);
                        }
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                adapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(adapter);
                mList = new ArrayList<>();
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
    public void searchText(final String text) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyRecycleAdapter(mList);
        mDatabase.child("posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DashBoard dashBoard = dataSnapshot.getValue(DashBoard.class);
                adapter.notifyDataSetChanged();
                if(dashBoard.getSubject().contains(text) || dashBoard.getDescription().contains(text)) {
                    mList.add(dashBoard);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
        mList = new ArrayList<>();
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

    public void postCateContiView(String category, final String continent) {
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
        cleanBasicListener();
        cleanBasicQuery();

        Query cquery = mDatabase.child("posts").orderByChild("category").equalTo(category);
//        cquery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    DashBoard dashBoard = dataSnapshot.getValue(DashBoard.class);
//                    if(dashBoard.getContinent().equals(continent)) {
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        setData(cquery);
        mAdapter.startListening();
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
                        Intent intent = new Intent(getActivity(), DetailDashboardActivity.class);
                        intent.putExtra(DetailDashboardActivity.EXTRA_POST, model);
                        intent.putExtra(DetailDashboardActivity.EXTRA_REF, postRef.getKey());
                        startActivity(intent);
                    }
                });
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference gpostRef = mDatabase.child("posts").child(postRef.getKey());
                        gpostRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                DashBoard dashBoard = dataSnapshot.getValue(DashBoard.class);
                                mAdapter.notifyDataSetChanged();

                                if(dashBoard.getCategory().equals(category) && dashBoard.getContinent().equals(continent) && dashBoard.getCountry().equals(country)) {
                                    mList.add(dashBoard);
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
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
            adpter = ArrayAdapter.createFromResource(getActivity(), R.array.Africa_Country, android.R.layout.simple_spinner_item);
        } else if(position == 5) {
            adpter = ArrayAdapter.createFromResource(getActivity(), R.array.Oceania_, android.R.layout.simple_spinner_item);
        } else if(position == 6){
            adpter = ArrayAdapter.createFromResource(getActivity(), R.array.SouthAmerica_Country, android.R.layout.simple_spinner_item);
        } else {
            adpter = ArrayAdapter.createFromResource(getActivity(), R.array.initialCountry, android.R.layout.simple_spinner_item);
        }
        adpter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        countrySpinner.setAdapter(adpter);
    }
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