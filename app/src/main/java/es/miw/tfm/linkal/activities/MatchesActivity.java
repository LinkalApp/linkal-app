package es.miw.tfm.linkal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.adapters.MatchAdapter;
import es.miw.tfm.linkal.adapters.OpenCampaignAdapter;
import es.miw.tfm.linkal.models.responses.MatchResponse;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.MatchViewModel;

public class MatchesActivity extends BaseActivity {

    private static final String STATUS_PENDING   = "Pendientes";
    private static final String STATUS_COMPLETED = "Confirmados";

    private RecyclerView recyclerMatches;
    private TextView txtEmpty;
    private AutoCompleteTextView spinnerStatus;
    private BottomNavigationView bottomNavigation;

    private String currentStatus = STATUS_PENDING;

    private MatchAdapter adapter;
    private MatchViewModel matchViewModel;

    private final ActivityResultLauncher<Intent> exploreLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String ratedMatchId = result.getData().getStringExtra("rated_match_id");
                    if (ratedMatchId != null) {
                        adapter.markAsRated(ratedMatchId);
                    }
                }
            });

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
        setupStatusDropdown();

        matchViewModel = new ViewModelProvider(this).get(MatchViewModel.class);
        observeViewModel();

        String token = SessionManager.getInstance().getBearerToken();
        matchViewModel.loadPending(token);

        setupBottomNavigation();

        loadCurrentStatus();
    }

    private void initView(){
        txtEmpty = findViewById(R.id.txtEmpty);
        recyclerMatches = findViewById(R.id.recyclerMatches);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupRecycler(MatchAdapter.Role adapterRole) {
        adapter = new MatchAdapter(new ArrayList<>(), adapterRole, this::onMatchClick);
        recyclerMatches.setLayoutManager(new LinearLayoutManager(this));
        recyclerMatches.setAdapter(adapter);
    }

    private void setupStatusDropdown() {
        List<String> options = List.of(STATUS_PENDING, STATUS_COMPLETED);
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, options);
        spinnerStatus.setAdapter(dropdownAdapter);
        spinnerStatus.setText(STATUS_PENDING, false);

        spinnerStatus.setOnItemClickListener((parent, view, position, id) -> {
            currentStatus = options.get(position);
            adapter.updateData(new ArrayList<>());
            loadCurrentStatus();
        });
    }

    private void loadCurrentStatus(){
        String token = SessionManager.getInstance().getBearerToken();
        if (STATUS_COMPLETED.equals(currentStatus)) {
            matchViewModel.loadCompleted(token);
        } else {
            matchViewModel.loadPending(token);
        }
    }

    private void observeViewModel() {
        matchViewModel.getMatches().observe(this, this::showMatches);
    }

    private void showMatches(List<MatchResponse> matches) {
        if (matches == null) return;
        if (matches.isEmpty()) {
            recyclerMatches.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerMatches.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.GONE);
            adapter.updateData(matches);
        }
    }

    private void setupBottomNavigation() {
        setupBottomNavigation(R.id.nav_matches);
    }

    private void onMatchClick(MatchResponse match) {
        boolean isBusiness = "BUSINESS".equals(SessionManager.getInstance().getRole());
        if (isBusiness) {
            openInfluencerDetail(match);
        } else {
            openCampaignDetail(match);
        }
    }

    private void openCampaignDetail(MatchResponse match) {
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
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_INTEREST_ALREADY_EXISTS, true);
        intent.putExtra(ExploreCampaignDetailActivity.EXTRA_MATCH_ID, orEmpty(match.getId()));
        intent.putExtra("open_already_rated",
                Boolean.TRUE.equals(match.getAlreadyRatedBusiness()));
        exploreLauncher.launch(intent);
    }

    private void openInfluencerDetail(MatchResponse match) {
        Intent intent = new Intent(this, InfluencerDetailActivity.class);
        intent.putExtra(InfluencerDetailActivity.EXTRA_ID, orEmpty(match.getInfluencerId()));
        intent.putExtra(InfluencerDetailActivity.EXTRA_NAME, orEmpty(match.getInfluencerName()));
        intent.putExtra(InfluencerDetailActivity.EXTRA_ARTISTIC_NAME, orEmpty(match.getInfluencerArtisticName()));
        intent.putExtra(InfluencerDetailActivity.EXTRA_DESCRIPTION, orEmpty(match.getInfluencerDescription()));
        intent.putExtra(InfluencerDetailActivity.EXTRA_EMAIL, orEmpty(match.getInfluencerEmail()));
        intent.putExtra(InfluencerDetailActivity.EXTRA_INSTAGRAM, orEmpty(match.getInfluencerInstagram()));
        intent.putExtra(InfluencerDetailActivity.EXTRA_TIKTOK, orEmpty(match.getInfluencerTiktok()));
        intent.putExtra(InfluencerDetailActivity.EXTRA_YOUTUBE, orEmpty(match.getInfluencerYoutube()));
        intent.putExtra(InfluencerDetailActivity.EXTRA_VERIFIED, Boolean.TRUE.equals(match.getInfluencerVerified()));
        if (match.getInfluencerInterests() != null) {
            intent.putStringArrayListExtra(InfluencerDetailActivity.EXTRA_INTERESTS,
                    new java.util.ArrayList<>(match.getInfluencerInterests()));
        }
        intent.putExtra(InfluencerDetailActivity.EXTRA_INTEREST_ALREADY_EXISTS, true);
        startActivity(intent);
    }

    private String orEmpty(String s) {
        return s != null ? s : "";
    }
}