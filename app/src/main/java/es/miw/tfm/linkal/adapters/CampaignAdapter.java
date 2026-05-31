package es.miw.tfm.linkal.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.activities.CampaignDetailActivity;
import es.miw.tfm.linkal.models.responses.CampaignResponse;

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.ViewHolder>{
    private List<CampaignResponse> items;

    public CampaignAdapter(List<CampaignResponse> items) {
        this.items = items;
    }

    public void updateData(List<CampaignResponse> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_campaign, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CampaignResponse c = items.get(position);
        holder.txtTitle.setText(c.getTitle());
        holder.txtStatus.setText(c.getStatus() != null ? c.getStatus() : "OPEN");
        holder.txtObjective.setText(c.getObjective() != null ? c.getObjective() : "");
        holder.txtReward.setText(c.getReward() != null ? c.getReward() : "");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CampaignDetailActivity.class);
            intent.putExtra(CampaignDetailActivity.EXTRA_ID,            c.getId());
            intent.putExtra(CampaignDetailActivity.EXTRA_TITLE,         c.getTitle());
            intent.putExtra(CampaignDetailActivity.EXTRA_DESCRIPTION,   c.getDescription());
            intent.putExtra(CampaignDetailActivity.EXTRA_OBJECTIVE,      c.getObjective());
            intent.putExtra(CampaignDetailActivity.EXTRA_REQUIREMENTS,  c.getRequirements());
            intent.putExtra(CampaignDetailActivity.EXTRA_REWARD,        c.getReward());
            intent.putExtra(CampaignDetailActivity.EXTRA_STATUS,        c.getStatus());
            intent.putExtra(CampaignDetailActivity.EXTRA_CREATION_DATE, c.getCreationDate());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtStatus, txtObjective, txtReward;
        ViewHolder(View v) {
            super(v);
            txtTitle     = v.findViewById(R.id.txtCampaignTitle);
            txtStatus    = v.findViewById(R.id.txtCampaignStatus);
            txtObjective = v.findViewById(R.id.txtCampaignObjective);
            txtReward    = v.findViewById(R.id.txtCampaignReward);
        }
    }
}
