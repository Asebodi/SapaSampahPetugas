package id.sapasampah.petugas;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment {


    public AddressFragment() {
        // Required empty public constructor
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActiveAdapter adapterActive;
    private FinishedAdapter adapterFinished;
    private RecyclerView recyclerViewActive, recyclerViewFinished;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address, container, false);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        recyclerViewActive = view.findViewById(R.id.activeRecyclerView);
        recyclerViewFinished = view.findViewById(R.id.finishedRecyclerView);

        setUpRecyclerView();
        setUpFinished();

        return view;
    }

    private void setUpFinished() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            //Finished RecyclerView
            CollectionReference mFinished = db.collection("finished");

            Query queryFinished = mFinished;
            FirestoreRecyclerOptions<Finished> optionsFinished = new FirestoreRecyclerOptions.Builder<Finished>().setQuery(queryFinished, Finished.class).build();

            adapterFinished = new FinishedAdapter(optionsFinished);
            recyclerViewFinished.setHasFixedSize(true);
            recyclerViewFinished.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewFinished.setAdapter(adapterFinished);
        }
    }

    private void setUpRecyclerView() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            //Active RecyclerView
            CollectionReference mActive = db.collection("active");

            Query queryActive = mActive;
            FirestoreRecyclerOptions<Active> optionsActive = new FirestoreRecyclerOptions.Builder<Active>().setQuery(queryActive, Active.class).build();

            adapterActive = new ActiveAdapter(optionsActive);
            recyclerViewActive.setHasFixedSize(true);
            recyclerViewActive.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewActive.setAdapter(adapterActive);
            recyclerViewActive.setNestedScrollingEnabled(false);

        } else {
            Intent goLogin = new Intent(getContext(), LoginActivity.class);
            startActivity(goLogin);
            goLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            adapterActive.startListening();
            adapterFinished.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            adapterActive.stopListening();
            adapterFinished.stopListening();
        }
    }

}
