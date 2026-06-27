package es.miw.tfm.linkal.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.utils.SessionManager;

/**
 * Activity base que elimina las animaciones de transición en toda la app.
 * Todas las Activities deben extender esta clase en lugar de AppCompatActivity.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(0, 0);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
        overridePendingTransition(0, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    //  Navegación inferior

    /**
     * Configura el BottomNavigationView con el menú según el rol del usuario
     * y marca como seleccionado el item indicado.
     *
     * Uso en cada Activity: setupBottomNavigation(R.id.nav_X);
     *
     * @param selectedItemId el R.id del item activo en esta pantalla
     */
    protected void setupBottomNavigation(int selectedItemId) {
        BottomNavigationView nav = findViewById(R.id.bottomNavigation);
        if (nav == null) return;

        boolean isBusiness = "BUSINESS".equals(SessionManager.getInstance().getRole());

        nav.getMenu().clear();
        nav.inflateMenu(isBusiness ? R.menu.nav_business_menu : R.menu.nav_influencer_menu);
        nav.setSelectedItemId(selectedItemId);

        if (selectedItemId != R.id.nav_home) {
            Class<?> homeTarget = isBusiness ? ExploreInfluencersActivity.class : ExploreCampaignsActivity.class;
            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Intent intent = new Intent(BaseActivity.this, homeTarget);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedItemId) return true;


            Class<?> target = null;
            if (id == R.id.nav_home) {
                target = isBusiness ? ExploreInfluencersActivity.class : ExploreCampaignsActivity.class;
            } else if (id == R.id.nav_profile) {
                target = isBusiness ? BusinessProfileActivity.class : InfluencerProfileActivity.class;
            } else if (id == R.id.nav_matches) {
                target = MatchesActivity.class;
            } else if (id == R.id.nav_campaigns) {
                target = CampaignsActivity.class;
            } else if (id == R.id.nav_chat) {
                target = ChatListActivity.class;
            }

            if (target != null) {
                startActivity(new Intent(this, target));
                finish();
                return true;
            }
            return false;
        });
    }
}