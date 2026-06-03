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
import es.miw.tfm.linkal.adapters.OpenCampaignAdapter;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.CampaignViewModel;

public class ExploreCampaignsActivity extends AppCompatActivity {

    private RecyclerView recyclerOpenCampaigns;
    private TextView txtEmpty, txtError;
    private BottomNavigationView bottomNavigation;

    private CampaignViewModel campaignViewModel;
    private OpenCampaignAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_explore_campaigns);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

        if (!SessionManager.getInstance().isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        initViews();
        setupRecycler();
        setupBottomNavigation();

        campaignViewModel = new ViewModelProvider(this).get(CampaignViewModel.class);
        observeViewModel();

        campaignViewModel.loadOpenCampaigns(SessionManager.getInstance().getBearerToken());
    }

    private void initViews(){
        recyclerOpenCampaigns = findViewById(R.id.recyclerOpenCampaigns);
        txtEmpty = findViewById(R.id.txtEmpty);
        txtError = findViewById(R.id.txtError);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupRecycler() {
        adapter = new OpenCampaignAdapter(new ArrayList<>());
        recyclerOpenCampaigns.setLayoutManager(new LinearLayoutManager(this));
        recyclerOpenCampaigns.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_home);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                startActivity(new Intent(this, InfluencerProfileActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_matches) {
                // TODO: navegar a pantalla de matches
                Toast.makeText(this, "Matches (próximamente)", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_chat) {
                // TODO: navegar a pantalla de chat
                Toast.makeText(this, "Chat (próximamente)", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void observeViewModel() {
        campaignViewModel.getOpenCampaigns().observe(this, campaigns -> {
            if (campaigns == null || campaigns.isEmpty()) {
                recyclerOpenCampaigns.setVisibility(View.GONE);
                txtEmpty.setVisibility(View.VISIBLE);
            } else {
                txtEmpty.setVisibility(View.GONE);
                recyclerOpenCampaigns.setVisibility(View.VISIBLE);
                adapter.updateData(campaigns);
            }
        });

        campaignViewModel.getError().observe(this, error -> {
            if (error != null) {
                txtError.setText(error);
                txtError.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        campaignViewModel.loadOpenCampaigns(SessionManager.getInstance().getBearerToken());
    }
}