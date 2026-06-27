package es.miw.tfm.linkal.activities;

import static es.miw.tfm.linkal.utils.AppConstants.PROVINCES;

import android.content.Intent;
import android.os.Bundle;
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
import es.miw.tfm.linkal.models.requests.UpdateBusinessRequest;
import es.miw.tfm.linkal.models.responses.BusinessProfileResponse;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.BusinessViewModel;
import es.miw.tfm.linkal.viewModel.InfluencerViewModel;

public class EditBusinessProfileActivity extends BaseActivity {

    EditText edtName, edtPhone, edtDescription, edtAddress, edtWebsite;
    Spinner spinnerProvince;
    TextView txtError;
    ImageView btnBack;
    Button btnSave;

    private BusinessViewModel businessViewModel;

    /**
     * true = el próximo valor de profile viene de guardar (→ finish);
     * false = viene de la carga inicial (→ rellenar campos).
     */
    private boolean isSaving = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_business_profile);
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
        setupSpinner();

        btnBack.setOnClickListener(v -> onBackPressed());
        btnSave.setOnClickListener(v -> saveProfile());

        businessViewModel = new ViewModelProvider(this).get(BusinessViewModel.class);
        observeViewModel();

        businessViewModel.loadProfile(SessionManager.getInstance().getBearerToken());
    }

    private void initViews(){
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtDescription = findViewById(R.id.edtDescription);
        edtAddress = findViewById(R.id.edtAddress);
        spinnerProvince = findViewById(R.id.edtProvince);
        edtWebsite = findViewById(R.id.edtWebsite);
        txtError = findViewById(R.id.txtError);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, PROVINCES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(adapter);
    }

    private void observeViewModel() {
        businessViewModel.getProfile().observe(this, profile -> {
            if (profile == null) return;
            if (isSaving) {
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                prefillFields(profile);
            }
        });

        businessViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                isSaving = false;
                btnSave.setEnabled(true);
                txtError.setText(error);
                txtError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void prefillFields(BusinessProfileResponse profile) {
        edtName.setText(orEmpty(profile.getName()));
        edtPhone.setText(orEmpty(profile.getPhoneNumber()));
        edtDescription.setText(orEmpty(profile.getDescription()));
        edtAddress.setText(orEmpty(profile.getAddress()));
        edtWebsite.setText(orEmpty(profile.getWebsite()));
        String currentProvince = profile.getProvince();
        if (currentProvince != null) {
            for (int i = 0; i < PROVINCES.length; i++) {
                if (PROVINCES[i].equals(currentProvince)) {
                    spinnerProvince.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveProfile() {
        txtError.setVisibility(View.GONE);

        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String website = edtWebsite.getText().toString().trim();
        boolean provinceSelected = spinnerProvince.getSelectedItemPosition() != 0;

        if (name.isEmpty() || phone.isEmpty() || description.isEmpty()
                || address.isEmpty() || !provinceSelected || website.isEmpty()) {
            txtError.setText("Todos los campos son obligatorios");
            txtError.setVisibility(View.VISIBLE);
            return;
        }

        btnSave.setEnabled(false);
        isSaving = true;

        UpdateBusinessRequest request = new UpdateBusinessRequest(
                name, phone, description, address,
                spinnerProvince.getSelectedItem().toString(), website
        );

        businessViewModel.updateProfile(SessionManager.getInstance().getBearerToken(), request);
    }

    private String orEmpty(String value) {
        return value != null ? value : "";
    }
}