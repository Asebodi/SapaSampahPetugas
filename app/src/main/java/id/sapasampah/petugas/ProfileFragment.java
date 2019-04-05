package id.sapasampah.petugas;


import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    FirebaseAuth mAuth;
    ConstraintLayout option1, option2, logout;
    TextView profileName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        option1 = view.findViewById(R.id.profileOption1);
        option2 = view.findViewById(R.id.profileOption2);
        logout = view.findViewById(R.id.profileLogout);
        profileName = view.findViewById(R.id.profileName);

        if (mAuth.getCurrentUser() != null) {
            String operatorName = mAuth.getCurrentUser().getDisplayName();
            profileName.setText(operatorName);
        }

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
