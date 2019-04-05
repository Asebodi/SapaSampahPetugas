package id.sapasampah.petugas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class
LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText loginEmail, loginPass;
    Button loginBtn;
    ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            Intent goToMain = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(goToMain);
            finish();
        }

        loginEmail = findViewById(R.id.loginEmail);
        loginPass = findViewById(R.id.loginPass);
        loginProgressBar = findViewById(R.id.loginProgressBar);

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = loginEmail.getText().toString();
        String password = loginPass.getText().toString();

        if (email.isEmpty()) {
            loginEmail.setError("Email kosong!");
            loginEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            loginPass.setError("Password Kosong!");
            loginPass.requestFocus();
            return;
        }

        loginProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loginProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Intent goIn = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(goIn);
                    Toast.makeText(LoginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Terjadi masalah", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
