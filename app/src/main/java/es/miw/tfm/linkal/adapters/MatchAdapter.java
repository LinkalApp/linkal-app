package es.miw.tfm.linkal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.models.responses.MatchResponse;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder>{
    public enum Role { INFLUENCER, BUSINESS }

    public interface OnMatchClickListener {
        void onMatchClick(MatchResponse match);
    }

    private List<MatchResponse> items;
    private final Role role;
    private final OnMatchClickListener listener;

    public MatchAdapter(List<MatchResponse> items, Role role, OnMatchClickListener listener) {
        this.items = items;
        this.role  = role;
        this.listener = listener;
    }

    public void updateData(List<MatchResponse> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public void markAsRated(String matchId) {
        for (int i = 0; i < items.size(); i++) {
            if (matchId.equals(items.get(i).getId())) {
                items.get(i).setAlreadyRatedBusiness(true);
                notifyItemChanged(i);
                break;
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchResponse m = items.get(position);

        holder.txtCampaignTitle.setText(orEmpty(m.getCampaignTitle()));

        if (role == Role.INFLUENCER) {
            holder.txtCampaignTitle.setText(orEmpty(m.getCampaignTitle()));
            String business = m.getBusinessName();
            holder.txtCounterpart.setText(business != null && !business.isEmpty()
                    ? "Comercio: " + business : "");
        } else {
            String name = m.getInfluencerName();
            holder.txtCampaignTitle.setText(name != null ? name : "");
            String subtitle = orEmpty(m.getCampaignTitle());
            holder.txtCounterpart.setText(subtitle);
        }

        String date = m.getCreatedAt();
        holder.txtDate.setText(date != null && date.length() >= 10
                ? "Enviado el " + date.substring(0, 10) : "");

        boolean showBadge = role == Role.INFLUENCER
                && "COMPLETED".equals(m.getStatus())
                && "CLOSED".equals(m.getCampaignStatus())
                && !Boolean.TRUE.equals(m.getAlreadyRatedBusiness());
        holder.badgeRate.setVisibility(showBadge ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onMatchClick(m);
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    private String orEmpty(String s) {
        return s != null ? s : "";
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCampaignTitle;
        TextView txtCounterpart;
        TextView txtDate;
        TextView badgeRate;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCampaignTitle = itemView.findViewById(R.id.txtMatchCampaignTitle);
            txtCounterpart = itemView.findViewById(R.id.txtMatchCounterpart);
            txtDate = itemView.findViewById(R.id.txtMatchDate);
            badgeRate = itemView.findViewById(R.id.badgeRate);
        }
    }
}
