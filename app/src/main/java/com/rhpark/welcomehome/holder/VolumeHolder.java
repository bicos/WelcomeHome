package com.rhpark.welcomehome.holder;

import android.content.Context;
import android.media.AudioManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SeekBar;

import com.rhpark.welcomehome.R;
import com.rhpark.welcomehome.adapter.MainListAdapter;
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
    }


    @Override
    public void bindView(UserVolume userContent) {
        if (userContent != null) {
            indoorRingVolume.setProgress(userContent.getIndoorRingVolume());
            indoorMediaVolume.setProgress(userContent.getIndoorMediaVolume());
            outdoorRingVolume.setProgress(userContent.getOutdoorRingVolume());
            outdoorMediaVolume.setProgress(userContent.getOutdoorMediaVolume());
        }
    }
}
