package es.miw.tfm.linkal.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.models.requests.CreateCampaignRequest;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.CampaignViewModel;

public class CreateCampaignActivity extends BaseActivity {

    EditText edtTitle, edtDescription, edtObjective, edtRequirements, edtReward;
    MaterialButton btnSave;
    ImageView btnBack;
    TextView txtError;

    private CampaignViewModel campaignViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_campaign);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

        initViews();

        campaignViewModel = new ViewModelProvider(this).get(CampaignViewModel.class);
        observeViewModel();

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> submitForm());
    }

    private void initViews() {
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        edtObjective = findViewById(R.id.edtObjective);
        edtRequirements = findViewById(R.id.edtRequirements);
        edtReward = findViewById(R.id.edtReward);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        txtError = findViewById(R.id.txtError);
    }

    private void observeViewModel() {
        campaignViewModel.getCreateResult().observe(this, campaign -> {
            if (campaign != null) {
                Toast.makeText(this, "Campaña creada correctamente", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });

        campaignViewModel.getError().observe(this, error -> {
            if (error != null) {
                btnSave.setEnabled(true);
                txtError.setText(error);
                txtError.setVisibility(View.VISIBLE);
            }
        });

        campaignViewModel.getIsLoading().observe(this, loading -> {
            btnSave.setEnabled(!Boolean.TRUE.equals(loading));
        });
    }

    private void submitForm() {
        txtError.setVisibility(View.GONE);

        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String objective = edtObjective.getText().toString().trim();
        String requirements = edtRequirements.getText().toString().trim();
        String rewardStr = edtReward.getText().toString().trim();

        if(!validate(title, description, objective)) return;

        CreateCampaignRequest request = new CreateCampaignRequest(
                title,
                description,
                requirements,
                rewardStr.isEmpty() ? null : rewardStr,
                objective.isEmpty() ? null : objective
        );

        String token = SessionManager.getInstance().getBearerToken();

        btnSave.setEnabled(false);
        campaignViewModel.create(SessionManager.getInstance().getBearerToken(), request);
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
}