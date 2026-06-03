package es.miw.tfm.linkal.activities;

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

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.adapters.CampaignAdapter;

public class ExploreCampaignDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "open_campaign_id";
    public static final String EXTRA_TITLE = "open_campaign_title";
    public static final String EXTRA_DESCRIPTION = "open_campaign_description";
    public static final String EXTRA_OBJECTIVE = "open_campaign_objective";
    public static final String EXTRA_REQUIREMENTS = "open_campaign_requirements";
    public static final String EXTRA_REWARD = "open_campaign_reward";
    public static final String EXTRA_STATUS = "open_campaign_status";
    public static final String EXTRA_CREATION_DATE = "open_campaign_creation_date";
    public static final String EXTRA_BUSINESS_NAME = "open_campaign_business_name";
    public static final String EXTRA_BUSINESS_CATEGORY = "open_campaign_business_category";
    public static final String EXTRA_BUSINESS_DESCRIPTION = "open_campaign_business_description";
    public static final String EXTRA_BUSINESS_WEBSITE = "open_campaign_business_website";
    public static final String EXTRA_BUSINESS_PROVINCE = "open_campaign_business_province";
    public static final String EXTRA_BUSINESS_ADDRESS = "open_campaign_business_address";
    public static final String EXTRA_BUSINESS_VERIFIED = "open_campaign_business_verified";

    private ImageButton btnBack;
    private TextView txtTitle, txtStatus, txtCreationDate, txtObjective, txtDescription, txtRequirements, txtReward;
    private TextView txtBusinessInitials, txtBusinessName, txtBusinessCategory, txtBusinessDescription, txtBusinessWebsite, txtBusinessProvince, txtBusinessAddress;
    private LinearLayout rowProvince, rowAddress, rowWebsite;
    private ImageView imgBusinessVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_explore_campaign_detail);
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
        btnBack = findViewById(R.id.btnBack);
        txtTitle = findViewById(R.id.txtTitle);
        txtStatus = findViewById(R.id.txtStatus);
        txtCreationDate = findViewById(R.id.txtCreationDate);
        txtObjective = findViewById(R.id.txtObjective);
        txtDescription = findViewById(R.id.txtDescription);
        txtRequirements = findViewById(R.id.txtRequirements);
        txtReward = findViewById(R.id.txtReward);
        txtBusinessInitials = findViewById(R.id.txtBusinessInitials);
        txtBusinessName = findViewById(R.id.txtBusinessName);
        txtBusinessCategory = findViewById(R.id.txtBusinessCategory);
        txtBusinessDescription = findViewById(R.id.txtBusinessDescription);
        txtBusinessWebsite = findViewById(R.id.txtBusinessWebsite);
        txtBusinessProvince = findViewById(R.id.txtBusinessProvince);
        txtBusinessAddress = findViewById(R.id.txtBusinessAddress);
        imgBusinessVerified = findViewById(R.id.imgBusinessVerified);
        rowProvince = findViewById(R.id.rowBusinessProvince);
        rowAddress = findViewById(R.id.rowBusinessAddress);
        rowWebsite = findViewById(R.id.rowBusinessWebsite);
   }

   private void populateFromExtras(){
       Bundle e = getIntent().getExtras();
       if (e == null) return;

       txtTitle.setText(e.getString(EXTRA_TITLE, ""));
       CampaignAdapter.applyStatus(txtStatus, e.getString(EXTRA_STATUS, "OPEN"));
       String date = e.getString(EXTRA_CREATION_DATE, "");
       txtCreationDate.setText(date.isEmpty() ? "" : "Publicada el " + date);

       // Datos del negocio
       String businessName      = e.getString(EXTRA_BUSINESS_NAME, "");
       String businessCategory    = e.getString(EXTRA_BUSINESS_CATEGORY, "");
       String businessDescription = e.getString(EXTRA_BUSINESS_DESCRIPTION, "");
       String businessWebsite     = e.getString(EXTRA_BUSINESS_WEBSITE, "");
       String businessProvince  = e.getString(EXTRA_BUSINESS_PROVINCE, "");
       String businessAddress   = e.getString(EXTRA_BUSINESS_ADDRESS, "");
       boolean businessVerified = e.getBoolean(EXTRA_BUSINESS_VERIFIED, false);

       txtBusinessInitials.setText(getInitials(businessName));
       txtBusinessName.setText(businessName);
       imgBusinessVerified.setVisibility(businessVerified ? View.VISIBLE : View.GONE);

       if (!businessCategory.isEmpty()) {
           txtBusinessCategory.setText(businessCategory);
           txtBusinessCategory.setVisibility(View.VISIBLE);
       }
       if (!businessDescription.isEmpty()) {
           txtBusinessDescription.setText(businessDescription);
           txtBusinessDescription.setVisibility(View.VISIBLE);
       }
       if (!businessProvince.isEmpty()) {
           txtBusinessProvince.setText(businessProvince);
           rowProvince.setVisibility(View.VISIBLE);
       }
       if (!businessWebsite.isEmpty()) {
           txtBusinessWebsite.setText(businessWebsite);
           rowWebsite.setVisibility(View.VISIBLE);
       }
       if (!businessAddress.isEmpty()) {
           txtBusinessAddress.setText(businessAddress);
           rowAddress.setVisibility(View.VISIBLE);
       }

       // Detalles campaña
       txtDescription.setText(e.getString(EXTRA_DESCRIPTION, ""));
       txtObjective.setText(e.getString(EXTRA_OBJECTIVE, ""));
       txtRequirements.setText(e.getString(EXTRA_REQUIREMENTS, ""));
       txtReward.setText(e.getString(EXTRA_REWARD, ""));
   }

    /**
     * Muestra texto en txtViewId y (opcionalmente) hace visible rowViewId.
     * Si el valor está vacío o el view no existe, no hace nada.
     */
    private void showTextRow(int txtViewId, Integer rowViewId, String value) {
        if (value == null || value.isEmpty()) return;
        TextView tv = findViewById(txtViewId);
        if (tv == null) return;
        tv.setText(value);
        tv.setVisibility(View.VISIBLE);
        if (rowViewId != null) {
            View row = findViewById(rowViewId);
            if (row != null) row.setVisibility(View.VISIBLE);
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
}