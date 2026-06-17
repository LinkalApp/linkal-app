package es.miw.tfm.linkal.adapters;

import static es.miw.tfm.linkal.utils.AppConstants.AVATAR_COLORS;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.models.responses.ChatResponse;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    public interface OnChatClickListener {
        void onChatClick(ChatResponse chat);
    }

    private List<ChatResponse> items;
    private OnChatClickListener    listener;

    public ChatAdapter(List<ChatResponse> items) {
        this.items    = items;
        this.listener = null;
    }

    public void setOnChatClickListener(OnChatClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<ChatResponse> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatResponse chat = items.get(position);

        holder.txtAvatar.setText(getInitials(chat.getDisplayName()));
        applyAvatarColor(holder.txtAvatar, position);
        holder.txtDisplayName.setText(orEmpty(chat.getDisplayName()));
        holder.txtCampaignTitle.setText(orEmpty(chat.getCampaignTitle()));
        holder.txtLastMessage.setText(chat.getLastMessage() != null
                ? chat.getLastMessage() : "Sin mensajes");
        holder.txtTime.setText(formatTime(chat.getLastMessageAt()));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onChatClick(chat);
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
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

    private String orEmpty(String s) { return s != null ? s : ""; }

    private String formatTime(String dateTime) {
        if (dateTime == null || dateTime.length() < 16) return "";
        return dateTime.substring(11, 16);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAvatar;
        TextView txtDisplayName;
        TextView txtCampaignTitle;
        TextView txtLastMessage;
        TextView txtTime;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAvatar        = itemView.findViewById(R.id.txtChatAvatar);
            txtDisplayName   = itemView.findViewById(R.id.txtChatDisplayName);
            txtCampaignTitle = itemView.findViewById(R.id.txtChatCampaignTitle);
            txtLastMessage   = itemView.findViewById(R.id.txtChatLastMessage);
            txtTime          = itemView.findViewById(R.id.txtChatTime);
        }
    }
}
