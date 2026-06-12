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
            String business = m.getBusinessName();
            holder.txtCounterpart.setText(business != null && !business.isEmpty()
                    ? "Comercio: " + business : "");
        }

        String date = m.getCreatedAt();
        holder.txtDate.setText(date != null && date.length() >= 10
                ? "Enviado el " + date.substring(0, 10) : "");

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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCampaignTitle = itemView.findViewById(R.id.txtMatchCampaignTitle);
            txtCounterpart   = itemView.findViewById(R.id.txtMatchCounterpart);
            txtDate          = itemView.findViewById(R.id.txtMatchDate);
        }
    }
}
