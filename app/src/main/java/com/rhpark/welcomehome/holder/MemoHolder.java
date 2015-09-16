package com.rhpark.welcomehome.holder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rhpark.welcomehome.R;
import com.rhpark.welcomehome.adapter.MainListAdapter;
import com.rhpark.welcomehome.data.Pref;
import com.rhpark.welcomehome.data.User;
import com.rhpark.welcomehome.data.UserMemo;

/**
 * Created by rhpark on 2015. 9. 2..
 */
public class MemoHolder
        extends RecyclerView.ViewHolder
        implements MainListAdapter.ViewHolderImpl<UserMemo> {

    private EditText editText;
    private Button btnSetting;

    public MemoHolder(View itemView) {
        super(itemView);

        editText = (EditText) itemView.findViewById(R.id.et_memo);
        btnSetting = (Button) itemView.findViewById(R.id.btn_save_setting);
    }

    @Override
    public void bindView(final UserMemo memo) {
        final Context context = itemView.getContext();

        if (!TextUtils.isEmpty(memo.getMemo())) {
            editText.setText(memo.getMemo());
        }

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("알림")
                        .setMessage("메모를 저장하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                User user = Pref.getUser();
                                memo.setMemo(editText.getText().toString());
                                user.replaceContent(memo);
                                Pref.setUser(user);

                                Toast.makeText(context, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });
    }
}
