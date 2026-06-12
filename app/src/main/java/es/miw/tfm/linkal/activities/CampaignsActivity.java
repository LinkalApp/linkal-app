package es.miw.tfm.linkal.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.adapters.CampaignAdapter;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.CampaignViewModel;

public class CampaignsActivity extends AppCompatActivity {

    private MaterialButton btnCreateCampaign;
    private TextView txtError;
    private BottomNavigationView bottomNavigation;
    private RecyclerView recyclerCampaigns;

    private CampaignAdapter adapter;
    private CampaignViewModel campaignViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_campaigns);
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
        setupNavigation();
        setupRecycler();

        campaignViewModel = new ViewModelProvider(this).get(CampaignViewModel.class);
        observeViewModel();

        btnCreateCampaign.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateCampaignActivity.class));
        });
    }

    private void initViews() {
        btnCreateCampaign = findViewById(R.id.btnCreateCampaign);
        txtError = findViewById(R.id.txtError);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        recyclerCampaigns = findViewById(R.id.recyclerCampaigns);
    }

    private void setupRecycler() {
        adapter = new CampaignAdapter(new ArrayList<>());
        recyclerCampaigns.setLayoutManager(new LinearLayoutManager(this));
        recyclerCampaigns.setAdapter(adapter);
    }

    private void setupNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_campaigns);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_campaigns) {
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, BusinessProfileActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(this, ExploreInfluencersActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_matches) {
                startActivity(new Intent(this, MatchesActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_chat) {
                android.widget.Toast.makeText(this, "Chat (próximamente)", android.widget.Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void observeViewModel() {
        campaignViewModel.getCampaigns().observe(this, campaigns -> {
            if (campaigns != null) {
                adapter.updateData(campaigns);
            }
        });

        campaignViewModel.getError().observe(this, error -> {
            if (error != null) {
                txtError.setText(error);
                txtError.setVisibility(android.view.View.VISIBLE);
            } else {
                txtError.setVisibility(android.view.View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar al volver de CreateCampaignActivity
        String businessId = SessionManager.getInstance().getUserId();
        if (businessId != null) {
            campaignViewModel.loadByBusiness(SessionManager.getInstance().getBearerToken(), businessId);
        }
    }
}