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
        applyStatus(holder.txtStatus, c.getStatus());

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

    /**
     * Aplica texto y color al badge de estado.
     * - Texto e icono con el color del estado
     * - Fondo con el mismo color al 20 % de opacidad (0x33 alpha)
     */
    public static void applyStatus(TextView badge, String status) {
        if (status == null) status = "OPEN";

        int colorRes;
        switch (status) {
            case "IN_PROGRESS": colorRes = R.color.status_in_progress; break;
            case "CLOSED":      colorRes = R.color.status_closed;      break;
            default:            colorRes = R.color.status_open;        break;
        }

        int color   = badge.getContext().getColor(colorRes);
        int bgColor = (color & 0x00FFFFFF) | 0x1A000000;

        badge.setText(status);
        badge.setTextColor(color);
        android.graphics.drawable.Drawable bg =
                badge.getContext().getDrawable(R.drawable.bg_badge_open).mutate();
        bg.setTint(bgColor);
        badge.setBackground(bg);
    }

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
