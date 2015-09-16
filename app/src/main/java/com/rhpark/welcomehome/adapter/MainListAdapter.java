package com.rhpark.welcomehome.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rhpark.welcomehome.R;
import com.rhpark.welcomehome.data.Constants;
import com.rhpark.welcomehome.data.User;
import com.rhpark.welcomehome.data.UserContentImpl;
import com.rhpark.welcomehome.holder.HomeMapHolder;
import com.rhpark.welcomehome.holder.MemoHolder;
import com.rhpark.welcomehome.holder.VolumeHolder;

/**
 * Created by rhpark on 2015. 9. 2..
 */
public class MainListAdapter extends RecyclerView.Adapter {

    private static final String TAG = MainListAdapter.class.getSimpleName();

    private User user;

    public MainListAdapter(User user) {
        this.user = user;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case Constants.TYPE_HOME_MAP:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_home_map, viewGroup, false);
                return new HomeMapHolder(view);
            case Constants.TYPE_VOLUMN:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_volume, viewGroup, false);
                return new VolumeHolder(view);
            case Constants.TYPE_MEMO:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_memo, viewGroup, false);
                return new MemoHolder(view);

            default:
                Log.e(TAG, "알 수 없는 뷰 타입입니다. : viewtype(" + viewType + ")");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((ViewHolderImpl) viewHolder).bindView(user.getContent(position));
    }

    @Override
    public int getItemViewType(int position) {
        return user.getContent(position).getType();
    }

    @Override
    public int getItemCount() {
        return user.getContents().size();
    }

    public interface ViewHolderImpl<T extends UserContentImpl> {

        public void bindView(T userContent);
    }
}
