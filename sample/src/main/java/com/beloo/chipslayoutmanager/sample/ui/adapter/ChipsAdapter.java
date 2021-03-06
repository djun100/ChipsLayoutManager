package com.beloo.chipslayoutmanager.sample.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.chipslayoutmanager.sample.CircleTransform;
import com.beloo.chipslayoutmanager.sample.R;
import com.beloo.chipslayoutmanager.sample.entity.ChipsEntity;
import com.beloo.chipslayoutmanager.sample.ui.OnRemoveListener;
import com.bumptech.glide.Glide;
import com.cy.draghelper.ItemTouchHelperAdapter;
import com.cy.draghelper.UtilDrag;

import java.util.Collections;
import java.util.List;

public class ChipsAdapter extends  RecyclerView.Adapter<ChipsAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<ChipsEntity> chipsEntities;
    private OnRemoveListener onRemoveListener;
    private boolean isShowingPosition;

    public ChipsAdapter(List<ChipsEntity> chipsEntities,
                        OnRemoveListener onRemoveListener,RecyclerView recyclerView) {
        this.chipsEntities = chipsEntities;
        this.onRemoveListener = onRemoveListener;
        UtilDrag.attach(this,recyclerView);
    }

    public ChipsAdapter(List<ChipsEntity> chipsEntities, OnRemoveListener onRemoveListener, boolean isShowingPosition) {
        this.chipsEntities = chipsEntities;
        this.onRemoveListener = onRemoveListener;
        this.isShowingPosition = isShowingPosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(chipsEntities.get(position));
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                UtilDrag.doTouch(ChipsAdapter.this,holder,event);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return chipsEntities.size();
    }

    @Override
    public boolean onDrag(int fromPosition, int toPosition) {
        Collections.swap(chipsEntities, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;

    }

    @Override
    public void onDrop(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDescription;
        private ImageView ivPhoto;
        private TextView tvName;
        private ImageButton ibClose;
        private TextView tvPosition;

        ViewHolder(View itemView) {
            super(itemView);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ibClose = (ImageButton) itemView.findViewById(R.id.ibClose);
            tvPosition = (TextView) itemView.findViewById(R.id.tvPosition);
        }

        void bindItem(ChipsEntity entity) {
            itemView.setTag(entity.getName());
            if (TextUtils.isEmpty(entity.getDescription())) {
                tvDescription.setVisibility(View.GONE);
            } else {
                tvDescription.setVisibility(View.VISIBLE);
                tvDescription.setText(entity.getDescription());
            }

            if (entity.getDrawableResId() != 0) {
                ivPhoto.setVisibility(View.VISIBLE);
                Glide.with(ivPhoto.getContext()).load(entity.getDrawableResId())
                        .transform(new CircleTransform(ivPhoto.getContext())).into(ivPhoto);
            } else {
                ivPhoto.setVisibility(View.GONE);
            }

            tvName.setText(entity.getName());

            if (isShowingPosition) {
                tvPosition.setText(String.valueOf(getAdapterPosition()));
            }

            ibClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRemoveListener != null && getAdapterPosition() != -1) {
                        onRemoveListener.onItemRemoved(getAdapterPosition());
                    }
                }
            });
        }
    }

}
