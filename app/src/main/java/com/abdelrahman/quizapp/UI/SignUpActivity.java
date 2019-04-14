package com.abdelrahman.quizapp.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abdelrahman.quizapp.R;
import com.abdelrahman.quizapp.UI.Home.HomeActivity;
import com.abdelrahman.quizapp.Model.User;
import com.abdelrahman.quizapp.Utils.IntentUtil;
import com.abdelrahman.quizapp.Utils.SnackBarUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener,
        ValueEventListener, OnSuccessListener, OnFailureListener {
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private static final String EMAIL_ADDRESS_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";
    @Pattern(regex = EMAIL_ADDRESS_PATTERN, message = "Input does not match email pattern")
    private TextInputEditText editTextNewEmail;
    @NotEmpty
    private TextInputEditText editTextNewUser;
    @Password(min = 8, message = "Password minimum length is 8")
    private TextInputEditText editTextNewPassword;
    @ConfirmPassword
    private TextInputEditText editTextConfirmPassword;
    private TextView textSignUp, textLogin;
    private RelativeLayout sinUpLayout;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference users;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Firebase initiation
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        users = mFirebaseDatabase.getReference("Users");

        validator = new Validator(this);
        validator.setValidationListener(this);

        sinUpLayout = findViewById(R.id.sign_up_layout);

        editTextNewUser = findViewById(R.id.edit_sign_up_name);
        editTextNewEmail = findViewById(R.id.edit_sign_up_email);
        editTextNewPassword = findViewById(R.id.edit_sign_up_password);
        editTextConfirmPassword = findViewById(R.id.edit_sign_up_confirm_password);

        textSignUp = findViewById(R.id.text_sign_up_sign_up);
        textLogin = findViewById(R.id.text_sign_up_login);

        textSignUp.setOnClickListener(this);
        textLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG,"Item clicked !");
        switch (v.getId()) {
            case R.id.text_sign_up_sign_up:
                validator.validate(true);
                break;
            case R.id.text_sign_up_login:
                IntentUtil.makeIntent(this, LoginActivity.class);
                break;
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        User user = new User(editTextNewUser.getText().toString(),
                editTextNewEmail.getText().toString(),
                editTextNewPassword.getText().toString());

        if (dataSnapshot.child(user.getUserName()).exists()) {
            SnackBarUtil.makeSnackBar(this ,sinUpLayout, "User already exists !", Snackbar.LENGTH_LONG);
        } else {
            users.child(user.getUserName()).setValue(user)
                    .addOnSuccessListener(this)
                    .addOnFailureListener(this);
        }
    }



    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

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
    public void onSuccess(Object o) {
        SnackBarUtil.makeSnackBar(this,sinUpLayout,"Registration Success !",Snackbar.LENGTH_LONG);
        IntentUtil.makeIntent(this, HomeActivity.class);

    }

    @Override
    public void onFailure(@NonNull Exception e) {
        SnackBarUtil.makeSnackBar(this,sinUpLayout,"Registration Failed ! \n" + e.getMessage() ,Snackbar.LENGTH_LONG);
    }


}
