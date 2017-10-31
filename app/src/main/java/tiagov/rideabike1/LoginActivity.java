package tiagov.rideabike1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private EditText mEmailField;
    private EditText mPasswordField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Views
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);

        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        //findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.verify_email_button).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    //@Override
    //public void onStart() {
    //    super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    //    FirebaseUser currentUser = mAuth.getCurrentUser();
    //}
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        //if (!validateFormNew()) {
        //    return;
        //}

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Conta criada com sucesso",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Erro",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            startActivity(new Intent(getBaseContext(), MainActivity.class));
                            //FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Autenticação falhou.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                            Toast.makeText(LoginActivity.this, "Autenticação falhou",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                        onStop();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
    }

    /*private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verify_email_button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }*/


    private boolean validateFormNew() {
        final View mView = getLayoutInflater().inflate(R.layout.dialog_new_account, null);
        boolean valid = true;

        final EditText mEmail = (EditText) mView.findViewById(R.id.etEmail);
        final EditText mPassword = (EditText) mView.findViewById(R.id.etPassword);

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Campo obrigatório");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Campo obrigatório");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        return valid;
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Campo obrigatório");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Campo obrigatório");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    /*private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);

            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }*/

    public void newAccountDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View mView = getLayoutInflater().inflate(R.layout.dialog_new_account, null);

        builder
                .setTitle("Nova conta")

                .setView(mView)

                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final EditText mEmail = (EditText) mView.findViewById(R.id.etEmail);
                        final EditText mPassword = (EditText) mView.findViewById(R.id.etPassword);

                        final String email = mEmail.getText().toString();
                        final String password = mPassword.getText().toString();

                        createAccount(email, password);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        closeContextMenu();
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            newAccountDialog();
        } else if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }/* else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.verify_email_button) {
            sendEmailVerification();
        }*/
    }
}