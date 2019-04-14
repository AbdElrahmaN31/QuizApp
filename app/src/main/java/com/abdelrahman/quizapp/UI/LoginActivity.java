package com.abdelrahman.quizapp.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abdelrahman.quizapp.R;
import com.abdelrahman.quizapp.UI.Home.HomeActivity;
import com.abdelrahman.quizapp.Model.User;
import com.abdelrahman.quizapp.Utils.IntentUtil;
import com.abdelrahman.quizapp.Utils.SnackBarUtil;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        Validator.ValidationListener, ValueEventListener {


    private static final String TAG = LoginActivity.class.getSimpleName();

    private TextView textLogin, textSignUp;
    private RelativeLayout activityLoginLayout;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference users;
    private Validator validator;
    @NotEmpty
    private TextInputEditText editTextUser;
    @Password(min = 8, message = "Password minimum length is 8")
    private TextInputEditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase initiation
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        users = mFirebaseDatabase.getReference("Users");

        validator = new Validator(this);
        validator.setValidationListener(this);

        activityLoginLayout = findViewById(R.id.login_layout);

        //connect views by their ids
        editTextUser = findViewById(R.id.edit_login_name);
        editTextPassword = findViewById(R.id.edit_login_password);

        textLogin = findViewById(R.id.text_login_login);
        textSignUp = findViewById(R.id.text_login_sign_up);

        textLogin.setOnClickListener(this);
        textSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_login_login:
                validator.validate(true);
                break;
            case R.id.text_login_sign_up:
                IntentUtil.makeIntent(this, SignUpActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        users.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            }
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        String userName = editTextUser.getText().toString(),
                password = editTextPassword.getText().toString();
        if (dataSnapshot.child(userName).exists()) {
            User login = dataSnapshot.child(userName).getValue(User.class);
            if (login.getPassword().equals(password)) {
                SnackBarUtil.makeSnackBar(this, activityLoginLayout, "You are logged in!", Snackbar.LENGTH_SHORT);
                IntentUtil.makeIntent(this, HomeActivity.class);
            } else if (!login.getPassword().equals(password)) {
                SnackBarUtil.makeSnackBar(this, activityLoginLayout, "Wrong password !", Snackbar.LENGTH_SHORT);
            }
        } else {
            SnackBarUtil.makeSnackBar(this, activityLoginLayout, "User not exists !", Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
