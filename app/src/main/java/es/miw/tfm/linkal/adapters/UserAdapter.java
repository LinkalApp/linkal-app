package es.miw.tfm.linkal.adapters;

import static es.miw.tfm.linkal.utils.AppConstants.AVATAR_COLORS;

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
import es.miw.tfm.linkal.models.responses.AdminUserResponse;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(AdminUserResponse user);
    }

    private List<AdminUserResponse> items;
    private final OnItemClickListener listener;

    public UserAdapter(List<AdminUserResponse> items, OnItemClickListener listener) {
        this.items    = items;
        this.listener = listener;
    }

    public void updateData(List<AdminUserResponse> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminUserResponse user = items.get(position);

        holder.txtInitials.setText(getInitials(user.getName()));
        applyAvatarColor(holder.txtInitials, position);

        holder.txtName.setText(orEmpty(user.getName()));

        holder.txtEmail.setText(orEmpty(user.getEmail()));

        String role = user.getRole();
        if ("INFLUENCER".equals(role)) {
            holder.txtRole.setText("Influencer");
            holder.txtRole.setBackgroundResource(R.drawable.bg_badge_influencer);
        } else if ("BUSINESS".equals(role)) {
            holder.txtRole.setText("Comercio");
            holder.txtRole.setBackgroundResource(R.drawable.bg_badge_business);
        } else {
            holder.txtRole.setText(orEmpty(role));
            holder.txtRole.setBackgroundResource(R.drawable.bg_badge_influencer);
        }

        holder.imgVerified.setVisibility(
                Boolean.TRUE.equals(user.getVerified()) ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(user);
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView txtInitials, txtName, txtEmail, txtRole;
        final ImageView imgVerified;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInitials = itemView.findViewById(R.id.txtUserInitials);
            txtName = itemView.findViewById(R.id.txtUserName);
            txtEmail = itemView.findViewById(R.id.txtUserEmail);
            txtRole = itemView.findViewById(R.id.txtUserRole);
            imgVerified = itemView.findViewById(R.id.imgUserVerified);
        }
    }


    private void applyAvatarColor(TextView avatar, int position) {
        int[] colors = AVATAR_COLORS[position % AVATAR_COLORS.length];
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        bg.setColor(colors[0]);
        avatar.setBackground(bg);
        avatar.setTextColor(colors[1]);
    }

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