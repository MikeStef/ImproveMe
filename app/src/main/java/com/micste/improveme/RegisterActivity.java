package com.micste.improveme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText et_email, et_password, et_name;
    private Button btn_register, btn_login;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_name = (EditText) findViewById(R.id.et_name);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.login_link);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void buttonActions(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                registerAccount(et_email.getText().toString(), et_password.getText().toString());
                break;
            case R.id.login_link:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private boolean formValidate() {
        boolean valid = true;

        String email = et_email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            et_email.setError("Required");
            valid = false;
        } else {
            et_email.setError(null);
        }

        String password = et_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            et_password.setError("Required");
            valid = false;
        } else {
            et_password.setError(null);
        }

        String name = et_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            et_name.setError("Required");
            valid = false;
        } else {
            et_name.setError(null);
        }

        return valid;

    }

    private void registerAccount(String email, String password) {
        Log.d(TAG, "registerAccount: " + email);

        if (!formValidate()) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUser successfull");

                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            String name = et_name.getText().toString();
                            String email = et_email.getText().toString();
                            writeNewUser(currentUser.getUid(), name, email);

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed: " +
                                    task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    public void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        databaseReference.child("users").child(userId).setValue(user);
    }
}
