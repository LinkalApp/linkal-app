package es.miw.tfm.linkal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.adapters.UserAdapter;
import es.miw.tfm.linkal.models.responses.AdminUserResponse;
import es.miw.tfm.linkal.utils.SessionManager;
import es.miw.tfm.linkal.viewModel.AdminViewModel;

public class ExploreUsersActivity extends BaseActivity {

    private static final String FILTER_ALL = "Todos";
    private static final String FILTER_INFLUENCER = "Influencer";
    private static final String FILTER_BUSINESS = "Comercio";

    private static final String FILTER_VERIFIED_ALL = "Todos";
    private static final String FILTER_VERIFIED_YES = "Verificados";
    private static final String FILTER_VERIFIED_NO  = "Sin verificar";

    private RecyclerView recyclerUsers;
    private TextView txtEmpty,  txtError;
    private AutoCompleteTextView spinnerRole, spinnerVerified;
    private AdminViewModel viewModel;
    private UserAdapter adapter;

    private String currentRole = null;
    private Boolean currentVerified = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_explore_users);
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
        setupRecycler();
        setupFilters();

        viewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        observeViewModel();

        loadUsers();
    }

    private void initViews() {
        recyclerUsers  = findViewById(R.id.recyclerUsers);
        txtEmpty = findViewById(R.id.txtEmpty);
        txtError = findViewById(R.id.txtError);
        spinnerRole = findViewById(R.id.spinnerRole);
        spinnerVerified = findViewById(R.id.spinnerVerified);
    }

    private void setupRecycler() {
        adapter = new UserAdapter(new ArrayList<>(), this::onUserClick);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerUsers.setAdapter(adapter);
    }

    private void setupFilters() {
        List<String> roles = Arrays.asList(FILTER_ALL, FILTER_INFLUENCER, FILTER_BUSINESS);
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, roles);
        spinnerRole.setAdapter(roleAdapter);
        spinnerRole.setText(FILTER_ALL, false);
        spinnerRole.setOnItemClickListener((parent, view, position, id) -> {
            String selected = roles.get(position);
            currentRole = FILTER_INFLUENCER.equals(selected) ? "INFLUENCER"
                    : FILTER_BUSINESS.equals(selected)   ? "BUSINESS"
                    : null;
            loadUsers();
        });

        List<String> verifiedOptions = Arrays.asList(FILTER_VERIFIED_ALL, FILTER_VERIFIED_YES, FILTER_VERIFIED_NO);
        ArrayAdapter<String> verifiedAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, verifiedOptions);
        spinnerVerified.setAdapter(verifiedAdapter);
        spinnerVerified.setText(FILTER_VERIFIED_ALL, false);
        spinnerVerified.setOnItemClickListener((parent, view, position, id) -> {
            String selected = verifiedOptions.get(position);
            currentVerified = FILTER_VERIFIED_YES.equals(selected) ? Boolean.TRUE
                    : FILTER_VERIFIED_NO.equals(selected)  ? Boolean.FALSE
                    : null;
            loadUsers();
        });
    }

    private void loadUsers() {
        String token = SessionManager.getInstance().getBearerToken();
        viewModel.loadAll(token, currentRole, currentVerified);
    }

    private void observeViewModel() {
        viewModel.getUsersResult().observe(this, users -> {
            if (users == null || users.isEmpty()) {
                recyclerUsers.setVisibility(View.GONE);
                txtEmpty.setVisibility(View.VISIBLE);
            } else {
                txtEmpty.setVisibility(View.GONE);
                recyclerUsers.setVisibility(View.VISIBLE);
                adapter.updateData(users);
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                txtError.setText(error);
                txtError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onUserClick(AdminUserResponse user) {
        Intent intent = new Intent(this, ExploreUserDetailActivity.class);
        intent.putExtra(ExploreUserDetailActivity.EXTRA_ID, orEmpty(user.getId()));
        intent.putExtra(ExploreUserDetailActivity.EXTRA_NAME, orEmpty(user.getName()));
        intent.putExtra(ExploreUserDetailActivity.EXTRA_EMAIL, orEmpty(user.getEmail()));
        intent.putExtra(ExploreUserDetailActivity.EXTRA_PHONE, orEmpty(user.getPhoneNumber()));
        intent.putExtra(ExploreUserDetailActivity.EXTRA_DESCRIPTION, orEmpty(user.getDescription()));
        intent.putExtra(ExploreUserDetailActivity.EXTRA_VERIFIED, Boolean.TRUE.equals(user.getVerified()));
        intent.putExtra(ExploreUserDetailActivity.EXTRA_ROLE, orEmpty(user.getRole()));
        startActivity(intent);
    }

    private String orEmpty(String s) { return s != null ? s : ""; }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }
}