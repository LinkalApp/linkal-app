package es.miw.tfm.linkal.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.activities.ExploreCampaignDetailActivity;
import es.miw.tfm.linkal.models.responses.CampaignResponse;

public class OpenCampaignAdapter extends RecyclerView.Adapter<OpenCampaignAdapter.ViewHolder> {

    private List<CampaignResponse> items;

    public OpenCampaignAdapter(List<CampaignResponse> items) {
        this.items = items;
    }

    public void updateData(List<CampaignResponse> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_campaign, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CampaignResponse c = items.get(position);

        holder.txtTitle.setText(orEmpty(c.getTitle()));
        holder.txtObjective.setText(orEmpty(c.getObjective()));
        holder.txtReward.setText(orEmpty(c.getReward()));
        CampaignAdapter.applyStatus(holder.txtStatus, c.getStatus());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ExploreCampaignDetailActivity.class);
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_ID,                   orEmpty(c.getId()));
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_TITLE,                orEmpty(c.getTitle()));
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_DESCRIPTION,          orEmpty(c.getDescription()));
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_OBJECTIVE,            orEmpty(c.getObjective()));
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_REQUIREMENTS,         orEmpty(c.getRequirements()));
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_REWARD,               orEmpty(c.getReward()));
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_STATUS,               orEmpty(c.getStatus()));
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_CREATION_DATE,        orEmpty(c.getCreationDate()));
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_BUSINESS_NAME,        orEmpty(c.getBusinessName()));
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_BUSINESS_CATEGORY,    orEmpty(c.getBusinessCategory()));
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_BUSINESS_DESCRIPTION, orEmpty(c.getBusinessDescription()));
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_BUSINESS_ADDRESS,     orEmpty(c.getBusinessAddress()));
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_BUSINESS_WEBSITE,     orEmpty(c.getBusinessWebsite()));
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_BUSINESS_PROVINCE,    orEmpty(c.getBusinessProvince()));
            intent.putExtra(ExploreCampaignDetailActivity.EXTRA_BUSINESS_VERIFIED,    Boolean.TRUE.equals(c.getBusinessVerified()));
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView txtTitle;
        final TextView txtStatus;
        final TextView txtObjective;
        final TextView txtReward;

        ViewHolder(@NonNull View v) {
            super(v);
            txtTitle     = v.findViewById(R.id.txtCampaignTitle);
            txtStatus    = v.findViewById(R.id.txtCampaignStatus);
            txtObjective = v.findViewById(R.id.txtCampaignObjective);
            txtReward    = v.findViewById(R.id.txtCampaignReward);
        }
    }

    private String orEmpty(String v) { return v != null ? v : ""; }
}
