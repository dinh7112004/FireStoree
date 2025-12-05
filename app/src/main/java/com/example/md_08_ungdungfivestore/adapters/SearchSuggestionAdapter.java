package com.example.md_08_ungdungfivestore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.md_08_ungdungfivestore.R;

import java.util.List;

public class SearchSuggestionAdapter extends RecyclerView.Adapter<SearchSuggestionAdapter.SuggestionViewHolder> {

    private List<String> suggestions;
    private OnSuggestionClickListener listener;

    public interface OnSuggestionClickListener {
        void onSuggestionClick(String query);
    }

    public SearchSuggestionAdapter(List<String> suggestions, OnSuggestionClickListener listener) {
        this.suggestions = suggestions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_suggestion, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        String query = suggestions.get(position);
        holder.tvQuery.setText(query);

        // Xử lý click để điền vào ô tìm kiếm và thực hiện tìm kiếm
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSuggestionClick(query);
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public void updateSuggestions(List<String> newSuggestions) {
        this.suggestions = newSuggestions;
        notifyDataSetChanged();
    }

    static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuery;

        SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuery = itemView.findViewById(R.id.tv_suggestion_query);
        }
    }
}