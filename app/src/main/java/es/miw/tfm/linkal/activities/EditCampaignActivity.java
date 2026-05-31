package es.miw.tfm.linkal.activities;

import static es.miw.tfm.linkal.utils.AppConstants.STATUS_CAMPAIGN_OPTIONS;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.models.requests.UpdateCampaignRequest;
import es.miw.tfm.linkal.models.responses.CampaignResponse;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.CampaignViewModel;

public class EditCampaignActivity extends AppCompatActivity {

    public static final String EXTRA_CAMPAIGN_ID = "campaign_id";
    public static final String EXTRA_CAMPAIGN_TITLE = "campaign_title";
    public static final String EXTRA_CAMPAIGN_DESCRIPTION = "campaign_description";
    public static final String EXTRA_CAMPAIGN_OBJECTIVE = "campaign_objective";
    public static final String EXTRA_CAMPAIGN_REQUIREMENTS = "campaign_requirements";
    public static final String EXTRA_CAMPAIGN_REWARD = "campaign_reward";
    public static final String EXTRA_CAMPAIGN_STATUS = "campaign_status";

    private EditText edtTitle, edtDescription, edtObjective, edtRequirements, edtReward;
    private Spinner spinnerStatus;
    private TextView txtError;
    private ImageView btnBack;
    private Button btnSave;

    private CampaignViewModel campaignViewModel;
    private String campaignId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_campaign);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

        campaignId = getIntent().getStringExtra(EXTRA_CAMPAIGN_ID);

        initViews();
        setupSpinner();
        prefillFields();

        campaignViewModel = new ViewModelProvider(this).get(CampaignViewModel.class);
        observeViewModel();

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void initViews() {
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        edtObjective = findViewById(R.id.edtObjective);
        edtRequirements = findViewById(R.id.edtRequirements);
        edtReward = findViewById(R.id.edtReward);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        txtError = findViewById(R.id.txtError);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, STATUS_CAMPAIGN_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);


    }

    private void prefillFields() {
        edtTitle.setText(orEmpty(getIntent().getStringExtra(EXTRA_CAMPAIGN_TITLE)));
        edtDescription.setText(orEmpty(getIntent().getStringExtra(EXTRA_CAMPAIGN_DESCRIPTION)));
        edtObjective.setText(orEmpty(getIntent().getStringExtra(EXTRA_CAMPAIGN_OBJECTIVE)));
        edtRequirements.setText(orEmpty(getIntent().getStringExtra(EXTRA_CAMPAIGN_REQUIREMENTS)));
        edtReward.setText(orEmpty(getIntent().getStringExtra(EXTRA_CAMPAIGN_REWARD)));

        String currentStatus = getIntent().getStringExtra(EXTRA_CAMPAIGN_STATUS);
        if (currentStatus != null) {
            for (int i = 0; i < STATUS_CAMPAIGN_OPTIONS.length; i++) {
                if (STATUS_CAMPAIGN_OPTIONS[i].equals(currentStatus)) {
                    spinnerStatus.setSelection(i);
                    break;
                }
            }
        }
    }

    private void observeViewModel() {
        campaignViewModel.getUpdateResult().observe(this, (CampaignResponse campaign) -> {
            if (campaign != null) {
                Toast.makeText(this, "Campaña actualizada", Toast.LENGTH_SHORT).show();
                Intent result = new Intent();
                result.putExtra(EXTRA_CAMPAIGN_TITLE,        campaign.getTitle());
                result.putExtra(EXTRA_CAMPAIGN_DESCRIPTION,  campaign.getDescription());
                result.putExtra(EXTRA_CAMPAIGN_OBJECTIVE,    campaign.getObjective());
                result.putExtra(EXTRA_CAMPAIGN_REQUIREMENTS, campaign.getRequirements());
                result.putExtra(EXTRA_CAMPAIGN_REWARD,       campaign.getReward());
                result.putExtra(EXTRA_CAMPAIGN_STATUS,       campaign.getStatus());
                setResult(RESULT_OK, result);
                finish();
            }
        });

        campaignViewModel.getError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                btnSave.setEnabled(true);
                txtError.setText(error);
                txtError.setVisibility(View.VISIBLE);
            }
        });

        campaignViewModel.getIsLoading().observe(this, loading -> {
            btnSave.setEnabled(!Boolean.TRUE.equals(loading));
        });
    }

    private void saveChanges() {
        txtError.setVisibility(View.GONE);

        String title        = edtTitle.getText().toString().trim();
        String description  = edtDescription.getText().toString().trim();
        String objective    = edtObjective.getText().toString().trim();
        String requirements = edtRequirements.getText().toString().trim();
        String reward       = edtReward.getText().toString().trim();
        String status       = STATUS_CAMPAIGN_OPTIONS[spinnerStatus.getSelectedItemPosition()];

        if(!validate(title, description, objective)) return;

        btnSave.setEnabled(false);

        UpdateCampaignRequest request = new UpdateCampaignRequest(
                title, description, requirements, reward, objective, status);

        campaignViewModel.update(SessionManager.getInstance().getBearerToken(), campaignId, request);
    }

    private  boolean validate(String title, String description, String objective) {
        boolean valid = true;
        if (TextUtils.isEmpty(title)) {
            edtTitle.setError("El título es obligatorio");
            edtTitle.requestFocus();
            valid = false;
        }
        if (TextUtils.isEmpty(description)) {
            edtDescription.setError("La descripción es obligatoria");
            edtDescription.requestFocus();
            valid = false;
        }
        if (TextUtils.isEmpty(objective)) {
            edtObjective.setError("El objetivo es obligatorio");
            edtObjective.requestFocus();
            valid = false;
        }
        return valid;
    }

    private String orEmpty(String value) {
        return value != null ? value : "";
    }
}