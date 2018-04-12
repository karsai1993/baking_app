package udacity.com.bakingtime.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import udacity.com.bakingtime.CommonApplicationFields;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.model.Step;

/**
 * Created by Laci on 11/04/2018.
 */

public class RecipeInfoListStepsFragment extends Fragment {

    public RecipeInfoListStepsFragment() {}

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mExoPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Step mStep;
    private TextView mInstructionTextView;

    private static final String TAG = RecipeInfoListStepsFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_recipe_info_list_steps,
                container,
                false
        );

        Bundle receivedStepsBundle = getArguments();
        mStep = receivedStepsBundle.getParcelable(CommonApplicationFields.STEP_EXTRA_DATA);

        mInstructionTextView = rootView.findViewById(R.id.tv_step_description);
        mInstructionTextView.setText(mStep.getDescription());

        mExoPlayerView = rootView.findViewById(R.id.exoplayer_step_video);

        initializeMediaSession();

        String videoUrl = mStep.getVideoUrl();
        String thumbnailUrl = mStep.getThumbnailUrl();
        if (videoUrl.isEmpty() && thumbnailUrl.isEmpty()) {
            mExoPlayerView.setVisibility(View.GONE);
        } else {
            mExoPlayerView.setVisibility(View.VISIBLE);
            if (videoUrl.isEmpty()) {
                initPlayer(Uri.parse(thumbnailUrl));
            } else {
                initPlayer(Uri.parse(videoUrl));
            }
        }

        return rootView;
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                );
        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MediaSessionCallback());
        mMediaSession.setActive(true);
    }

    private void initPlayer(Uri videoUri) {
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    getContext(),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl()
            );
            mExoPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(new ExoPlayer.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
                        mStateBuilder.setState(
                                PlaybackStateCompat.STATE_PLAYING,
                                mExoPlayer.getCurrentPosition(),
                                1f
                        );
                    } else if ((playbackState == ExoPlayer.STATE_READY)) {
                        mStateBuilder.setState(
                                PlaybackStateCompat.STATE_PAUSED,
                                mExoPlayer.getCurrentPosition(),
                                1f
                        );
                    }
                    mMediaSession.setPlaybackState(mStateBuilder.build());
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity() {

                }
            });
            String userAgent = Util.getUserAgent(getContext(), getContext().getApplicationInfo().name);
            MediaSource mediaSource = new ExtractorMediaSource(
                    videoUri,
                    new DefaultDataSourceFactory(
                            getContext(),
                            userAgent
                    ),
                    new DefaultExtractorsFactory(),
                    null,
                    null
            );
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
        mMediaSession.setActive(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    public static class MediaButtonBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
