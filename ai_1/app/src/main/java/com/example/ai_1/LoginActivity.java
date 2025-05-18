package com.example.ai_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ai_1.databinding.ActivityLoginBinding;
import com.example.ai_1.model.User;
import com.example.ai_1.model.UserRequest;
import com.example.ai_1.network.ApiService;
import com.example.ai_1.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = RetrofitClient.getClient().create(ApiService.class);

        binding.loginButton.setOnClickListener(v -> login());
        binding.registerButton.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void login() {
        String username = binding.usernameInput.getText().toString();
        String password = binding.passwordInput.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        UserRequest request = new UserRequest();
        request.setUsername(username);
        request.setPassword(password);

        apiService.login(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        // 可以在这里保存用户信息到SharedPreferences
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "登录失败：服务器返回数据为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody() != null ? response.errorBody().string() : "未知错误";
                    } catch (Exception e) {
                        errorBody = "解析错误信息失败";
                    }
                    Toast.makeText(LoginActivity.this, "登录失败：" + errorBody, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                String errorMessage = t.getMessage();
                if (errorMessage == null) {
                    errorMessage = "网络连接失败，请检查网络设置";
                }
                Toast.makeText(LoginActivity.this, "网络错误：" + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
} 