package es.miw.tfm.linkal.activities;

import static es.miw.tfm.linkal.utils.AppConstants.CATEGORIES;
import static es.miw.tfm.linkal.utils.AppConstants.PROVINCES;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import es.miw.tfm.linkal.adapters.OpenCampaignAdapter;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.CampaignViewModel;

public class ExploreCampaignsActivity extends AppCompatActivity {

    private RecyclerView recyclerOpenCampaigns;
    private TextView txtEmpty, txtError;
    private Spinner spinnerProvince;
    private ChipGroup chipGroupCategory;
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

        campaignViewModel = new ViewModelProvider(this).get(CampaignViewModel.class);

        initViews();
        setupRecycler();
        setupCategoryChips();
        setupProvinceSpinner();
        setupBottomNavigation();

        observeViewModel();

        campaignViewModel.loadOpenCampaigns(SessionManager.getInstance().getBearerToken());
    }

    private void initViews(){
        recyclerOpenCampaigns = findViewById(R.id.recyclerOpenCampaigns);
        txtEmpty = findViewById(R.id.txtEmpty);
        txtError = findViewById(R.id.txtError);
        spinnerProvince = findViewById(R.id.spinnerProvince);
        chipGroupCategory = findViewById(R.id.chipGroupCategory);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupRecycler() {
        adapter = new OpenCampaignAdapter(new ArrayList<>());
        recyclerOpenCampaigns.setLayoutManager(new LinearLayoutManager(this));
        recyclerOpenCampaigns.setAdapter(adapter);
    }

    private void setupCategoryChips() {
        for (String category : CATEGORIES) {
            Chip chip = new Chip(this);
            chip.setText(category);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(true);
            chipGroupCategory.addView(chip);
        }
        chipGroupCategory.removeViewAt(0);
        chipGroupCategory.setOnCheckedStateChangeListener((group, checkedIds) -> applyFilters());
    }

    private void setupProvinceSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, PROVINCES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(adapter);

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void applyFilters() {
        String category = getCheckedChipText(chipGroupCategory);
        String province = spinnerProvince.getSelectedItemPosition() == 0
                ? null
                : (String) spinnerProvince.getSelectedItem();
        campaignViewModel.loadOpenCampaignsByFilters(
                SessionManager.getInstance().getBearerToken(), category, province);
    }

    private String getCheckedChipText(ChipGroup group) {
        List<Integer> checkedIds = group.getCheckedChipIds();
        if (checkedIds.isEmpty()) return null;
        Chip chip = group.findViewById(checkedIds.get(0));
        return chip != null ? chip.getText().toString() : null;
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
                startActivity(new Intent(this, MatchesActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_chat) {
                startActivity(new Intent(this, ChatListActivity.class));
                finish();
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
        applyFilters();
    }
}