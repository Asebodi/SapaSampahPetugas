package id.sapasampah.petugas;

import android.service.autofill.TextValueSanitizer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Calendar;

public class InputActivity extends AppCompatActivity {

    CollectionReference mColRef = FirebaseFirestore.getInstance().collection("users");
    Toolbar inputToolbar;
    TextView userAddr, inputBalance;
    EditText inputWeight;
    Button inputBtn;
    String uid, balance, weight;
    ProgressBar inputProgressBar;

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
        inputProgressBar = findViewById(R.id.inputProgressBar);

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

        inputWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String weightET = inputWeight.getText().toString();

                if (!weightET.isEmpty()) {
                    Integer balance = Integer.parseInt(inputWeight.getText().toString());
                    String balanceFormat = String.format("%,d", balance);
                    String balanceDisp = "Rp " + balanceFormat;
                    inputBalance.setText(balanceDisp);
                } else {
                    inputBalance.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("InputActivity", "onClick: Clicked");
                weight = inputWeight.getText().toString();
                if (weight.isEmpty()) {
                    inputWeight.setError("Berat belum terisi!");
                    inputWeight.requestFocus();
                    return;
                }

                inputProgressBar.setVisibility(View.VISIBLE);
                int weight = Integer.parseInt(inputWeight.getText().toString());
                double weightInKg = weight/1000;
                String weightKgStr = String.format("%.2f", weightInKg);

                String amount = "Rp " + String.format("%,d", weight);

                int intBalance = Integer.parseInt(balance);

                int input = weight+intBalance;
                String inputStr = Integer.toString(input);

                //TIME STUFF
                Long currentMillis = Calendar.getInstance().getTimeInMillis();

                Calendar calendar = Calendar.getInstance();
                // new Locale("ind", "in_ID") after the pattern for Indonesia locale
                SimpleDateFormat date = new SimpleDateFormat("dd MMMM yyyy");
                SimpleDateFormat time = new SimpleDateFormat("hh:mm a");

                String currentDate = date.format(calendar.getTime());
                String currentTime = time.format(calendar.getTime());

                Map<String, Object> balanceUpdate = new HashMap<>();
                balanceUpdate.put("balance",inputStr);

                final Map<String, Object> pickupAdd = new HashMap<>();
                pickupAdd.put("date", currentDate);
                pickupAdd.put("time", currentTime);
                pickupAdd.put("amount", amount);
                pickupAdd.put("weight", weightKgStr);
                pickupAdd.put("total", String.format("%,d", input));
                pickupAdd.put("epoch", currentMillis);

                mColRef.document(uid).update(balanceUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mColRef.document(uid).collection("pickup").add(pickupAdd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                inputProgressBar.setVisibility(View.GONE);
                                Toast.makeText(InputActivity.this, "Input sampah berhasil!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                inputProgressBar.setVisibility(View.GONE);
                                Toast.makeText(InputActivity.this, "Terjadi sebuah masalah", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
