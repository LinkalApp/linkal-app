package es.miw.tfm.linkal.activities;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;

import java.util.List;

import es.miw.tfm.linkal.R;

public class InfluencerDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "influencer_id";
    public static final String EXTRA_NAME = "influencer_name";
    public static final String EXTRA_ARTISTIC_NAME = "influencer_artistic_name";
    public static final String EXTRA_DESCRIPTION = "influencer_description";
    public static final String EXTRA_EMAIL = "influencer_email";
    public static final String EXTRA_INSTAGRAM = "influencer_instagram";
    public static final String EXTRA_TIKTOK = "influencer_tiktok";
    public static final String EXTRA_YOUTUBE = "influencer_youtube";
    public static final String EXTRA_VERIFIED = "influencer_verified";
    public static final String EXTRA_INTERESTS = "influencer_interests";

    private TextView txtInitials, txtName, txtArtisticName, txtDescription, txtEmail, txtInstagram, txtTiktok, txtYoutube;
    private ImageView imgVerifiedBadge;
    private ImageButton btnBack;
    private FlexboxLayout tagsContainer;
    private LinearLayout sectionInterests, sectionSocial, rowInstagram, rowTiktok, rowYoutube, sectionContact, rowEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_influencer_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

        initViews();

        btnBack.setOnClickListener(v -> finish());
        populateFromExtras();
    }

    private void initViews(){
        txtInitials = findViewById(R.id.txtInitials);
        txtName = findViewById(R.id.txtName);
        txtArtisticName = findViewById(R.id.txtArtisticName);
        txtDescription = findViewById(R.id.txtDescription);
        txtEmail = findViewById(R.id.txtEmail);
        txtInstagram = findViewById(R.id.txtInstagram);
        txtTiktok = findViewById(R.id.txtTiktok);
        txtYoutube = findViewById(R.id.txtYoutube);
        imgVerifiedBadge = findViewById(R.id.imgVerifiedBadge);
        tagsContainer = findViewById(R.id.tagsContainer);
        sectionInterests = findViewById(R.id.sectionInterests);
        sectionSocial = findViewById(R.id.sectionSocial);
        rowInstagram = findViewById(R.id.rowInstagram);
        rowTiktok = findViewById(R.id.rowTiktok);
        rowYoutube = findViewById(R.id.rowYoutube);
        sectionContact = findViewById(R.id.sectionContact);
        rowEmail = findViewById(R.id.rowEmail);
        btnBack = findViewById(R.id.btnBack);
    }

    private void populateFromExtras() {
        Bundle e = getIntent().getExtras();
        if (e == null) return;

        String name = e.getString(EXTRA_NAME, "");
        String artisticName = e.getString(EXTRA_ARTISTIC_NAME, "");
        String description = e.getString(EXTRA_DESCRIPTION, "");
        String email = e.getString(EXTRA_EMAIL, "");
        String instagram = e.getString(EXTRA_INSTAGRAM, "");
        String tiktok = e.getString(EXTRA_TIKTOK, "");
        String youtube = e.getString(EXTRA_YOUTUBE, "");
        boolean verified = e.getBoolean(EXTRA_VERIFIED, false);
        List<String> interests = e.getStringArrayList(EXTRA_INTERESTS);

        txtInitials.setText(getInitials(name));
        txtName.setText(name);
        imgVerifiedBadge.setVisibility(verified ? View.VISIBLE : View.GONE);

        if (!artisticName.isEmpty()) {
            txtArtisticName.setText(artisticName);
            txtArtisticName.setVisibility(View.VISIBLE);
        }

        txtDescription.setText(description.isEmpty() ? "Sin descripción" : description);

        if (interests != null && !interests.isEmpty()) {
            addInterestTags(interests);
            sectionInterests.setVisibility(View.VISIBLE);
        }

        boolean hasSocial = showRow(rowInstagram, txtInstagram, instagram)
                | showRow(rowTiktok, txtTiktok, tiktok)
                | showRow(rowYoutube, txtYoutube, youtube);
        if (hasSocial) {
            sectionSocial.setVisibility(View.VISIBLE);
        }

        boolean hasContact = showRow(rowEmail, txtEmail, email);
        if (hasContact) sectionContact.setVisibility(View.VISIBLE);
    }

    // Helpers
    private boolean showRow(LinearLayout row, TextView label, String value) {
        if (value != null && !value.isEmpty()) {
            label.setText(value);
            row.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

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
        for (int i = 0; i < interests.size(); i++) {
            int[] colors = TAG_COLORS[i % TAG_COLORS.length];
            Chip chip = new Chip(this);
            chip.setText(interests.get(i));
            chip.setTextSize(12f);
            chip.setTextColor(colors[1]);
            chip.setChipBackgroundColor(ColorStateList.valueOf(colors[0]));
            chip.setChipStrokeWidth(0f);
            chip.setEnsureMinTouchTargetSize(false);
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

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        String[] words = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(words.length, 2); i++) {
            if (!words[i].isEmpty()) sb.append(Character.toUpperCase(words[i].charAt(0)));
        }
        return sb.toString();
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}