package com.rhpark.welcomehome.holder;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.rhpark.welcomehome.R;
import com.rhpark.welcomehome.adapter.MainListAdapter;
import com.rhpark.welcomehome.data.Pref;
import com.rhpark.welcomehome.data.User;
import com.rhpark.welcomehome.data.UserVolume;

/**
 * Created by rhpark on 2015. 9. 3..
 */
public class VolumeHolder
        extends RecyclerView.ViewHolder
        implements MainListAdapter.ViewHolderImpl<UserVolume> {

    private SeekBar indoorRingVolume;
    private SeekBar indoorMediaVolume;
    private SeekBar outdoorRingVolume;
    private SeekBar outdoorMediaVolume;

    private Button btnSaveSetting;

    public VolumeHolder(View itemView) {
        super(itemView);

        indoorRingVolume = (SeekBar) itemView.findViewById(R.id.indoor_ring_volume);
        indoorMediaVolume = (SeekBar) itemView.findViewById(R.id.indoor_media_volume);
        outdoorRingVolume = (SeekBar) itemView.findViewById(R.id.outdoor_ring_volume);
        outdoorMediaVolume = (SeekBar) itemView.findViewById(R.id.outdoor_media_volume);

        Context context = itemView.getContext();
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        int maxRingVoluem = audio.getStreamMaxVolume(AudioManager.STREAM_RING);
        int maxMediaVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        indoorRingVolume.setMax(maxRingVoluem);
        outdoorRingVolume.setMax(maxRingVoluem);
        indoorMediaVolume.setMax(maxMediaVolume);
        outdoorMediaVolume.setMax(maxMediaVolume);

        btnSaveSetting = (Button) itemView.findViewById(R.id.btn_save_setting);
    }


    @Override
    public void bindView(final UserVolume userContent) {
        if (userContent != null) {
            indoorRingVolume.setProgress(userContent.getIndoorRingVolume());
            indoorRingVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) userContent.setIndoorRingVolume(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            indoorMediaVolume.setProgress(userContent.getIndoorMediaVolume());
            indoorMediaVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) userContent.setIndoorMediaVolume(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            outdoorRingVolume.setProgress(userContent.getOutdoorRingVolume());
            outdoorRingVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) userContent.setOutdoorRingVolume(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            outdoorMediaVolume.setProgress(userContent.getOutdoorMediaVolume());
            outdoorMediaVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) userContent.setOutdoorMediaVolume(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            btnSaveSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("알림")
                            .setMessage("볼륨 설정을 저장하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    User user = Pref.getUser();
                                    user.replaceContent(userContent);
                                    Pref.setUser(user);

                                    Toast.makeText(v.getContext(), "볼륨 설정이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("취소", null)
                            .show();
                }
            });
        }
    }
}
