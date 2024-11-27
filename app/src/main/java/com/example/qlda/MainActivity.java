package com.example.qlda;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlda.home.ConnectSqlServer;
import com.example.qlda.login.LoginActivity;
import com.google.firebase.FirebaseApp;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

//        ConnectSqlServer connectSqlServer = new ConnectSqlServer();
//        Connection connection = connectSqlServer.CONN();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}