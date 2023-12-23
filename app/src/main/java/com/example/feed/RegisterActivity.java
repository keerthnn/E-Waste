    package com.example.feed;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;

    import java.util.regex.Pattern;


    public class

    RegisterActivity extends AppCompatActivity {

        private static final String TAG = ">";
        private EditText ETemail;
        private EditText ETpassword;

        private EditText ETconfirmPassword;

        private FirebaseAuth mAuth;

        protected void registerUser(){
            String email = ETemail.getText().toString().trim();
            String password = ETpassword.getText().toString().trim();
            String confirmPassword = ETconfirmPassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
                Toast.makeText(this," Invalid Email ",Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(this," Invalid Password ",Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Toast.makeText(RegisterActivity.this," User already exists, Logging in... ", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegisterActivity.this, SecondActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                    createUser();
                            }

                            // ...
                        }
                    });



        }

        protected void createUser(){

            String email = ETemail.getText().toString().trim();
            String password = ETpassword.getText().toString().trim();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(RegisterActivity.this,"Logging in... ", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, SecondActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Email already exists/Invalid Email.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }

                            // ...
                        }
                    });
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.register_activity);

            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();

            ETemail = (EditText)findViewById(R.id.email);
            ETpassword = (EditText)findViewById(R.id.password);
            ETconfirmPassword = findViewById(R.id.confirm_password);


            Button register = (Button) findViewById(R.id.register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = ETemail.getText().toString();
                    String password = ETpassword.getText().toString();
                    String confirmPassword = ETconfirmPassword.getText().toString();
                    if (!isValidPassword(password)){
                        Toast.makeText(RegisterActivity.this,"Password Does not matches rules",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!password.equals(confirmPassword)) {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(RegisterActivity.this,FirstActivity.class);
                    intent.putExtra("Email",email);
                    intent.putExtra("Password",password);
                    registerUser();
                }
            });
        }

        @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            //updateUI(currentUser);
        }
        Pattern lowercase = Pattern.compile("^.*[a-z].*$");
        Pattern uppercase = Pattern.compile("^.*[A-Z].*$");
        Pattern number = Pattern.compile("^.*[0-9].*$");
        Pattern specialCharacter = Pattern.compile("^.*[^a-zA-Z0-9].*$");
        private Boolean isValidPassword(String password){
            if (password.length()<8){
                return false;
            }
            if (!lowercase.matcher(password).matches()){
                return false;
            }
            if (!uppercase.matcher(password).matches()){
                return false;
            }
            if (!number.matcher(password).matches()){
                return false;
            }
            if (!specialCharacter.matcher(password).matches()){
                return false;
            }

            return true;
        }
    }
