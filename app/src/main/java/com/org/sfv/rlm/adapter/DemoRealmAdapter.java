package com.org.sfv.rlm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.org.sfv.rlm.DemoRealmObject;
import com.org.sfv.rlm.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nguyen.quang.tung on 6/9/2016.
 */
public class DemoRealmAdapter extends RecyclerView.Adapter<DemoRealmAdapter.SimpleViewHolder> {

    public interface OnItemLongClickListener {
        void onItemLongClick(DemoRealmObject object);
    }

    private OnItemLongClickListener mOnItemLongClickListener;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<DemoRealmObject> mList;

    public DemoRealmAdapter(Context context, List<DemoRealmObject> mList) {
        this.mContext = context;
        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mOnItemLongClickListener = null;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mLayoutInflater.inflate(R.layout.item_list, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        final DemoRealmObject object = mList.get(position);

        holder.mTxtId.setText(String.valueOf(object.getId()));
        holder.mTxtTitle.setText(object.getTitle().toString());
        holder.mTxtName.setText(object.getName().toString());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null)
                    mOnItemLongClickListener.onItemLongClick(object);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setOnItemClickListener(OnItemLongClickListener onItemClickListener) {
        this.mOnItemLongClickListener = onItemClickListener;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_id) TextView mTxtId;
        @BindView(R.id.text_view_title) TextView mTxtTitle;
        @BindView(R.id.text_view_name) TextView mTxtName;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }
}
