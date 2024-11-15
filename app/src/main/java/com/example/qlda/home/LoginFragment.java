package com.example.qlda.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.qlda.R;

public class LoginFragment extends Fragment {

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            title = getArguments().getString(ARG_TITLE);
//            color = getArguments().getInt(ARG_COLOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_sign_up, container, false);

        // Khởi tạo các thành phần giao diện
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPassword = view.findViewById(R.id.etPassword);
        Button btnLogin = view.findViewById(R.id.btnSignIn);
        Button btnSignUp = view.findViewById(R.id.btnSignUp);

        // Xử lý sự kiện nhấn nút Đăng nhập
        btnLogin.setOnClickListener(v -> {
            String username = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            // Thực hiện logic đăng nhập ở đây
            Toast.makeText(getActivity(), "Đăng nhập với: " + username, Toast.LENGTH_SHORT).show();
            // Có thể thêm logic xác thực ở đây

        });

        return view;
    }


}
