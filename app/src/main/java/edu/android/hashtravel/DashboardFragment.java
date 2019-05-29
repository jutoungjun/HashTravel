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
                mManager = new LinearLayoutManager(getActivity());
                mManager.setReverseLayout(true);
                mManager.setStackFromEnd(true);
                mRecyclerView.setLayoutManager(mManager);
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

                selectDatas();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        continentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                continent = continentSpinner.getSelectedItem().toString();
                setSpinnerCountry(position, countrySpinner);
                selectDatas();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country = countrySpinner.getSelectedItem().toString();
                selectDatas();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return view;
    }

    public void selectDatas () {

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mManager);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts");
        adapter = new MyRecycleAdapter(mList);

        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    DashBoard dashBoard = child.getValue(DashBoard.class);
                    ifData(dashBoard);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ref.addValueEventListener(mListener);
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
        mList = new ArrayList<>();
    }


    public void searchText(final String text) {
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mManager);
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
    public void postContinentView(final String btnContinent){
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mManager);
        adapter = new MyRecycleAdapter(mList);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts");
        adapter = new MyRecycleAdapter(mList);

        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    DashBoard dashBoard = child.getValue(DashBoard.class);
                    if(dashBoard.getContinent().equals(btnContinent)) {
                    mList.add(dashBoard);
                }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ref.addValueEventListener(mListener);
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
        mList = new ArrayList<>();

        categorySpinner.setSelection(0);

        if(btnContinent.equals(continents[1])) {
            continentSpinner.setSelection(1);
        } else if (btnContinent.equals(continents[2])) {
            continentSpinner.setSelection(2);
        } else if (btnContinent.equals(continents[3])) {
            continentSpinner.setSelection(3);
        } else if (btnContinent.equals(continents[4])) {
            continentSpinner.setSelection(4);
        } else if (btnContinent.equals(continents[5])) {
            continentSpinner.setSelection(5);
        } else if (btnContinent.equals(continents[6])) {
            continentSpinner.setSelection(6);
        }
        countrySpinner.setSelection(0);

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

    public void ifData(DashBoard dashBoard) {
        if(category.equals("전체") && continent.equals("All") && country.equals("All")) {
            mList.add(dashBoard);
        } else if(category.equals("전체") && !continent.equals("All") && country.equals("All")) {
            if(dashBoard.getContinent().equals(continent)) {
                mList.add(dashBoard);
            }
        } else if(!category.equals("전체") && continent.equals("All") && country.equals("All")) {
            if(dashBoard.getCategory().equals(category)) {
                mList.add(dashBoard);
            }
        } else if(!category.equals("전체") && !continent.equals("All") && country.equals("All")) {
            if(dashBoard.getCategory().equals(category) && dashBoard.getContinent().equals(continent)) {
                mList.add(dashBoard);
            }
        } else if(!category.equals("전체") && !continent.equals("All") && !country.equals("All")) {
            if(dashBoard.getCategory().equals(category) && dashBoard.getContinent().equals(continent) && dashBoard.getCountry().equals(country)) {
                mList.add(dashBoard);
            }
        } else if(category.equals("전체") && !continent.equals("All") && !country.equals("All")) {
            if(dashBoard.getContinent().equals(continent) && dashBoard.getCountry().equals(country)) {
                mList.add(dashBoard);
            }
        }
    }


}