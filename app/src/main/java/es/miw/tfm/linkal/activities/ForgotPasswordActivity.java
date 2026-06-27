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

public class ForgotPasswordActivity extends BaseActivity {

    private EditText edtEmail;
    private Button btnSend;
    private TextView txtError, txtBackToLogin;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        this.initViews();
        this.observeViewModel();

        btnSend.setOnClickListener(v -> attemptSend());
        txtBackToLogin.setOnClickListener(v -> finish());
    }

    private void initViews() {
        edtEmail = findViewById(R.id.edtEmail);
        btnSend = findViewById(R.id.btnSend);
        txtError = findViewById(R.id.txtError);
        txtBackToLogin = findViewById(R.id.txtBackToLogin);
    }

    private void observeViewModel() {
        userViewModel.getLoading().observe(this, isLoading ->
                btnSend.setEnabled(!Boolean.TRUE.equals(isLoading))
        );

        userViewModel.getError().observe(this, errorMsg -> {
            if (errorMsg != null) {
                if(errorMsg.contains("404")){
                    txtError.setText("Usuario no encontrado");
                } else{
                    txtError.setText(errorMsg);
                }
                txtError.setVisibility(View.VISIBLE);
            }
        });

        userViewModel.getSuccess().observe(this, ok -> {
            if (Boolean.TRUE.equals(ok)) {
                String email = edtEmail.getText().toString().trim();
                Intent intent = new Intent(this, ResetPasswordActivity.class);
                intent.putExtra(ResetPasswordActivity.EXTRA_EMAIL, email);
                startActivity(intent);
                finish();
            }
        });
    }

    private void attemptSend() {
        txtError.setVisibility(View.GONE);

        String email = edtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            edtEmail.setError("El email es obligatorio");
            edtEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email no válido");
            edtEmail.requestFocus();
            return;
        }

        userViewModel.forgotPassword(email);
    }
}