package es.miw.tfm.linkal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.BusinessViewModel;
import es.miw.tfm.linkal.viewModel.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    TextView txtError, txtForgotPassword;
    Button btnLogin;

    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager.init(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        this.initViews();
        this.observeViewModel();

        btnLogin.setOnClickListener(v -> validateUser());

        txtForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    public void initViews(){
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        txtError = findViewById(R.id.txtError);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void observeViewModel() {
        userViewModel.getLoading().observe(this, isLoading ->
                btnLogin.setEnabled(!Boolean.TRUE.equals(isLoading))
        );

        userViewModel.getError().observe(this, errorMsg -> {
            if (errorMsg != null) {
                txtError.setText(errorMsg);
                txtError.setVisibility(View.VISIBLE);
            }
        });

        userViewModel.getAuthResult().observe(this, authResponse -> {
            if (authResponse != null) {
                SessionManager.getInstance().saveSession(
                        authResponse.getToken(),
                        authResponse.getEmail(),
                        authResponse.getRole(),
                        authResponse.getId());
                String role = authResponse.getRole();
                Intent intent;
                if ("BUSINESS".equals(role)) {
                    intent = new Intent(this, ExploreInfluencersActivity.class);
                } else if ("INFLUENCER".equals(role)){
                    intent = new Intent(this, ExploreCampaignsActivity.class);
                } else{
                    intent = new Intent(this, ExploreUsersActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Validar usuario con la base de datos,
     * si es correcto, redirigir a la pantalla principal, sino mostrar un mensaje de error
     */
    public void validateUser(){
        txtError.setVisibility(View.GONE);

        String email    = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();

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
        if (password.isEmpty()) {
            edtPassword.setError("La contraseña es obligatoria");
            edtPassword.requestFocus();
            return;
        }

        userViewModel.login(email, password);
    }
}


