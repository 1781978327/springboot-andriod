package com.example.ai_1;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ai_1.databinding.ActivityRegisterBinding;
import com.example.ai_1.model.User;
import com.example.ai_1.model.UserRequest;
import com.example.ai_1.network.ApiService;
import com.example.ai_1.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = RetrofitClient.getClient().create(ApiService.class);

        binding.registerButton.setOnClickListener(v -> register());
    }

    private void register() {
        String username = binding.usernameInput.getText().toString();
        String email = binding.emailInput.getText().toString();
        String password = binding.passwordInput.getText().toString();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
            return;
        }

        UserRequest request = new UserRequest();
        request.setUsername(username);
        request.setEmail(email);
        request.setPassword(password);

        apiService.register(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "注册失败：服务器返回数据为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody() != null ? response.errorBody().string() : "未知错误";
                    } catch (Exception e) {
                        errorBody = "解析错误信息失败";
                    }
                    Toast.makeText(RegisterActivity.this, "注册失败：" + errorBody, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                String errorMessage = t.getMessage();
                if (errorMessage == null) {
                    errorMessage = "网络连接失败，请检查网络设置";
                }
                Toast.makeText(RegisterActivity.this, "网络错误：" + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
} 