package es.miw.tfm.linkal.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import es.miw.tfm.linkal.R;

/**
 * Conecta un campo de contraseña con la card de requisitos
 * y actualiza cada fila en tiempo real según el usuario escribe.
 */
public class PasswordRequirementsHelper {

    private PasswordRequirementsHelper() {}

    /**
     * @param passwordField campo EditText de la contraseña
     * @param infoButton    ImageButton "ⓘ" que muestra/oculta la card
     * @param cardView      la view inflada de card_password_requirements.xml
     */
    public static void attach(EditText passwordField, ImageButton infoButton, View cardView) {
        infoButton.setOnClickListener(v -> {
            boolean visible = cardView.getVisibility() == View.VISIBLE;
            cardView.setVisibility(visible ? View.GONE : View.VISIBLE);
        });
        attach(passwordField, cardView);
    }

    /**
     * @param passwordField campo EditText de la contraseña
     * @param cardView      la view inflada de card_password_requirements.xml
     */
    public static void attach(EditText passwordField, View cardView) {
        TextView reqLength = cardView.findViewById(R.id.reqLength);
        TextView reqUpper = cardView.findViewById(R.id.reqUpper);
        TextView reqLower = cardView.findViewById(R.id.reqLower);
        TextView reqDigit = cardView.findViewById(R.id.reqDigit);
        TextView reqSpecial = cardView.findViewById(R.id.reqSpecial);

        passwordField.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String pw = s.toString();
                setReq(reqLength,  pw.length() >= 8,                    "● Mínimo 8 caracteres");
                setReq(reqUpper,   PasswordValidator.hasUpperCase(pw),   "● Al menos una mayúscula (A-Z)");
                setReq(reqLower,   PasswordValidator.hasLowerCase(pw),   "● Al menos una minúscula (a-z)");
                setReq(reqDigit,   PasswordValidator.hasDigit(pw),       "● Al menos un número (0-9)");
                setReq(reqSpecial, PasswordValidator.hasSpecialChar(pw), "● Al menos un carácter especial (., -, _, @…)");
            }
        });
    }

    private static void setReq(TextView view, boolean met, String label) {
        if (met) {
            view.setText("✓ " + label.substring(2)); // reemplaza "● " por "✓ "
            view.setTextColor(0xFF2E7D32);            // verde
        } else {
            view.setText(label);
            view.setTextColor(0xFFA7ADB8);            // gris
        }
    }
}
