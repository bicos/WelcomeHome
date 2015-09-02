package com.rhpark.welcomehome.holder;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rhpark.welcomehome.MainActivity;
import com.rhpark.welcomehome.R;
import com.rhpark.welcomehome.SelectLocationActivity;
import com.rhpark.welcomehome.adapter.MainListAdapter;
import com.rhpark.welcomehome.data.UserContent;
import com.rhpark.welcomehome.data.UserHomeMap;

/**
 * Created by rhpark on 2015. 9. 2..
 */
public class HomeMapHolder<T extends UserContent>
        extends RecyclerView.ViewHolder
        implements MainListAdapter.ViewHolderImpl<T> {

    ImageView ivMapImg;
    TextView tvDesc;

    public HomeMapHolder(View itemView) {
        super(itemView);

        ivMapImg = (ImageView) itemView.findViewById(R.id.home_map_img);
        tvDesc = (TextView) itemView.findViewById(R.id.home_map_text);
    }

    @Override
    public void bindView(T obj) {
        if (obj instanceof UserHomeMap) {
            final UserHomeMap homeMap = (UserHomeMap) obj;
            ivMapImg.setImageBitmap(BitmapFactory.decodeFile(homeMap.getHomeImgPath()));
            tvDesc.setText(homeMap.getHomeImgDesc());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("알림")
                            .setMessage("집의 위치를 재설정 하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SelectLocationActivity.startSelectLocationActivity(
                                            (Activity) v.getContext(),
                                            homeMap.getHomeLocation(),
                                            MainActivity.REQ_CODE_GET_HOME_LOCATION);
                                }
                            })
                            .setNegativeButton("취소", null)
                            .show();
                }
            });
        }

    }
}