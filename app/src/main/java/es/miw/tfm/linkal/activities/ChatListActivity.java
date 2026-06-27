package es.miw.tfm.linkal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.adapters.ChatAdapter;
import es.miw.tfm.linkal.models.responses.ChatResponse;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.ChatViewModel;
import es.miw.tfm.linkal.viewModel.MatchViewModel;

public class ChatListActivity extends BaseActivity {

    private RecyclerView recyclerChats;
    private TextView txtEmpty;
    private BottomNavigationView bottomNavigation;
    private ChatAdapter adapter;
    private ChatViewModel chatViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        initView();
        setupRecycler();

        String role = SessionManager.getInstance().getRole();

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        observeViewModel();

        setupBottomNavigation();
        loadChats();
    }

    private void initView(){
        recyclerChats = findViewById(R.id.recyclerChats);
        txtEmpty = findViewById(R.id.txtEmpty);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupRecycler(){
        adapter = new ChatAdapter(new ArrayList<>());
        adapter.setOnChatClickListener(this::onChatClick);
        recyclerChats.setLayoutManager(new LinearLayoutManager(this));
        recyclerChats.setAdapter(adapter);
    }

    private void observeViewModel(){
        chatViewModel.getChats().observe(this, chatList -> {
            if (chatList == null || chatList.isEmpty()) {
                recyclerChats.setVisibility(View.GONE);
                txtEmpty.setVisibility(View.VISIBLE);
            } else {
                recyclerChats.setVisibility(View.VISIBLE);
                txtEmpty.setVisibility(View.GONE);
                adapter.updateData(chatList);
            }
        });
        chatViewModel.getError().observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());

    }

    private void setupBottomNavigation() {
        setupBottomNavigation(R.id.nav_chat);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadChats();
    }

    private void loadChats() {
        chatViewModel.loadChats(SessionManager.getInstance().getBearerToken());
    }

    private void onChatClick(ChatResponse chat) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_CHAT_ID,        chat.getId());
        intent.putExtra(ChatActivity.EXTRA_COUNTERPART,    chat.getDisplayName());
        intent.putExtra(ChatActivity.EXTRA_CAMPAIGN_TITLE, chat.getCampaignTitle());
        startActivity(intent);
    }

}