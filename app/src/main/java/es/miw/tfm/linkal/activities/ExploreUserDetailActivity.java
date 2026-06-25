package es.miw.tfm.linkal.activities;

import android.app.AlertDialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.models.responses.AdminUserResponse;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.AdminViewModel;

public class ExploreUserDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "admin_user_id";
    public static final String EXTRA_NAME = "admin_user_name";
    public static final String EXTRA_EMAIL = "admin_user_email";
    public static final String EXTRA_PHONE = "admin_user_phone";
    public static final String EXTRA_DESCRIPTION = "admin_user_description";
    public static final String EXTRA_VERIFIED = "admin_user_verified";
    public static final String EXTRA_ROLE = "admin_user_role";

    private ImageButton btnBack;
    private TextView txtDetailInitials, txtDetailName, txtDetailRole, txtDetailEmail,
            txtDetailPhone, txtDetailDescription;
    private ImageView imgDetailVerified;
    private LinearLayout rowPhone, rowDescription, layoutBtnVerify;
    private MaterialButton btnVerify, btnDelete;

    // Campos de rol
    private View cardRoleData;
    private LinearLayout rowArtisticName, rowInterests, rowSocial, rowCategory, rowProvince,
             rowAddress, rowWebsite;
    private TextView txtDetailArtisticName, txtDetailInterests, txtDetailSocial, txtDetailCategory,
            txtDetailProvince, txtDetailAddress, txtDetailWebsite;

    private AdminViewModel adminViewModel;
    private String userId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_explore_user_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

        initViews();
        btnBack.setOnClickListener(v -> finish());

        populateBasicFromExtras();

        userId = getIntent().getStringExtra(EXTRA_ID);
        userName = getIntent().getStringExtra(EXTRA_NAME);
        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        observeViewModel();

        if (userId != null && !userId.isEmpty()) {
            adminViewModel.loadById(SessionManager.getInstance().getBearerToken(), userId);
        }

        btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        txtDetailInitials = findViewById(R.id.txtDetailInitials);
        txtDetailName = findViewById(R.id.txtDetailName);
        txtDetailRole = findViewById(R.id.txtDetailRole);
        imgDetailVerified = findViewById(R.id.imgDetailVerified);
        txtDetailEmail = findViewById(R.id.txtDetailEmail);
        rowPhone = findViewById(R.id.rowPhone);
        txtDetailPhone = findViewById(R.id.txtDetailPhone);
        rowDescription = findViewById(R.id.rowDescription);
        txtDetailDescription = findViewById(R.id.txtDetailDescription);
        cardRoleData = findViewById(R.id.cardRoleData);
        rowArtisticName = findViewById(R.id.rowArtisticName);
        txtDetailArtisticName = findViewById(R.id.txtDetailArtisticName);
        rowInterests = findViewById(R.id.rowInterests);
        txtDetailInterests = findViewById(R.id.txtDetailInterests);
        rowSocial = findViewById(R.id.rowSocial);
        txtDetailSocial = findViewById(R.id.txtDetailSocial);
        rowCategory = findViewById(R.id.rowCategory);
        txtDetailCategory = findViewById(R.id.txtDetailCategory);
        rowProvince = findViewById(R.id.rowProvince);
        txtDetailProvince = findViewById(R.id.txtDetailProvince);
        rowAddress = findViewById(R.id.rowAddress);
        txtDetailAddress = findViewById(R.id.txtDetailAddress);
        rowWebsite = findViewById(R.id.rowWebsite);
        txtDetailWebsite = findViewById(R.id.txtDetailWebsite);
        layoutBtnVerify = findViewById(R.id.layoutBtnVerify);
        btnVerify = findViewById(R.id.btnVerify);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void populateBasicFromExtras() {
        String name = getIntent().getStringExtra(EXTRA_NAME);
        String email = getIntent().getStringExtra(EXTRA_EMAIL);
        String phone = getIntent().getStringExtra(EXTRA_PHONE);
        String desc = getIntent().getStringExtra(EXTRA_DESCRIPTION);
        boolean verified = getIntent().getBooleanExtra(EXTRA_VERIFIED, false);
        String role = getIntent().getStringExtra(EXTRA_ROLE);

        txtDetailInitials.setText(getInitials(name));
        applyAvatarColor(txtDetailInitials);
        txtDetailName.setText(orEmpty(name));
        txtDetailEmail.setText(orEmpty(email));
        imgDetailVerified.setVisibility(verified ? View.VISIBLE : View.GONE);
        applyRoleBadge(role);

        if (!verified) {
            layoutBtnVerify.setVisibility(View.VISIBLE);
            btnVerify.setOnClickListener(v -> onVerifyClicked());
        }

        if (phone != null && !phone.isEmpty()) {
            txtDetailPhone.setText(phone);
            rowPhone.setVisibility(View.VISIBLE);
        }
        if (desc != null && !desc.isEmpty()) {
            txtDetailDescription.setText(desc);
            rowDescription.setVisibility(View.VISIBLE);
        }
    }

    private void onVerifyClicked() {
        if (userId == null) return;
        btnVerify.setEnabled(false);
        adminViewModel.verifyUser(SessionManager.getInstance().getBearerToken(), userId);
    }

    private void showDeleteConfirmationDialog() {
        String name = isBlank(userName) ? "este usuario" : userName;
        new AlertDialog.Builder(this)
                .setTitle("Eliminar usuario")
                .setMessage("¿Estás seguro de que quieres eliminar a " + name + "? Esta acción retirará su acceso a la plataforma de forma inmediata.")
                .setPositiveButton("Eliminar", (dialog, which) -> onDeleteConfirmed())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void onDeleteConfirmed() {
        if (userId == null) return;
        btnDelete.setEnabled(false);
        adminViewModel.deleteUser(SessionManager.getInstance().getBearerToken(), userId);
    }

    private void observeViewModel() {
        adminViewModel.getUserDetail().observe(this, user -> {
            if (user == null) return;
            populateRoleSpecific(user);
        });

        adminViewModel.getVerifyResult().observe(this, user -> {
            if (user == null) return;
            imgDetailVerified.setVisibility(View.VISIBLE);
            layoutBtnVerify.setVisibility(View.GONE);
            Toast.makeText(this, "Usuario verificado correctamente", Toast.LENGTH_SHORT).show();
        });

        adminViewModel.getDeleteResult().observe(this, success -> {
            if (!Boolean.TRUE.equals(success)) return;
            Toast.makeText(this, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });

        adminViewModel.getErrorMessage().observe(this, error -> {
            if (error == null) return;
            btnVerify.setEnabled(true);
        });
    }

    private void populateRoleSpecific(AdminUserResponse user) {
        String role = user.getRole();

        if ("INFLUENCER".equals(role)) {
            cardRoleData.setVisibility(View.VISIBLE);

            if (!isBlank(user.getArtisticName())) {
                txtDetailArtisticName.setText(user.getArtisticName());
                rowArtisticName.setVisibility(View.VISIBLE);
            }

            List<String> interests = user.getInterests();
            if (interests != null && !interests.isEmpty()) {
                txtDetailInterests.setText(String.join(", ", interests));
                rowInterests.setVisibility(View.VISIBLE);
            }

            StringBuilder social = new StringBuilder();
            if (!isBlank(user.getInstagram())) {
                social.append("Instagram: ").append(user.getInstagram()).append("\n");
            }
            if (!isBlank(user.getTiktok())) {
                social.append("TikTok: ").append(user.getTiktok()).append("\n");
            }
            if (!isBlank(user.getYoutube())) {
                social.append("YouTube: ").append(user.getYoutube());
            }
            String socialStr = social.toString().trim();
            if (!socialStr.isEmpty()) {
                txtDetailSocial.setText(socialStr);
                rowSocial.setVisibility(View.VISIBLE);
            }

        } else if ("BUSINESS".equals(role)) {
            cardRoleData.setVisibility(View.VISIBLE);

            if (!isBlank(user.getCategory())) {
                txtDetailCategory.setText(user.getCategory());
                rowCategory.setVisibility(View.VISIBLE);
            }
            if (!isBlank(user.getProvince())) {
                txtDetailProvince.setText(user.getProvince());
                rowProvince.setVisibility(View.VISIBLE);
            }
            if (!isBlank(user.getAddress())) {
                txtDetailAddress.setText(user.getAddress());
                rowAddress.setVisibility(View.VISIBLE);
            }
            if (!isBlank(user.getWebsite())) {
                txtDetailWebsite.setText(user.getWebsite());
                rowWebsite.setVisibility(View.VISIBLE);
            }
        }
    }

    private void applyRoleBadge(String role) {
        if ("INFLUENCER".equals(role)) {
            txtDetailRole.setText("Influencer");
            txtDetailRole.setBackgroundResource(R.drawable.bg_badge_influencer);
        } else if ("BUSINESS".equals(role)) {
            txtDetailRole.setText("Comercio");
            txtDetailRole.setBackgroundResource(R.drawable.bg_badge_business);
        } else {
            txtDetailRole.setVisibility(View.GONE);
        }
    }

    private void applyAvatarColor(TextView avatar) {
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        bg.setColor(0xFFEDE9FF);
        avatar.setBackground(bg);
        avatar.setTextColor(0xFF7B6CF6);
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

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
    private String orEmpty(String s)   { return s != null ? s : ""; }
}