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
import es.miw.tfm.linkal.adapters.InfluencerAdapter;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.InfluencerViewModel;

public class ExploreInfluencersActivity extends AppCompatActivity {

    private RecyclerView recyclerInfluencers;
    private TextView txtEmpty, txtError;
    private BottomNavigationView bottomNavigation;

    private InfluencerViewModel influencerViewModel;
    private InfluencerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_explore_influencers);
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
        setupNavigation();

        influencerViewModel = new ViewModelProvider(this).get(InfluencerViewModel.class);
        observeViewModel();

        influencerViewModel.loadAll(SessionManager.getInstance().getBearerToken());
    }

    private void initViews() {
        recyclerInfluencers = findViewById(R.id.recyclerInfluencers);
        txtEmpty = findViewById(R.id.txtEmpty);
        txtError = findViewById(R.id.txtError);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupRecycler() {
        adapter = new InfluencerAdapter(new ArrayList<>());
        recyclerInfluencers.setLayoutManager(new LinearLayoutManager(this));
        recyclerInfluencers.setAdapter(adapter);
    }

    private void setupNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_home);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_campaigns) {
                startActivity(new Intent(this, CampaignsActivity.class));
                finish();
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, BusinessProfileActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_matches) {
                Toast.makeText(this, "Matches (próximamente)", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_chat) {
                android.widget.Toast.makeText(this, "Chat (próximamente)", android.widget.Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void observeViewModel() {
        influencerViewModel.getInfluencers().observe(this, influencers -> {
            if (influencers == null || influencers.isEmpty()) {
                recyclerInfluencers.setVisibility(View.GONE);
                txtEmpty.setVisibility(View.VISIBLE);
            } else {
                txtEmpty.setVisibility(View.GONE);
                recyclerInfluencers.setVisibility(View.VISIBLE);
                adapter.updateData(influencers);
            }
        });

        influencerViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                txtError.setText(error);
                txtError.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        influencerViewModel.loadAll(SessionManager.getInstance().getBearerToken());
    }
}