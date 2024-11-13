//package com.example.qlda.login;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.qlda.R;
//
//public class Login extends AppCompatActivity {
//    public static final String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-z]{2,}";
//    private EditText txtEmail;
//    private EditText txtpassword;
//    private TextView txtValidate;
//    private Button btnLogin;
//    private Boolean isValidEmail = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView((R.layout.issue_dashboard));
//        txtEmail = findViewById(R.id.txtEmail);
//        txtpassword = (EditText) findViewById(R.id.txtLogin);
//        txtValidate = (TextView) findViewById(R.id.txtValidate);
//        btnLogin = (Button) findViewById(R.id.btnLogin);
//
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!isValidEmail){
//                    Toast.makeText(getApplicationContext(),"Validate Email false",
//                            Toast.LENGTH_LONG).show();
//                    return;
//                }
//            }
//        });
//        txtEmail.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                txtValidate.setText("");
//                String email = txtEmail.getText().toString().trim();
//                isValidEmail = (email.matches(emailPattern)&& s.length() >0);
//                if(!isValidEmail){
//                    txtValidate.setTextColor(Color.rgb(255,0,0));
//                    txtValidate.setText("Invalid email address");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }
//}
