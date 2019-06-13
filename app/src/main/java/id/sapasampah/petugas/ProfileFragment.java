package id.sapasampah.petugas;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ConstraintLayout option0, option1, option2, logout;
    TextView profileName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        final CollectionReference mActive = db.collection("active");
        final CollectionReference mFinished = db.collection("finished");

        option0 = view.findViewById(R.id.profileOption0);
        option1 = view.findViewById(R.id.profileOption1);
        option2 = view.findViewById(R.id.profileOption2);
        logout = view.findViewById(R.id.profileLogout);
        profileName = view.findViewById(R.id.profileName);

        if (mAuth.getCurrentUser() != null) {
            String operatorName = mAuth.getCurrentUser().getDisplayName();
            profileName.setText(operatorName);
        }

        WriteBatch batch = db.batch();

        option0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFinished.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("ProfileFragment", "onComplete: " + document.getId() + ", " + document.getData());
                                mActive.document(document.getId()).set(document.getData()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mFinished.document(document.getId()).delete();
                                        Toast.makeText(getContext(), "Reset alamat berhasil", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Terjadi kesalahan saat mereset", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else {
                            Toast.makeText(getContext(), "Daftar alamat telah direset", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToGuide = new Intent(getContext(), GuideActivity.class);
                startActivity(goToGuide);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAbout = new Intent(getContext(), AboutActivity.class);
                startActivity(goToAbout);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        return view;
    }

}
