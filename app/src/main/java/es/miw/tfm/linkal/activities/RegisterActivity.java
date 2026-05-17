package es.miw.tfm.linkal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.utils.PasswordValidator;

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout selectorBusiness, selectorInfluencer, layoutAddInfoUser;
    private TextView txtNameLabel;
    private EditText edtName, edtEmail, edtPassword, edtConfirmPassword, edtPhoneNumber, edtDescription;
    private Button btnContinue;
    private String selectedRole = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

        this.initViews();

        selectorBusiness.setOnClickListener(v -> selectBusiness());
        selectorInfluencer.setOnClickListener(v -> selectInfluencer());

        btnContinue.setOnClickListener(v -> {
            if (selectedRole.equals("INFLUENCER")) {
                validateAndContinue("INFLUENCER");
            } else if (selectedRole.equals("BUSINESS")) {
                validateAndContinue("BUSINESS");
            }
        });
    }

    private void initViews(){
        selectorBusiness = findViewById(R.id.businessSelector);
        selectorInfluencer = findViewById(R.id.influencerSelector);
        layoutAddInfoUser =  findViewById(R.id.layoutAddInfoUser);
        txtNameLabel = findViewById(R.id.txtNameLabel);
        edtName = findViewById(R.id.edtName);
        edtEmail =  findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtDescription = findViewById(R.id.edtDescription);
        btnContinue = findViewById(R.id.btnContinue);
    }

    private void selectBusiness() {
        if (selectedRole.equals("INFLUENCER")) resetFields(); // solo resetea si venía de otro rol
        selectedRole = "BUSINESS";
        changeColors(selectorBusiness, selectorInfluencer);
        layoutAddInfoUser.setVisibility(View.VISIBLE);
        txtNameLabel.setText(getResources().getString(R.string.titleNameBusiness));
        edtName.setHint(getResources().getString(R.string.hintNameBusiness));
    }

    private void selectInfluencer() {
        if (selectedRole.equals("BUSINESS")) resetFields(); // solo resetea si venía de otro rol
        selectedRole = "INFLUENCER";
        changeColors(selectorInfluencer, selectorBusiness);
        layoutAddInfoUser.setVisibility(View.VISIBLE);
        txtNameLabel.setText(getResources().getString(R.string.titleNameSurnameInfluencer));
        edtName.setHint(getResources().getString(R.string.hintNameInfluencer));
    }

    private void resetFields() {
        edtName.setText("");
        edtName.setError(null);
        edtEmail.setText("");
        edtEmail.setError(null);
        edtPassword.setText("");
        edtPassword.setError(null);
        edtConfirmPassword.setText("");
        edtConfirmPassword.setError(null);
        edtPhoneNumber.setText("");
        edtPhoneNumber.setError(null);
        edtDescription.setText("");
        edtDescription.setError(null);
    }

    private void changeColors(LinearLayout selectedLayout, LinearLayout unselectedLayout) {
        selectedLayout.setBackgroundResource(R.drawable.bg_selected_card);
        for (int i = 0; i < selectedLayout.getChildCount(); i++) {
            View child = selectedLayout.getChildAt(i);

            if (child instanceof TextView) {
                ((TextView) child).setTextColor(getResources().getColor(R.color.secondary, null));

            } else if (child instanceof ImageView) {
                ((ImageView) child).setColorFilter(getResources().getColor(R.color.secondary, null));
            }
        }

        unselectedLayout.setBackgroundResource(R.drawable.bg_unselected_card);
        for (int i = 0; i < unselectedLayout.getChildCount(); i++) {
            View child = unselectedLayout.getChildAt(i);

            if (child instanceof TextView) {
                ((TextView) child).setTextColor(getResources().getColor(R.color.black, null));

            } else if (child instanceof ImageView) {
                ((ImageView) child).setColorFilter(getResources().getColor(R.color.black, null));
            }
        }
    }

    /**
     * Validar campos y continuar al siguiente paso
     */
    private void validateAndContinue(String role) {
        String name = edtName.getText().toString().trim();
        if (name.isEmpty()) {
            edtName.setError("El nombre es obligatorio");
            edtName.requestFocus();
        }else if (edtEmail.getText().toString().trim().isEmpty()) {
            edtEmail.setError("El email es obligatorio");
            edtEmail.requestFocus();
        } else if (edtPassword.getText().toString().trim().isEmpty()) {
            edtPassword.setError("La contraseña es obligatoria");
            edtPassword.requestFocus();
        }else if(!PasswordValidator.isValid(edtPassword.getText().toString())) {
            String passwordError = PasswordValidator.getValidationError(edtPassword.getText().toString());
            edtPassword.setError(passwordError);
            edtPassword.requestFocus();
        } else if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
            edtConfirmPassword.setError("Las contraseñas no coinciden");
            edtConfirmPassword.requestFocus();
        } else if(edtPhoneNumber.getText().toString().trim().isEmpty()) {
            edtPhoneNumber.setError("El número de teléfono es obligatorio");
            edtPhoneNumber.requestFocus();
        } else if (edtDescription.getText().toString().trim().isEmpty()) {
            edtDescription.setError("La descripción es obligatoria");
            edtDescription.requestFocus();
        } else {
            Intent intent = null;
           if(role.equals("INFLUENCER")){
               intent = new Intent(this, RegisterInfluencerActivity.class);
           } else if(role.equals("BUSINESS")) {
               intent = new Intent(this, RegisterBusinessActivity.class);
           }

            intent.putExtra("name", edtName.getText().toString().trim());
            intent.putExtra("email", edtEmail.getText().toString().trim());
            intent.putExtra("password", edtPassword.getText().toString().trim());
            intent.putExtra("phone", edtPhoneNumber.getText().toString().trim());
            intent.putExtra("description", edtDescription.getText().toString().trim());
            intent.putExtra("role", role);

            startActivity(intent);
        }
    }
}