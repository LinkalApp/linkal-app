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

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView recyclerChats;
    private TextView txtEmpty;
    private BottomNavigationView bottomNavigation;
    private ChatAdapter adapter;
    private ChatViewModel chatViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

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
        boolean isBusiness = "BUSINESS".equals(SessionManager.getInstance().getRole());

        bottomNavigation.getMenu().clear();
        if(isBusiness){
            bottomNavigation.inflateMenu(R.menu.nav_business_menu);
        }else{
            bottomNavigation.inflateMenu(R.menu.nav_influencer_menu);
        }

        bottomNavigation.setSelectedItemId(R.id.nav_chat);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_matches) {
                startActivity(new Intent(this, MatchesActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(this, isBusiness
                        ? ExploreInfluencersActivity.class
                        : ExploreCampaignsActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, isBusiness
                        ? BusinessProfileActivity.class
                        : InfluencerProfileActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_campaigns) {
                startActivity(new Intent(this, CampaignsActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_chat) {
                return true;
            }
            return false;
        });
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