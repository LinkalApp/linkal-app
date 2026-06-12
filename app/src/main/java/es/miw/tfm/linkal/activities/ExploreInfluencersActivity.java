package es.miw.tfm.linkal.activities;

import static es.miw.tfm.linkal.utils.AppConstants.INTERESTS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.adapters.InfluencerAdapter;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.InfluencerViewModel;

public class ExploreInfluencersActivity extends AppCompatActivity {

    private RecyclerView recyclerInfluencers;
    private TextView txtEmpty, txtError;
    private ChipGroup chipGroupInterests;
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
        influencerViewModel = new ViewModelProvider(this).get(InfluencerViewModel.class);

        initViews();
        setupRecycler();
        setupInterestChips();
        setupNavigation();

        observeViewModel();

        influencerViewModel.loadAll(SessionManager.getInstance().getBearerToken());
    }

    private void initViews() {
        recyclerInfluencers = findViewById(R.id.recyclerInfluencers);
        txtEmpty = findViewById(R.id.txtEmpty);
        txtError = findViewById(R.id.txtError);
        chipGroupInterests = findViewById(R.id.chipGroupInterests);
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

    private void setupInterestChips() {
        for (String interest : INTERESTS) {
            Chip chip = new Chip(this);
            chip.setText(interest);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(true);
            chipGroupInterests.addView(chip);
        }

        chipGroupInterests.setOnCheckedStateChangeListener((group, checkedIds) -> {
            List<String> selected = new ArrayList<>();
            for (int id : checkedIds) {
                Chip chip = group.findViewById(id);
                if (chip != null) selected.add(chip.getText().toString());
            }
            String selectedStr = selected.isEmpty() ? "ninguna" : String.join(", ", selected);
            Log.d("ExploreInfluencers", "Intereses seleccionados: " + selectedStr);

            influencerViewModel.loadByInterests(
                    SessionManager.getInstance().getBearerToken(), selected);
        });
    }

    private List<String> getSelectedInterests() {
        List<String> selected = new ArrayList<>();
        for (int i = 0; i < chipGroupInterests.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupInterests.getChildAt(i);
            if (chip.isChecked()) {
                selected.add(chip.getText().toString());
            }
        }
        return selected;
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
        List<String> selected = getSelectedInterests();
        influencerViewModel.loadByInterests(SessionManager.getInstance().getBearerToken(), selected);
    }
}