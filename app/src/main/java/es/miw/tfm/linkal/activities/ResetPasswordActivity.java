package es.miw.tfm.linkal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.viewModel.UserViewModel;

public class ResetPasswordActivity extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "extra_email";

    private EditText edtCode, edtNewPassword, edtConfirmPassword;
    private Button btnReset;
    private TextView txtError;

    private UserViewModel userViewModel;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

        email = getIntent().getStringExtra(EXTRA_EMAIL);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        this.initViews();
        this.observeViewModel();

        btnReset.setOnClickListener(v -> attemptReset());
    }

    private void initViews(){
        edtCode            = findViewById(R.id.edtCode);
        edtNewPassword     = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnReset           = findViewById(R.id.btnReset);
        txtError           = findViewById(R.id.txtError);
    }

    private void observeViewModel() {
        userViewModel.getLoading().observe(this, isLoading ->
                btnReset.setEnabled(!Boolean.TRUE.equals(isLoading))
        );

        userViewModel.getError().observe(this, errorMsg -> {
            if (errorMsg != null) {
                txtError.setText(errorMsg);
                txtError.setVisibility(View.VISIBLE);
            }
        });

        userViewModel.getSuccess().observe(this, ok -> {
            if (Boolean.TRUE.equals(ok)) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void attemptReset() {
        txtError.setVisibility(View.GONE);

        String code        = edtCode.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString();
        String confirm     = edtConfirmPassword.getText().toString();

        if (code.isEmpty() || code.length() != 6) {
            edtCode.setError("Introduce el código de 6 dígitos");
            edtCode.requestFocus();
            return;
        }
        if (newPassword.isEmpty()) {
            edtNewPassword.setError("La contraseña es obligatoria");
            edtNewPassword.requestFocus();
            return;
        }
        if (!newPassword.equals(confirm)) {
            edtConfirmPassword.setError("Las contraseñas no coinciden");
            edtConfirmPassword.requestFocus();
            return;
        }

        userViewModel.resetPassword(email, code, newPassword);
    }
}