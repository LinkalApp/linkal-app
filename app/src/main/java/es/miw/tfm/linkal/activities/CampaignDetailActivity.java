package es.miw.tfm.linkal.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.miw.tfm.linkal.R;

public class CampaignDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "campaign_id";
    public static final String EXTRA_TITLE = "campaign_title";
    public static final String EXTRA_DESCRIPTION = "campaign_description";
    public static final String EXTRA_OBJECTIVE = "campaign_objective";
    public static final String EXTRA_REQUIREMENTS = "campaign_requirements";
    public static final String EXTRA_REWARD = "campaign_reward";
    public static final String EXTRA_STATUS = "campaign_status";
    public static final String EXTRA_CREATION_DATE = "campaign_creation_date";

    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_campaign_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

        initView();
        bindData();

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
    }

    private void bindData() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) return;
        setText(R.id.txtTitle, extras.getString(EXTRA_TITLE, ""));
        setText(R.id.txtStatus, extras.getString(EXTRA_STATUS, "OPEN"));
        setText(R.id.txtCreationDate, extras.getString(EXTRA_CREATION_DATE, ""));
        setText(R.id.txtDescription, extras.getString(EXTRA_DESCRIPTION, ""));
        setText(R.id.txtObjective, extras.getString(EXTRA_OBJECTIVE, ""));
        setText(R.id.txtRequirements, extras.getString(EXTRA_REQUIREMENTS, ""));
        setText(R.id.txtReward, extras.getString(EXTRA_REWARD, ""));
    }

    private void setText(int viewId, String value) {
        TextView tv = findViewById(viewId);
        if (tv != null) tv.setText(value);
    }
}