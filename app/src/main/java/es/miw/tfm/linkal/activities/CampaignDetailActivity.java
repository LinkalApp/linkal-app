package es.miw.tfm.linkal.activities;

import static es.miw.tfm.linkal.activities.EditCampaignActivity.*;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.adapters.CampaignAdapter;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.CampaignViewModel;

public class CampaignDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "campaign_id";
    public static final String EXTRA_TITLE = "campaign_title";
    public static final String EXTRA_DESCRIPTION = "campaign_description";
    public static final String EXTRA_OBJECTIVE = "campaign_objective";
    public static final String EXTRA_REQUIREMENTS = "campaign_requirements";
    public static final String EXTRA_REWARD = "campaign_reward";
    public static final String EXTRA_STATUS = "campaign_status";
    public static final String EXTRA_CREATION_DATE = "campaign_creation_date";

    private TextView txtTitle, txtStatus, txtCreationDate, txtDescription, txtObjective, txtRequirements, txtReward;
    private ImageView btnMoreOptions;

    private String campaignId, campaignTitle, campaignDescription,
            campaignObjective, campaignRequirements, campaignReward, campaignStatus;

    private CampaignViewModel campaignViewModel;

    private final ActivityResultLauncher<Intent> editLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    campaignTitle = data.getStringExtra(EXTRA_CAMPAIGN_TITLE);
                    campaignDescription = data.getStringExtra(EXTRA_CAMPAIGN_DESCRIPTION);
                    campaignObjective = data.getStringExtra(EXTRA_CAMPAIGN_OBJECTIVE);
                    campaignRequirements = data.getStringExtra(EXTRA_CAMPAIGN_REQUIREMENTS);
                    campaignReward = data.getStringExtra(EXTRA_CAMPAIGN_REWARD);
                    campaignStatus = data.getStringExtra(EXTRA_CAMPAIGN_STATUS);
                    refreshViews();
                }
            });
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_campaign_detail);
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

        initView();
        loadExtras();
        refreshViews();
        observeViewModel();

        btnBack.setOnClickListener(v -> finish());
        btnMoreOptions.setOnClickListener(this::showOptionsMenu);
    }

    private void initView() {
        txtTitle = findViewById(R.id.txtTitle);
        txtStatus = findViewById(R.id.txtStatus);
        txtCreationDate = findViewById(R.id.txtCreationDate);
        txtDescription = findViewById(R.id.txtDescription);
        txtObjective = findViewById(R.id.txtObjective);
        txtRequirements = findViewById(R.id.txtRequirements);
        txtReward = findViewById(R.id.txtReward);
        btnBack = findViewById(R.id.btnBack);
        btnMoreOptions = findViewById(R.id.btnMoreOptions);
    }

    //  Menú 3 puntos
    private void showOptionsMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.menu_campaign_options, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit_campaign) {
                openEditCampaign();
                return true;
            }else if (item.getItemId() == R.id.action_delete_campaign) {
                showDeleteAccountDialog();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar campaña")
                .setMessage("¿Seguro que quieres eliminar \"" + campaignTitle + "\"? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    String token = "Bearer " + SessionManager.getInstance().getToken();
                    campaignViewModel.delete(token, campaignId);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void openEditCampaign() {
        Intent intent = new Intent(this, EditCampaignActivity.class);
        intent.putExtra(EXTRA_CAMPAIGN_ID, campaignId);
        intent.putExtra(EXTRA_CAMPAIGN_TITLE, campaignTitle);
        intent.putExtra(EXTRA_CAMPAIGN_DESCRIPTION, campaignDescription);
        intent.putExtra(EXTRA_CAMPAIGN_OBJECTIVE, campaignObjective);
        intent.putExtra(EXTRA_CAMPAIGN_REQUIREMENTS, campaignRequirements);
        intent.putExtra(EXTRA_CAMPAIGN_REWARD, campaignReward);
        intent.putExtra(EXTRA_CAMPAIGN_STATUS, campaignStatus);
        editLauncher.launch(intent);
    }

    private void loadExtras() {
        Bundle e = getIntent().getExtras();
        if (e == null) return;
        campaignId = e.getString(EXTRA_ID, "");
        campaignTitle = e.getString(EXTRA_TITLE, "");
        campaignDescription = e.getString(EXTRA_DESCRIPTION, "");
        campaignObjective = e.getString(EXTRA_OBJECTIVE, "");
        campaignRequirements = e.getString(EXTRA_REQUIREMENTS, "");
        campaignReward  = e.getString(EXTRA_REWARD, "");
        campaignStatus = e.getString(EXTRA_STATUS, "OPEN");
        setText(R.id.txtCreationDate, e.getString(EXTRA_CREATION_DATE, ""));
    }

    private void refreshViews() {
        txtTitle.setText(orEmpty(campaignTitle));
        txtStatus.setText(orEmpty(campaignStatus));
        txtDescription.setText(orEmpty(campaignDescription));
        txtObjective.setText(orEmpty(campaignObjective));
        txtRequirements.setText(orEmpty(campaignRequirements));
        txtReward.setText(orEmpty(campaignReward));
        CampaignAdapter.applyStatus(txtStatus, campaignStatus);
    }

    private void observeViewModel() {
        campaignViewModel.getDeleteResult().observe(this, deleted -> {
            if (Boolean.TRUE.equals(deleted)) {
                Toast.makeText(this, "Campaña eliminada", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });
        campaignViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setText(int viewId, String value) {
        TextView tv = findViewById(viewId);
        if (tv != null) tv.setText(value);
    }
    private String orEmpty(String v) { return v != null ? v : ""; }
}