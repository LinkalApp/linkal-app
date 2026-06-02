package es.miw.tfm.linkal.adapters;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.activities.InfluencerDetailActivity;
import es.miw.tfm.linkal.models.responses.InfluencerProfileResponse;

public class InfluencerAdapter extends RecyclerView.Adapter<InfluencerAdapter.ViewHolder>{
    private List<InfluencerProfileResponse> items;

    public InfluencerAdapter(List<InfluencerProfileResponse> items) {
        this.items = items;
    }

    public void updateData(List<InfluencerProfileResponse> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_influencer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InfluencerProfileResponse influencer = items.get(position);

        holder.txtInitials.setText(getInitials(influencer.getName()));
        applyAvatarColor(holder.txtInitials, position);

        holder.txtName.setText(orEmpty(influencer.getName()));
        holder.imgVerifiedBadge.setVisibility(
                Boolean.TRUE.equals(influencer.getVerified()) ? View.VISIBLE : View.GONE);
        if (influencer.getArtisticName() != null && !influencer.getArtisticName().isEmpty()) {
            holder.txtArtisticName.setText(influencer.getArtisticName());
            holder.txtArtisticName.setVisibility(View.VISIBLE);
        } else {
            holder.txtArtisticName.setVisibility(View.GONE);
        }
        if (influencer.getInterests() != null && !influencer.getInterests().isEmpty()) {
            List<String> interests = influencer.getInterests();
            String preview = String.join(" · ", interests.subList(0, Math.min(3, interests.size())));
            if (interests.size() > 3) preview += "…";
            holder.txtInterests.setText(preview);
            holder.txtInterests.setVisibility(View.VISIBLE);
        } else {
            holder.txtInterests.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), InfluencerDetailActivity.class);
            intent.putExtra(InfluencerDetailActivity.EXTRA_ID,            orEmpty(influencer.getId()));
            intent.putExtra(InfluencerDetailActivity.EXTRA_NAME,          orEmpty(influencer.getName()));
            intent.putExtra(InfluencerDetailActivity.EXTRA_ARTISTIC_NAME, orEmpty(influencer.getArtisticName()));
            intent.putExtra(InfluencerDetailActivity.EXTRA_DESCRIPTION,   orEmpty(influencer.getDescription()));
            intent.putExtra(InfluencerDetailActivity.EXTRA_EMAIL,         orEmpty(influencer.getEmail()));
            intent.putExtra(InfluencerDetailActivity.EXTRA_INSTAGRAM,     orEmpty(influencer.getInstagram()));
            intent.putExtra(InfluencerDetailActivity.EXTRA_TIKTOK,        orEmpty(influencer.getTiktok()));
            intent.putExtra(InfluencerDetailActivity.EXTRA_YOUTUBE,       orEmpty(influencer.getYoutube()));
            intent.putExtra(InfluencerDetailActivity.EXTRA_VERIFIED,      Boolean.TRUE.equals(influencer.getVerified()));
            if (influencer.getInterests() != null) {
                intent.putStringArrayListExtra(InfluencerDetailActivity.EXTRA_INTERESTS,
                        new java.util.ArrayList<>(influencer.getInterests()));
            }
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView txtInitials;
        final TextView  txtName;
        final ImageView imgVerifiedBadge;
        final TextView  txtArtisticName;
        final TextView  txtInterests;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInitials      = itemView.findViewById(R.id.txtInfluencerInitials);
            txtName          = itemView.findViewById(R.id.txtInfluencerName);
            imgVerifiedBadge = itemView.findViewById(R.id.imgVerifiedBadge);
            txtArtisticName  = itemView.findViewById(R.id.txtInfluencerArtisticName);
            txtInterests     = itemView.findViewById(R.id.txtInfluencerInterests);
        }
    }

    // Colores de avatar ------------------------------------------------
    private static final int[][] AVATAR_COLORS = {
            {0xFFEDE9FF, 0xFF7B6CF6}, // morado
            {0xFFD6F5EE, 0xFF1A9E7E}, // verde
            {0xFFFFEBD6, 0xFFE07A2F}, // naranja
            {0xFFD6EEFF, 0xFF2878C8}, // azul
            {0xFFFFD6E7, 0xFFD63B6E}, // rosa
            {0xFFFFF3CC, 0xFFB08800}, // amarillo
    };

    private void applyAvatarColor(TextView avatar, int position) {
        int[] colors = AVATAR_COLORS[position % AVATAR_COLORS.length];
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        bg.setColor(colors[0]);
        avatar.setBackground(bg);
        avatar.setTextColor(colors[1]);
    }

    // Helpers
    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        String[] words = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(words.length, 2); i++) {
            if (!words[i].isEmpty()) sb.append(Character.toUpperCase(words[i].charAt(0)));
        }
        return sb.toString();
    }

    private String orEmpty(String v) { return v != null ? v : ""; }
}
