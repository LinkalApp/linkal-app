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
import es.miw.tfm.linkal.adapters.MatchAdapter;
import es.miw.tfm.linkal.adapters.OpenCampaignAdapter;
import es.miw.tfm.linkal.models.responses.MatchResponse;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.MatchViewModel;

public class MatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerMatches;
    private TextView txtEmpty;
    private BottomNavigationView bottomNavigation;

    private MatchAdapter adapter;
    private MatchViewModel matchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_matches);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

        initView();

        String role = SessionManager.getInstance().getRole();
        MatchAdapter.Role adapterRole = "INFLUENCER".equals(role)
                ? MatchAdapter.Role.INFLUENCER
                : MatchAdapter.Role.BUSINESS;

        setupRecycler(adapterRole);

        matchViewModel = new ViewModelProvider(this).get(MatchViewModel.class);
        observeViewModel();

        String token = SessionManager.getInstance().getBearerToken();
        matchViewModel.loadPending(token);

        setupBottomNavigation();
    }

    private void initView(){
        txtEmpty = findViewById(R.id.txtEmpty);
        recyclerMatches = findViewById(R.id.recyclerMatches);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupRecycler(MatchAdapter.Role adapterRole) {
        adapter = new MatchAdapter(new ArrayList<>(), adapterRole, this::onMatchClick);
        recyclerMatches.setLayoutManager(new LinearLayoutManager(this));
        recyclerMatches.setAdapter(adapter);
    }

    private void observeViewModel() {
        matchViewModel.getPendingMatches().observe(this, matches -> {
            if (matches == null) return;
            if (matches.isEmpty()) {
                recyclerMatches.setVisibility(View.GONE);
                txtEmpty.setVisibility(View.VISIBLE);
            } else {
                recyclerMatches.setVisibility(View.VISIBLE);
                txtEmpty.setVisibility(View.GONE);
                adapter.updateData(matches);
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_matches);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_matches) {
                return true;
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(this, ExploreCampaignsActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, InfluencerProfileActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_chat) {
                // TODO: navegar a pantalla de chat
                Toast.makeText(this, "Chat (próximamente)", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void onMatchClick(MatchResponse match) {
        Intent intent = new Intent(this, ExploreCampaignDetailActivity.class);
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_ID, orEmpty(match.getCampaignId()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_TITLE, orEmpty(match.getCampaignTitle()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_DESCRIPTION, orEmpty(match.getCampaignDescription()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_OBJECTIVE, orEmpty(match.getCampaignObjective()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_REQUIREMENTS, orEmpty(match.getCampaignRequirements()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_REWARD, orEmpty(match.getCampaignReward()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_STATUS, orEmpty(match.getCampaignStatus()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_CREATION_DATE, orEmpty(match.getCampaignCreationDate()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_BUSINESS_NAME, orEmpty(match.getBusinessName()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_BUSINESS_CATEGORY, orEmpty(match.getBusinessCategory()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_BUSINESS_DESCRIPTION, orEmpty(match.getBusinessDescription()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_BUSINESS_WEBSITE, orEmpty(match.getBusinessWebsite()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_BUSINESS_PROVINCE, orEmpty(match.getBusinessProvince()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_BUSINESS_ADDRESS, orEmpty(match.getBusinessAddress()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_BUSINESS_VERIFIED,
                Boolean.TRUE.equals(match.getBusinessVerified()));
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_MATCH_ALREADY_EXISTS, true);
        startActivity(intent);
    }

    private String orEmpty(String s) {
        return s != null ? s : "";
    }
}