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

public class CampaignsActivity extends BaseActivity {

    private MaterialButton btnCreateCampaign;
    private TextView txtError;
    private BottomNavigationView bottomNavigation;
    private RecyclerView recyclerCampaigns;

    private CampaignAdapter adapter;
    private CampaignViewModel campaignViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaigns);

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
        setupBottomNavigation(R.id.nav_campaigns);
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