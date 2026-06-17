package es.miw.tfm.linkal.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.ChatViewModel;

public class ChatActivity extends AppCompatActivity {

    public static final String EXTRA_CHAT_ID = "chat_id";
    public static final String EXTRA_CHAT_NAME = "chat_name";
    public static final String EXTRA_COUNTERPART = "counterpart";
    public static final String EXTRA_CAMPAIGN_TITLE = "campaign_title";

    private String chatId;
    private TextView txtSubtitle;
    private EditText edtMessage;
    private ImageButton btnSend;
    private Toolbar toolbar;
    private ChatViewModel chatViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

        initView();
        setupToolbar();

        chatId = getIntent().getStringExtra(EXTRA_CHAT_ID);

        String campaignTitle = getIntent().getStringExtra(EXTRA_CAMPAIGN_TITLE);
        if (txtSubtitle != null && campaignTitle != null) {
            txtSubtitle.setText(campaignTitle);
        }

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        observeViewModel();

        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        edtMessage = findViewById(R.id.edtMessage);
        txtSubtitle = findViewById(R.id.txtChatSubtitle);
        btnSend = findViewById(R.id.btnSend);
    }

    private void setupToolbar(){
        String counterpart = getIntent().getStringExtra(EXTRA_COUNTERPART);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(counterpart != null ? counterpart : "Chat");
        }
    }

    private void observeViewModel() {
        chatViewModel.getMessageSent().observe(this, sent -> {
            if (!Boolean.TRUE.equals(sent)) return;
            edtMessage.setText("");
            edtMessage.setEnabled(true);
            btnSend.setEnabled(true);
            Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
        });
        chatViewModel.getError().observe(this, msg -> {
            edtMessage.setEnabled(true);
            btnSend.setEnabled(true);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            Log.e("SEND_MSG", msg);
        });
    }

    private void sendMessage() {
        String text = edtMessage.getText().toString().trim();
        if (text.isEmpty()) return;
        setInputEnabled(false);
        chatViewModel.sendMessage(
                SessionManager.getInstance().getBearerToken(), chatId, text);
    }

    private void setInputEnabled(boolean enabled) {
        edtMessage.setEnabled(enabled);
        btnSend.setEnabled(enabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}