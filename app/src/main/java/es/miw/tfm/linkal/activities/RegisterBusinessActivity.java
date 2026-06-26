package es.miw.tfm.linkal.activities;

import static es.miw.tfm.linkal.utils.AppConstants.CATEGORIES;
import static es.miw.tfm.linkal.utils.AppConstants.PROVINCES;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import es.miw.tfm.linkal.models.requests.RegisterBusinessRequest;
import es.miw.tfm.linkal.viewModel.BusinessViewModel;

public class RegisterBusinessActivity extends AppCompatActivity {

    private EditText edtAddress, edtWebsite, edtOtherCategory;
    private Spinner spinnerProvincia, spinnerCategory;
    private TextView txtOtherCategoryLabel;
    private Button btnRegister;

    private BusinessViewModel businessViewModel;

    // Datos recibidos de RegisterActivity
    private String userName, userEmail, userPassword, userPhone, userDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_business);
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

        businessViewModel = new ViewModelProvider(this).get(BusinessViewModel.class);

        initViews();
        setupSpinners();
        observeViewModel();

        btnRegister.setOnClickListener(v -> validateAndRegister());
    }

    private void initViews() {
        edtAddress = findViewById(R.id.edtAddress);
        edtWebsite = findViewById(R.id.edtWebsite);
        edtOtherCategory = findViewById(R.id.edtOtherCategory);
        spinnerProvincia = findViewById(R.id.spinnerProvincia);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        txtOtherCategoryLabel = findViewById(R.id.txtOtherCategoryLabel);
        btnRegister = findViewById(R.id.btnRegisterBusiness);
    }

    private void setupSpinners() {
        // Spinner de provincias
        ArrayAdapter<String> provinciaAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, PROVINCES);
        provinciaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvincia.setAdapter(provinciaAdapter);

        // Spinner de categorías
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, CATEGORIES);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Mostrar/ocultar campo "Otra" según la selección
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean isOtra = CATEGORIES[position].equals("Otra");
                txtOtherCategoryLabel.setVisibility(isOtra ? View.VISIBLE : View.GONE);
                edtOtherCategory.setVisibility(isOtra ? View.VISIBLE : View.GONE);
                if (!isOtra) edtOtherCategory.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void observeViewModel() {
        businessViewModel.getIsLoading().observe(this, isLoading ->
                btnRegister.setEnabled(!isLoading)
        );

        businessViewModel.getRegisterSuccess().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(this, "¡Registro completado!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finishAffinity();
            }
        });

        businessViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                if(error.contains("409")){
                    Toast.makeText(this, "Existe una cuenta con este correo", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void validateAndRegister() {
        String address = edtAddress.getText().toString().trim();
        String website = edtWebsite.getText().toString().trim();

        if (address.isEmpty()) {
            edtAddress.setError("La dirección es obligatoria");
            edtAddress.requestFocus();
            return;
        }


        if (spinnerProvincia.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Selecciona una provincia", Toast.LENGTH_SHORT).show();
            return;
        }
        String province = PROVINCES[spinnerProvincia.getSelectedItemPosition()];

        if (website.isEmpty()) {
            edtWebsite.setError("La página web es obligatoria");
            edtWebsite.requestFocus();
            return;
        }

        if (spinnerCategory.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Selecciona una categoría", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedCategory = CATEGORIES[spinnerCategory.getSelectedItemPosition()];
        if (selectedCategory.equals("Otra")) {
            String otherCategory = edtOtherCategory.getText().toString().trim();
            if (otherCategory.isEmpty()) {
                edtOtherCategory.setError("Especifica tu categoría");
                edtOtherCategory.requestFocus();
                return;
            }
            selectedCategory = otherCategory;
        }

        // Construir el request completo (datos de user + datos de business)
        RegisterBusinessRequest request = new RegisterBusinessRequest(
                userName, userEmail, userPassword, userPhone, userDescription,
                address, province, website, selectedCategory
        );

        businessViewModel.register(request);
    }
}