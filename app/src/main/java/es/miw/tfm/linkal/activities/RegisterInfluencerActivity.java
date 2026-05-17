package es.miw.tfm.linkal.activities;

import static es.miw.tfm.linkal.utils.AppConstants.INTERESTS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.models.requests.RegisterInfluencerRequest;
import es.miw.tfm.linkal.viewModel.InfluencerViewModel;

public class RegisterInfluencerActivity extends AppCompatActivity {

    private EditText edtArtisticName, edtInstagram, edtTikTok, edtYoutube;
    private ChipGroup chipGroupInterests;
    private Button btnRegister;

    private InfluencerViewModel influencerViewModel;

    // Datos recibidos de RegisterActivity
    private String userName, userEmail, userPassword, userPhone, userDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_influencer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

        // Recuperar datos del usuario base desde RegisterActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName        = extras.getString("name");
            userEmail       = extras.getString("email");
            userPassword    = extras.getString("password");
            userPhone       = extras.getString("phone");
            userDescription = extras.getString("description");
        }

        influencerViewModel = new ViewModelProvider(this).get(InfluencerViewModel.class);

        this.initViews();
        setupInterestChips();
        observeViewModel();

        btnRegister.setOnClickListener(v -> validateAndRegister());
    }

    private void initViews() {
        edtArtisticName = findViewById(R.id.edtArtisticName);
        edtInstagram = findViewById(R.id.edtInstagram);
        edtTikTok = findViewById(R.id.edtTikTok);
        edtYoutube = findViewById(R.id.edtYoutube);
        chipGroupInterests = findViewById(R.id.chipGroupInterests);
        btnRegister = findViewById(R.id.btnRegisterInfluencer);
    }

    private void setupInterestChips() {
        for (String interest : INTERESTS) {
            Chip chip = new Chip(this);
            chip.setText(interest);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(true);
            chipGroupInterests.addView(chip);
        }
    }

    private void observeViewModel() {
        influencerViewModel.getRegisterSuccess().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(this, "¡Registro completado!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finishAffinity();
            }
        });

        influencerViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void validateAndRegister() {
        String artisticName = edtArtisticName.getText().toString().trim();
        String instagram = edtInstagram.getText().toString().trim();
        String tiktok = edtTikTok.getText().toString().trim();
        String youtube = edtYoutube.getText().toString().trim();

        if (artisticName.isEmpty()) {
            edtArtisticName.setError("El nombre artístico es obligatorio");
            edtArtisticName.requestFocus();
            return;
        }

        List<String> selectedInterests = getSelectedInterests();
        if (selectedInterests.isEmpty()) {
            Toast.makeText(this, "Selecciona al menos un interés", Toast.LENGTH_SHORT).show();
            return;
        }

        if (instagram.isEmpty() && tiktok.isEmpty() && youtube.isEmpty()) {
            Toast.makeText(this, "Añade al menos una red social", Toast.LENGTH_SHORT).show();
            return;
        }

        // Construir el request completo (datos de user + datos de influencer)
        RegisterInfluencerRequest request = new RegisterInfluencerRequest(
                userName, userEmail, userPassword, userPhone, userDescription,
                artisticName, selectedInterests,
                instagram.isEmpty() ? null : instagram,
                tiktok.isEmpty()    ? null : tiktok,
                youtube.isEmpty()   ? null : youtube
        );

        influencerViewModel.register(request);
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
}