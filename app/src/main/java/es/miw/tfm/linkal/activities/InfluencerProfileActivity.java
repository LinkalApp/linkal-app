package es.miw.tfm.linkal.activities;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;
import java.util.Locale;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.models.responses.InfluencerProfileResponse;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.InfluencerViewModel;

public class InfluencerProfileActivity extends AppCompatActivity {

    TextView txtInitials, txtName, txtArtisticName, txtDescription, txtRatingValue, txtInstagram, txtTiktok, txtYoutube, txtError;
    LinearLayout rowRating, rowInstagram, rowTiktok, rowYoutube;
    FlexboxLayout tagsContainer;
    ImageView imgVerifiedBadge, btnMoreOptions;
    ImageView[] linkIcons;
    BottomNavigationView bottomNavigation;

    private InfluencerViewModel influencerViewModel;

    /** Lanzador para la pantalla de edición; recarga el perfil al volver */
    private final ActivityResultLauncher<Intent> editLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    influencerViewModel.loadProfile(SessionManager.getInstance().getBearerToken());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_influencer_profile);
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

        influencerViewModel = new ViewModelProvider(this).get(InfluencerViewModel.class);
        observeViewModel();

        influencerViewModel.loadProfile(SessionManager.getInstance().getBearerToken());
    }

    private void initViews() {
        txtInitials = findViewById(R.id.txtInitials);
        txtName = findViewById(R.id.txtName);
        txtArtisticName = findViewById(R.id.txtArtisticName);
        txtDescription = findViewById(R.id.txtDescription);
        txtInstagram = findViewById(R.id.txtInstagram);
        txtTiktok = findViewById(R.id.txtTiktok);
        txtYoutube = findViewById(R.id.txtYoutube);
        txtError = findViewById(R.id.txtError);
        tagsContainer = findViewById(R.id.tagsContainer);
        rowRating        = findViewById(R.id.rowRating);
        linkIcons        = new ImageView[]{
                findViewById(R.id.link1),
                findViewById(R.id.link2),
                findViewById(R.id.link3),
                findViewById(R.id.link4),
                findViewById(R.id.link5)
        };
        txtRatingValue   = findViewById(R.id.txtRatingValue);
        rowInstagram = findViewById(R.id.rowInstagram);
        rowTiktok = findViewById(R.id.rowTiktok);
        rowYoutube = findViewById(R.id.rowYoutube);
        imgVerifiedBadge = findViewById(R.id.imgVerifiedBadge);
        btnMoreOptions   = findViewById(R.id.btnMoreOptions);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        btnMoreOptions.setOnClickListener(this::showOptionsMenu);
    }

    // Menú 3 puntos
    private void showOptionsMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.menu_profile_options, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit_profile) {
                editLauncher.launch(new Intent(this, EditInfluencerProfileActivity.class));
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_profile);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                return true;
            } else if (id == R.id.nav_home) {
                // TODO: navegar a pantalla de inicio
                Toast.makeText(this, "Inicio (próximamente)", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_matches) {
                // TODO: navegar a pantalla de matches
                Toast.makeText(this, "Matches (próximamente)", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_chat) {
                // TODO: navegar a pantalla de chat
                Toast.makeText(this, "Chat (próximamente)", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void observeViewModel() {
        influencerViewModel.getProfile().observe(this, this::populateProfile);

        influencerViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                txtError.setText(error);
                txtError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void populateProfile(InfluencerProfileResponse profile) {
        Log.i("InfluencerProfileActivity", "Cargando perfil del influencer: " + profile);
        if (profile == null) return;

        // Avatar: iniciales del nombre real
        txtInitials.setText(getInitials(profile.getName()));

        // Nombre real
        txtName.setText(orEmpty(profile.getName()));

        // Nombre artístico (subtítulo)
        if (profile.getArtisticName() != null && !profile.getArtisticName().isEmpty()) {
            txtArtisticName.setText(profile.getArtisticName());
            txtArtisticName.setVisibility(View.VISIBLE);
        } else {
            txtArtisticName.setVisibility(View.GONE);
        }

        // Badge verificado: solo se muestra si está verificado
        imgVerifiedBadge.setVisibility(
                Boolean.TRUE.equals(profile.getVerified()) ? View.VISIBLE : View.GONE);

        // Rating promedio
        showRating(profile.getAverageRating());

        // Tags de intereses
        addInterestTags(profile.getInterests());

        // Descripción
        txtDescription.setText(
                (profile.getDescription() != null && !profile.getDescription().isEmpty())
                        ? profile.getDescription()
                        : "Sin descripción");

        // Redes sociales (solo muestra la fila si tiene valor)
        showSocialRow(rowInstagram, txtInstagram, profile.getInstagram());
        showSocialRow(rowTiktok,    txtTiktok,    profile.getTiktok());
        showSocialRow(rowYoutube,   txtYoutube,   profile.getYoutube());
    }

    // --------------------------Helpers --------------------------------------

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        String[] words = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(words.length, 2); i++) {
            if (!words[i].isEmpty()) {
                sb.append(Character.toUpperCase(words[i].charAt(0)));
            }
        }
        return sb.toString();
    }

    // Paleta de colores para los chips: [fondo, texto]
    private static final int[][] TAG_COLORS = {
            {0xFFEDE9FF, 0xFF7B6CF6},
            {0xFFD6F5EE, 0xFF1A9E7E},
            {0xFFFFEBD6, 0xFFE07A2F},
            {0xFFD6EEFF, 0xFF2878C8},
            {0xFFFFD6E7, 0xFFD63B6E},
            {0xFFF0F0F0, 0xFF555555},
    };

    private void addInterestTags(List<String> interests) {
        tagsContainer.removeAllViews();
        if (interests == null || interests.isEmpty()) return;

        for (int i = 0; i < interests.size(); i++) {
            int[] colors = TAG_COLORS[i % TAG_COLORS.length];
            int bgColor   = colors[0];
            int textColor = colors[1];

            Chip chip = new Chip(this);
            chip.setText(interests.get(i));
            chip.setTextSize(12f);
            chip.setTextColor(textColor);
            chip.setChipBackgroundColor(ColorStateList.valueOf(bgColor));
            chip.setChipStrokeWidth(0f);
            chip.setClickable(false);
            chip.setFocusable(false);

            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
            chip.setLayoutParams(lp);

            tagsContainer.addView(chip);
        }
    }

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

    private String orEmpty(String value) {
        return value != null ? value : "";
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}