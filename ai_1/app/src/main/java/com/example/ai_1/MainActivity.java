package com.example.ai_1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ai_1.adapter.ChatAdapter;
import com.example.ai_1.model.ChatMessage;
import com.example.ai_1.model.ChatRequest;
import com.example.ai_1.network.ApiService;
import com.example.ai_1.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private EditText messageInput;
    private MaterialButton sendButton;
    private ProgressBar progressBar;
    private ChatAdapter adapter;
    private List<ChatMessage> messages;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        recyclerView = findViewById(R.id.recyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        progressBar = findViewById(R.id.progressBar);

        // 初始化消息列表
        messages = new ArrayList<>();
        adapter = new ChatAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 初始化API服务
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // 设置发送按钮点击事件
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (message.isEmpty()) {
            return;
        }

        // 清空输入框
        messageInput.setText("");

        // 添加用户消息到列表
        messages.add(new ChatMessage(message, true));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);

        // 显示加载状态
        progressBar.setVisibility(View.VISIBLE);
        sendButton.setEnabled(false);

        Log.d(TAG, "发送消息: " + message);

        // 发送请求到服务器
        apiService.askQuestion(new ChatRequest(message)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.GONE);
                sendButton.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body();
                    Log.d(TAG, "收到响应: " + responseBody);
                    
                    // 添加AI回复到列表
                    messages.add(new ChatMessage(responseBody, false));
                    adapter.notifyItemInserted(messages.size() - 1);
                    recyclerView.scrollToPosition(messages.size() - 1);
                } else {
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "读取错误响应失败", e);
                    }
                    Log.e(TAG, "服务器响应错误: " + response.code() + ", 错误信息: " + errorBody);
                    showError("服务器响应错误: " + response.code() + "\n" + errorBody);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                sendButton.setEnabled(true);
                Log.e(TAG, "网络请求失败", t);
                showError("网络错误: " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}