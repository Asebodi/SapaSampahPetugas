package id.sapasampah.petugas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FrameLayout homeFrameLayout;

    private AddressFragment addressFragment;
    private QrFragment qrFragment;
    private ProfileFragment profileFragment;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user == null) {
                    Intent goLogin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(goLogin);
                    finish();
                }
            }
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        homeFrameLayout = findViewById(R.id.homeFrameLayout);

        addressFragment = new AddressFragment();
        qrFragment = new QrFragment();
        profileFragment = new ProfileFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.menuAddr:
                        bottomNavigationView.setItemBackgroundResource(R.color.colorAccent);
                        setFragment(addressFragment);
                        return true;

                    case R.id.menuQr:
                        bottomNavigationView.setItemBackgroundResource(R.color.colorAccent);
                        setFragment(qrFragment);
                        return true;

                    case R.id.menuProfile:
                        bottomNavigationView.setItemBackgroundResource(R.color.colorAccent);
                        setFragment(profileFragment);
                        return true;

                        default:
                            return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction homeFragmentTransaction = getSupportFragmentManager().beginTransaction();
        homeFragmentTransaction.replace(R.id.homeFrameLayout, fragment);
        homeFragmentTransaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
}

