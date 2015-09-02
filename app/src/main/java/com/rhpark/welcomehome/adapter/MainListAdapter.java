package com.rhpark.welcomehome.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rhpark.welcomehome.R;
import com.rhpark.welcomehome.data.Constants;
import com.rhpark.welcomehome.data.User;
import com.rhpark.welcomehome.data.UserContent;
import com.rhpark.welcomehome.holder.HomeMapHolder;

/**
 * Created by rhpark on 2015. 9. 2..
 */
public class MainListAdapter extends RecyclerView.Adapter{

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
            default:
                Log.e(TAG, "알 수 없는 뷰 타입입니다. : viewtype("+viewType+")");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolderImpl) {
            ((ViewHolderImpl) viewHolder).bindView(user.getContent(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        UserContent content = user.getContent(position);
        return content.getType();
    }

    @Override
    public int getItemCount() {
        return user.getContents().size();
    }

    public interface ViewHolderImpl<T extends UserContent> {

        public void bindView(T userContent);
    }
}
