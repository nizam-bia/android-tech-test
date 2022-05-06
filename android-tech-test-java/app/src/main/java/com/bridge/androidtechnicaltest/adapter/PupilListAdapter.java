package com.bridge.androidtechnicaltest.adapter;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bridge.androidtechnicaltest.R;
import com.bridge.androidtechnicaltest.listeners.PupilOnClickListener;
import com.bridge.androidtechnicaltest.listeners.PupilOnDeleteListener;
import com.bridge.androidtechnicaltest.model.PupilUi;

public class PupilListAdapter extends ListAdapter<PupilUi, PupilListAdapter.PupilViewHolder> {

    private final PupilOnClickListener onClickListener;
    private final PupilOnDeleteListener onDeleteListener;

    public PupilListAdapter(
            PupilOnClickListener onClickListener,
            PupilOnDeleteListener onDeleteListener
    ) {
        super(new DiffCallback());
        this.onClickListener = onClickListener;
        this.onDeleteListener = onDeleteListener;
    }

    @NonNull
    @Override
    public PupilViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return new PupilViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_pupil_list, viewGroup, false
        ), onClickListener, onDeleteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PupilViewHolder pupilViewHolder, int position) {
        pupilViewHolder.bind(getItem(position));
    }

    protected static class PupilViewHolder extends RecyclerView.ViewHolder {

        private TextView pupilIdText;
        private TextView pupilNameText;

        private final PupilOnClickListener onClickListener;
        private final PupilOnDeleteListener onDeleteListener;

        private PupilViewHolder(
                @NonNull View itemView,
                PupilOnClickListener onClickListener,
                PupilOnDeleteListener onDeleteListener
        ) {
            super(itemView);
            initView(itemView);
            this.onClickListener = onClickListener;
            this.onDeleteListener = onDeleteListener;
        }

        private void initView(View itemView) {
            pupilIdText = itemView.findViewById(R.id.tvpupilID);
            pupilNameText = itemView.findViewById(R.id.tvName);
        }

        public void bind(PupilUi item) {
            pupilIdText.setText(String.valueOf(item.getPupilId()));
            pupilNameText.setText(item.getPupilName());
            ImageView iconDelete = itemView.findViewById(R.id.ivDelete);
            iconDelete.setOnClickListener(v -> {
                if (onDeleteListener != null)
                    onDeleteListener.onDelete();
            });

            itemView.setOnClickListener(v -> {
                if (onClickListener != null)
                    onClickListener.onClick(item);
            });
        }
    }

    private static class DiffCallback extends DiffUtil.ItemCallback<PupilUi> {

        @Override
        public boolean areItemsTheSame(@NonNull PupilUi oldItem, @NonNull PupilUi newItem) {
            return oldItem.getPupilId() == newItem.getPupilId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull PupilUi oldItem, @NonNull PupilUi newItem) {
            return oldItem.getPupilName().equalsIgnoreCase(newItem.getPupilName()) &&
                    oldItem.getPupilId() == newItem.getPupilId();
        }
    }
}
