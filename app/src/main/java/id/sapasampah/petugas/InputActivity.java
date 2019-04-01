package id.sapasampah.petugas;

import android.service.autofill.TextValueSanitizer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InputActivity extends AppCompatActivity {

    CollectionReference mColRef = FirebaseFirestore.getInstance().collection("users");
    Toolbar inputToolbar;
    TextView userAddr, inputWeight, inputBalance;
    Button inputBtn;
    String uid, balance, weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        if (getIntent().hasExtra("qrValue")) {
            uid = getIntent().getExtras().getString("qrValue");
        }

        inputToolbar = findViewById(R.id.inputToolbar);
        setSupportActionBar(inputToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userAddr = findViewById(R.id.userAddr);
        inputWeight = findViewById(R.id.inputWeight);
        inputBalance = findViewById(R.id.inputBalance);
        inputBtn = findViewById(R.id.inputBtn);
        weight = inputWeight.getText().toString();

        mColRef.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String province = documentSnapshot.getString("province");
                    String city = documentSnapshot.getString("city");
                    String district = documentSnapshot.getString("district");
                    String postal = documentSnapshot.getString("postal");
                    String fullAddr = documentSnapshot.getString("fullAddr");
                    balance = documentSnapshot.getString("balance");
                    userAddr.setText(fullAddr + ", " + district + ", " + city + ", " + province + ", " + postal);
                }
            }
        });

        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!weight.isEmpty()) {
                    int weight = Integer.parseInt(inputWeight.getText().toString());
                    int intBalance = Integer.parseInt(balance);

                    int input = weight+intBalance;
                    String inputStr = Integer.toString(input);

                    Map<String, Object> balanceUpdate = new HashMap<>();
                    balanceUpdate.put("balance",inputStr);

                    mColRef.document(uid).update(balanceUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(InputActivity.this, "Input sampah berhasil!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(InputActivity.this, "Terjadi sebuah masalah", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}
