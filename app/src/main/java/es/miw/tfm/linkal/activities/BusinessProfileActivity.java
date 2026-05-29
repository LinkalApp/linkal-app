package es.miw.tfm.linkal.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.models.responses.BusinessProfileResponse;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.BusinessViewModel;

public class BusinessProfileActivity extends AppCompatActivity {

    TextView txtInitials, txtName, txtCategory, txtDescription, txtAddress, txtProvince, txtWebsite, txtPhone, txtRatingValue, txtError;
    ImageView imgVerifiedBadge, btnMoreOptions;
    ImageView[] linkIcons;
    LinearLayout rowAddress, rowProvince, rowWebsite, rowPhone, rowRating;
    BottomNavigationView bottomNavigation;

    private BusinessViewModel businessViewModel;

    /** Lanzador para la pantalla de edición; recarga el perfil al volver */
    private final ActivityResultLauncher<Intent> editLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    businessViewModel.loadProfile(SessionManager.getInstance().getBearerToken());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_business_profile);
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
        setupBottomNavigation();

        btnMoreOptions.setOnClickListener(this::showOptionsMenu);

        businessViewModel = new ViewModelProvider(this).get(BusinessViewModel.class);
        observeViewModel();

        businessViewModel.loadProfile(SessionManager.getInstance().getBearerToken());
    }

    private void initViews(){
        txtInitials = findViewById(R.id.txtInitials);
        txtName = findViewById(R.id.txtName);
        txtCategory = findViewById(R.id.txtCategory);
        txtDescription = findViewById(R.id.txtDescription);
        txtAddress = findViewById(R.id.txtAddress);
        txtProvince = findViewById(R.id.txtProvince);
        txtWebsite = findViewById(R.id.txtWebsite);
        txtPhone = findViewById(R.id.txtPhone);
        txtRatingValue = findViewById(R.id.txtRatingValue);
        linkIcons        = new ImageView[]{
                findViewById(R.id.link1),
                findViewById(R.id.link2),
                findViewById(R.id.link3),
                findViewById(R.id.link4),
                findViewById(R.id.link5)
        };
        txtError = findViewById(R.id.txtError);
        imgVerifiedBadge = findViewById(R.id.imgVerifiedBadge);
        rowAddress = findViewById(R.id.rowAddress);
        rowProvince = findViewById(R.id.rowProvince);
        rowWebsite = findViewById(R.id.rowWebsite);
        rowPhone = findViewById(R.id.rowPhone);
        rowRating = findViewById(R.id.rowRating);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        btnMoreOptions = findViewById(R.id.btnMoreOptions);
    }

    private void showOptionsMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.menu_profile_options, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit_profile) {
                editLauncher.launch(new Intent(this, EditBusinessProfileActivity.class));
                return true;
            }
            return false;
        });
        popup.show();
    }

    //Navegación
    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_profile);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                return true;
            } else if (id == R.id.nav_home) {
                Toast.makeText(this, "Inicio (próximamente)", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_matches) {
                Toast.makeText(this, "Matches (próximamente)", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_chat) {
                Toast.makeText(this, "Chat (próximamente)", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    // Observadores
    private void observeViewModel() {
        businessViewModel.getProfile().observe(this, this::populateProfile);

        businessViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                txtError.setText(error);
                txtError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void populateProfile(BusinessProfileResponse profile) {
        if (profile == null) return;

        txtError.setVisibility(View.GONE);
        txtInitials.setText(getInitials(profile.getName()));
        txtName.setText(orEmpty(profile.getName()));

        if (profile.getCategory() != null && !profile.getCategory().isEmpty()) {
            txtCategory.setText(profile.getCategory());
            txtCategory.setVisibility(View.VISIBLE);
        } else {
            txtCategory.setVisibility(View.GONE);
        }

        imgVerifiedBadge.setVisibility(
                Boolean.TRUE.equals(profile.getVerified()) ? View.VISIBLE : View.GONE);

        showRating(profile.getAverageRating());

        txtDescription.setText(
                (profile.getDescription() != null && !profile.getDescription().isEmpty())
                        ? profile.getDescription()
                        : "Sin descripción");

        showInfoRow(rowAddress, txtAddress, profile.getAddress());
        showInfoRow(rowProvince,  txtProvince,  profile.getProvince());
        showInfoRow(rowWebsite,   txtWebsite,   profile.getWebsite());
        showInfoRow(rowPhone,     txtPhone,     profile.getPhoneNumber());
    }

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        String[] words = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(words.length, 2); i++) {
            if (!words[i].isEmpty()) sb.append(Character.toUpperCase(words[i].charAt(0)));
        }
        return sb.toString();
    }

    private void showInfoRow(LinearLayout row, TextView label, String value) {
        if (value != null && !value.isEmpty()) {
            label.setText(value);
            row.setVisibility(View.VISIBLE);
        } else {
            row.setVisibility(View.GONE);
        }
    }

    private String orEmpty(String value) { return value != null ? value : ""; }

    private void showRating(Double avg) {
        if (avg == null) {
            avg = 0.0;
        }

        int filled = (int) Math.round(avg); // 0-5 links encendidos
        int colorOn  = getColor(R.color.secondary);     // morado
        int colorOff = getColor(R.color.neutral_light); // gris

        for (int i = 0; i < linkIcons.length; i++) {
            linkIcons[i].setColorFilter(i < filled ? colorOn : colorOff, PorterDuff.Mode.SRC_IN);
        }

        txtRatingValue.setText(String.format(Locale.getDefault(), "%.1f / 5", avg));
        rowRating.setVisibility(View.VISIBLE);
    }

    private void showSocialRow(LinearLayout row, TextView label, String value) {
        if (value != null && !value.isEmpty()) {
            label.setText(value);
            row.setVisibility(View.VISIBLE);
        } else {
            row.setVisibility(View.GONE);
        }
    }
}