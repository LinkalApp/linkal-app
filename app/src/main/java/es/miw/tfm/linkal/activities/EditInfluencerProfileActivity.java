package es.miw.tfm.linkal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.models.requests.UpdateInfluencerRequest;
import es.miw.tfm.linkal.models.responses.InfluencerProfileResponse;
import es.miw.tfm.linkal.utils.AppConstants;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.InfluencerViewModel;

public class EditInfluencerProfileActivity extends AppCompatActivity {

    EditText edtName, edtArtisticName, edtPhone, edtDescription, edtInstagram, edtTiktok, edtYoutube;
    TextView txtError;
    FlexboxLayout interestsContainer;
    ImageView btnBack;
    Button btnSave;

    private InfluencerViewModel influencerViewModel;

    private List<String> currentInterests = new ArrayList<>();
    /**
     * true = el próximo valor de profile viene de guardar (→ finish);
     * false = viene de la carga inicial (→ rellenar campos).
     */
    private boolean isSaving = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_influencer_profile);
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

        btnBack.setOnClickListener(v -> onBackPressed());
        btnSave.setOnClickListener(v -> saveProfile());

        setupInterestChips();

        influencerViewModel = new ViewModelProvider(this).get(InfluencerViewModel.class);
        observeViewModel();

        influencerViewModel.loadProfile(SessionManager.getInstance().getBearerToken());
    }

    private void initViews(){
        edtName = findViewById(R.id.edtName);
        edtArtisticName = findViewById(R.id.edtArtisticName);
        edtPhone = findViewById(R.id.edtPhone);
        edtDescription = findViewById(R.id.edtDescription);
        edtInstagram = findViewById(R.id.edtInstagram);
        edtTiktok = findViewById(R.id.edtTiktok);
        edtYoutube = findViewById(R.id.edtYoutube);
        txtError = findViewById(R.id.txtError);
        interestsContainer = findViewById(R.id.interestsContainer);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupInterestChips() {
        for (String interest : AppConstants.INTERESTS) {
            Chip chip = new Chip(this);
            chip.setText(interest);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(true);
            chip.setChipBackgroundColorResource(R.color.neutral_light);
            chip.setTextColor(getColor(R.color.neutral_dark));
            chip.setChipStrokeColorResource(R.color.neutral_medium);
            chip.setChipStrokeWidth(1f);
            chip.setLayoutParams(new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT));
            ((FlexboxLayout.LayoutParams) chip.getLayoutParams()).setMargins(0, 0, 8, 8);
            interestsContainer.addView(chip);
        }
    }

    private void updateSelectedChips(List<String> selectedInterests) {
        for (int i = 0; i < interestsContainer.getChildCount(); i++) {
            Chip chip = (Chip) interestsContainer.getChildAt(i);
            boolean selected = selectedInterests != null
                    && selectedInterests.contains(chip.getText().toString());
            chip.setChecked(selected);
        }
    }

    private List<String> getSelectedInterests() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < interestsContainer.getChildCount(); i++) {
            Chip chip = (Chip) interestsContainer.getChildAt(i);
            if (chip.isChecked()) result.add(chip.getText().toString());
        }
        return result;
    }

    private void observeViewModel() {
        influencerViewModel.getProfile().observe(this, profile -> {
            if (profile == null) return;
            if (isSaving) {
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                prefillFields(profile);
            }
        });

        influencerViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                isSaving = false;
                btnSave.setEnabled(true);
                txtError.setText(error);
                txtError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void prefillFields(InfluencerProfileResponse profile) {
        edtName.setText(orEmpty(profile.getName()));
        edtArtisticName.setText(orEmpty(profile.getArtisticName()));
        edtPhone.setText(orEmpty(profile.getPhoneNumber()));
        edtDescription.setText(orEmpty(profile.getDescription()));
        edtInstagram.setText(orEmpty(profile.getInstagram()));
        edtTiktok.setText(orEmpty(profile.getTiktok()));
        edtYoutube.setText(orEmpty(profile.getYoutube()));
        currentInterests = profile.getInterests() != null
                ? profile.getInterests() : new ArrayList<>();
        updateSelectedChips(currentInterests);
    }

    private void saveProfile() {
        txtError.setVisibility(View.GONE);

        String name         = edtName.getText().toString().trim();
        String artisticName = edtArtisticName.getText().toString().trim();
        String phone        = edtPhone.getText().toString().trim();
        String description  = edtDescription.getText().toString().trim();
        String instagram    = edtInstagram.getText().toString().trim();
        String tiktok       = edtTiktok.getText().toString().trim();
        String youtube      = edtYoutube.getText().toString().trim();
        List<String> interests = getSelectedInterests();

        if (instagram.isEmpty() && tiktok.isEmpty() && youtube.isEmpty()) {
            txtError.setText("Debes tener al menos una red social (Instagram, TikTok o YouTube)");
            txtError.setVisibility(View.VISIBLE);
            return;
        }

        btnSave.setEnabled(false);
        isSaving = true;

        UpdateInfluencerRequest request = new UpdateInfluencerRequest(
                name.isEmpty() ? null : name,
                phone.isEmpty() ? null : phone,
                description.isEmpty() ? null : description,
                artisticName.isEmpty() ? null : artisticName,
                interests.isEmpty() ? null : interests,
                instagram,
                tiktok,
                youtube
        );

        influencerViewModel.updateProfile(SessionManager.getInstance().getBearerToken(), request);
    }

    private String orEmpty(String value) {
        return value != null ? value : "";
    }
}