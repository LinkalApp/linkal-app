package es.miw.tfm.linkal.activities;

import static es.miw.tfm.linkal.activities.EditCampaignActivity.*;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
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

import java.util.List;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.adapters.CampaignAdapter;
import es.miw.tfm.linkal.models.requests.UpdateCampaignRequest;
import es.miw.tfm.linkal.models.responses.MatchResponse;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.CampaignViewModel;
import es.miw.tfm.linkal.viewModel.EvaluationViewModel;
import es.miw.tfm.linkal.viewModel.MatchViewModel;

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
    private MatchViewModel matchViewModel;
    private EvaluationViewModel evaluationViewModel;

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
        matchViewModel = new ViewModelProvider(this).get(MatchViewModel.class);
        evaluationViewModel = new ViewModelProvider(this).get(EvaluationViewModel.class);

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

        popup.getMenu().findItem(R.id.action_start_campaign)
                .setVisible("OPEN".equals(campaignStatus));
        popup.getMenu().findItem(R.id.action_finish_campaign)
                .setVisible("IN_PROGRESS".equals(campaignStatus));

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_edit_campaign)   { openEditCampaign(); return true; }
            if (id == R.id.action_delete_campaign) { confirmDelete();    return true; }
            if (id == R.id.action_start_campaign)  { startCampaign();   return true; }
            if (id == R.id.action_finish_campaign) { confirmFinish();    return true; }
            return false;
        });
        popup.show();
    }

    private void startCampaign() {
        matchViewModel.getCampaignMatches().observe(this, matches -> {
            matchViewModel.getCampaignMatches().removeObservers(this);
            if (matches == null || matches.isEmpty()) {
                Toast.makeText(this,
                        "No hay influencers con match completado en esta campaña",
                        Toast.LENGTH_LONG).show();
                return;
            }
            showInfluencerSelector(matches);
        });
        matchViewModel.loadMatchesByCampaign(
                SessionManager.getInstance().getBearerToken(), campaignId);
    }

    private void showInfluencerSelector(List<MatchResponse> matches) {
        String[] names = new String[matches.size()];
        for (int i = 0; i < matches.size(); i++) {
            String name    = matches.get(i).getInfluencerName();
            String artistic = matches.get(i).getInfluencerArtisticName();
            names[i] = orEmpty(name) +
                    (artistic != null && !artistic.isEmpty() ? " (" + artistic + ")" : "");
        }

        new AlertDialog.Builder(this)
                .setTitle("Selecciona al influencer")
                .setItems(names, (dialog, which) -> {
                    String matchId = matches.get(which).getId();
                    new AlertDialog.Builder(this)
                            .setTitle("Iniciar campaña")
                            .setMessage("Al iniciar la campaña con " + names[which] +
                                    " se eliminarán el resto de matches y chats pendientes. ¿Continuar?")
                            .setPositiveButton("Iniciar", (d, w) ->
                                    campaignViewModel.startWithInfluencer(
                                            SessionManager.getInstance().getBearerToken(),
                                            campaignId, matchId))
                            .setNegativeButton("Cancelar", null)
                            .show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void openRatingFlow() {
        matchViewModel.getCampaignMatches().observe(this, matches -> {
            matchViewModel.getCampaignMatches().removeObservers(this);
            if (matches == null || matches.isEmpty()) {
                Toast.makeText(this, "No se encontró el match de esta campaña", Toast.LENGTH_LONG).show();
                return;
            }
            showRatingDialog(matches.get(0).getId(), matches.get(0).getInfluencerName());
        });
        matchViewModel.loadMatchesByCampaign(
                SessionManager.getInstance().getBearerToken(), campaignId);
    }

    private void showRatingDialog(String matchId, String influencerName) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_rate_influencer, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        TextView txtName = dialogView.findViewById(R.id.txtInfluencerName);
        if (txtName != null) txtName.setText(orEmpty(influencerName));

        new AlertDialog.Builder(this)
                .setTitle("Valorar influencer")
                .setView(dialogView)
                .setPositiveButton("Enviar", (dialog, which) -> {
                    int score = (int) ratingBar.getRating();
                    if (score < 1) {
                        Toast.makeText(this, "Selecciona al menos 1 estrella", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    evaluationViewModel.create(
                            SessionManager.getInstance().getBearerToken(), matchId, score);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmFinish() {
        new AlertDialog.Builder(this)
                .setTitle("Finalizar campaña")
                .setMessage("¿Quieres marcar esta campaña como finalizada?")
                .setPositiveButton("Finalizar", (dialog, which) -> {
                    UpdateCampaignRequest request = new UpdateCampaignRequest(
                            campaignTitle, campaignDescription,
                            campaignRequirements, campaignReward,
                            campaignObjective, "CLOSED");
                    campaignViewModel.update(SessionManager.getInstance().getBearerToken(),
                            campaignId, request);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmDelete() {
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

        campaignViewModel.getUpdateResult().observe(this, campaign -> {
            if (campaign == null) return;
            campaignStatus = campaign.getStatus();
            refreshViews();
            if ("CLOSED".equals(campaignStatus)) {
                openRatingFlow();
            }
        });

        campaignViewModel.getStartResult().observe(this, campaign -> {
            if (campaign == null) return;
            campaignStatus = campaign.getStatus();
            refreshViews();
            Toast.makeText(this, "¡Campaña iniciada!", Toast.LENGTH_SHORT).show();
        });

        evaluationViewModel.getEvaluationResult().observe(this, eval -> {
            if (eval == null) return;
            Toast.makeText(this, "¡Valoración enviada!", Toast.LENGTH_SHORT).show();
        });

        evaluationViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });

        matchViewModel.getError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
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