package es.miw.tfm.linkal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.miw.tfm.linkal.R;
import es.miw.tfm.linkal.models.responses.MessageResponse;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private static final int VIEW_TYPE_DATE_HEADER = 3;

    private static final SimpleDateFormat DAY_KEY_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat DISPLAY_DATE_FORMAT =
            new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

    private List<Object>  rows;
    private final String  currentUserId;

    public MessageAdapter(List<MessageResponse> items, String currentUserId) {
        this.currentUserId = currentUserId;
        this.rows = buildRows(items);
    }

    public void updateData(List<MessageResponse> newItems) {
        this.rows = buildRows(newItems);
        notifyDataSetChanged();
    }

    private List<Object> buildRows(List<MessageResponse> items) {
        List<Object> result = new ArrayList<>();
        if (items == null) {
            return result;
        }

        String lastDayKey = null;
        for (MessageResponse msg : items) {
            String dayKey = dayKeyOf(msg.getSentAt());
            if (dayKey != null && !dayKey.equals(lastDayKey)) {
                result.add(formatDateLabel(dayKey));
                lastDayKey = dayKey;
            }
            result.add(msg);
        }
        return result;
    }

    private String dayKeyOf(String sentAt) {
        if (sentAt == null || sentAt.length() < 10) {
            return null;
        }
        return sentAt.substring(0, 10); // yyyy-MM-dd
    }

    private String formatDateLabel(String dayKey) {
        String todayKey = DAY_KEY_FORMAT.format(new Date());
        if (dayKey.equals(todayKey)) {
            return "Hoy";
        }

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        String yesterdayKey = DAY_KEY_FORMAT.format(yesterday.getTime());
        if (dayKey.equals(yesterdayKey)) {
            return "Ayer";
        }

        try {
            Date date = DAY_KEY_FORMAT.parse(dayKey);
            return DISPLAY_DATE_FORMAT.format(date);
        } catch (Exception e) {
            return dayKey;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object row = rows.get(position);
        if (row instanceof String) {
            return VIEW_TYPE_DATE_HEADER;
        }
        MessageResponse msg = (MessageResponse) row;
        return currentUserId != null && currentUserId.equals(msg.getSenderId())
                ? VIEW_TYPE_SENT
                : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_date_header, parent, false);
            return new DateHeaderViewHolder(view);
        }
        int layout = viewType == VIEW_TYPE_SENT
                ? R.layout.item_message_sent
                : R.layout.item_message_received;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object row = rows.get(position);

        if (holder instanceof DateHeaderViewHolder) {
            ((DateHeaderViewHolder) holder).txtDate.setText((String) row);
            return;
        }

        MessageResponse msg = (MessageResponse) row;
        MessageViewHolder messageHolder = (MessageViewHolder) holder;
        messageHolder.txtText.setText(msg.getText());
        if (messageHolder.txtTime != null && msg.getSentAt() != null && msg.getSentAt().length() >= 16) {
            messageHolder.txtTime.setText(msg.getSentAt().substring(11, 16)); // HH:mm
        }
    }

    @Override
    public int getItemCount() {
        return rows != null ? rows.size() : 0;
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView txtText;
        TextView txtTime;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtText = itemView.findViewById(R.id.txtMessageText);
            txtTime = itemView.findViewById(R.id.txtMessageTime);
        }
    }

    static class DateHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;

        DateHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDateHeader);
        }
    }
}
