package com.example.manish.wpaper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private LinearLayout mPhoneLayout, mCodeLayout;
    private EditText mPhoneText, mCodeText;
    private ProgressBar mPhone_progressbar,mCode_progressbar;
    private Button mSendBtn;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private int SendByn_Type = 0;

    public String TAG = "Auth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();

        mPhoneLayout = (LinearLayout)findViewById(R.id.ll1);
        mCodeLayout = (LinearLayout)findViewById(R.id.ll2);

        mPhoneText = (EditText)findViewById(R.id.phoneno);
        mCodeText = (EditText)findViewById(R.id.verifycode);

        mPhone_progressbar = (ProgressBar)findViewById(R.id.phoneno_progress);
        mCode_progressbar = (ProgressBar)findViewById(R.id.verifycode_progress);

        mSendBtn = (Button)findViewById(R.id.sendcode);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SendByn_Type == 0) {

                    mPhone_progressbar.setVisibility(View.VISIBLE);
                    mPhoneText.setEnabled(false);
                    mSendBtn.setEnabled(false);

                    String PhoneNo = mPhoneText.getText().toString();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            PhoneNo,
                            60,
                            TimeUnit.SECONDS,
                            AuthActivity.this,
                            mCallBacks

                    );
                }else {

                    mSendBtn.setEnabled(false);
                    mCodeLayout.setVisibility(View.VISIBLE);

                    String VerificationCode = mCodeText.getText().toString();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,VerificationCode);
                    signInWithPhoneAuthCredential(credential);

                }
            }
        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);

                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(AuthActivity.this,"INvalid Req",Toast.LENGTH_SHORT).show();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(AuthActivity.this,"SMS Quota exceeded",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("Authh",e.getMessage());
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;

                SendByn_Type = 1;

                mPhone_progressbar.setVisibility(View.INVISIBLE);
                mCodeLayout.setVisibility(View.VISIBLE);

                mSendBtn.setText("Verify Code");
                mSendBtn.setEnabled(true);
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = task.getResult().getUser();

                            Intent mainIntent = new Intent(AuthActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                            finish();

                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(AuthActivity.this,"Invalid Code",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

}
