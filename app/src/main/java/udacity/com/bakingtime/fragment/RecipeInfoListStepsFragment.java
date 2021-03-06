package udacity.com.bakingtime.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.squareup.picasso.Picasso;

import udacity.com.bakingtime.ApplicationHelper;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.model.Step;
import udacity.com.bakingtime.utils.NetworkUtils;

/**
 * This class creates the fragment of each step introduction.
 * The code regarding full screen and rotation-handling is from:
 * https://github.com/GeoffLedak/ExoplayerFullscreen
 */

public class RecipeInfoListStepsFragment extends Fragment
        implements
        Player.EventListener {

    public RecipeInfoListStepsFragment() {}

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mExoPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Step mStep;
    private ScrollView mInstructionScrollView;
    private TextView mInstructionTextView;
    private boolean mExoPlayerFullscreen = false;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;
    private MediaSource mMediaSource;
    private int mResumeWindow;
    private long mResumePosition;
    private View mView;
    private TextView mNetworkErrorTextView;
    private boolean mIsPlayWhenReady;
    private ImageView mStepThumbnailImageView;

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private static final String STATE_PLAYER_WHEN_READY = "playerWhenReady";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
            mIsPlayWhenReady = savedInstanceState.getBoolean(STATE_PLAYER_WHEN_READY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(
                R.layout.fragment_recipe_info_list_steps,
                container,
                false
        );
        populateUI();
        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        outState.putBoolean(STATE_PLAYER_WHEN_READY, mIsPlayWhenReady);

        super.onSaveInstanceState(outState);
    }


    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(
                getContext(),
                android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }


    private void openFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        mFullScreenDialog.addContentView(
                mExoPlayerView,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
        mFullScreenIcon.setImageDrawable(
                ContextCompat.getDrawable(
                        getContext(),
                        R.drawable.ic_icons8_collapse_96
                )
        );
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void closeFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        ((FrameLayout) mView.findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(
                ContextCompat.getDrawable(
                        getContext(),
                        R.drawable.ic_icons8_expand_96
                )
        );
    }


    private void initFullscreenButton() {

        PlaybackControlView controlView = mExoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }


    private void initExoPlayer() {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory
                = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                trackSelector,
                loadControl
        );
        mExoPlayerView.setPlayer(mExoPlayer);

        mExoPlayer.addListener(this);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            mExoPlayer.seekTo(mResumeWindow, mResumePosition);
        }

        if (mMediaSource!= null) mExoPlayer.prepare(mMediaSource);
        mExoPlayer.setPlayWhenReady(mIsPlayWhenReady);
    }

    private void populateUI() {
        if (mExoPlayerView == null) {

            mExoPlayerView = mView.findViewById(R.id.exoplayer);
            mStepThumbnailImageView = mView.findViewById(R.id.iv_step_thumbnail);

            Bundle receivedStepsBundle = getArguments();

            mStep = receivedStepsBundle.getParcelable(ApplicationHelper.STEP_EXTRA_DATA);

            mInstructionScrollView = mView.findViewById(R.id.sv_step_description);
            String description = mStep.getDescription();
            if (description.equals(mStep.getShortDescription())) {
                mInstructionScrollView.setVisibility(View.GONE);
            } else {
                mInstructionScrollView.setVisibility(View.VISIBLE);
                mInstructionTextView = mView.findViewById(R.id.tv_step_description);
                mInstructionTextView.setText(description);
            }

            initFullscreenDialog();
            initFullscreenButton();

            initializeMediaSession();

            String videoUrl = mStep.getVideoUrl();
            String thumbnailUrl = mStep.getThumbnailUrl();
            Uri uri = null;
            if (videoUrl.isEmpty() && thumbnailUrl.isEmpty()) {
                mExoPlayerView.setVisibility(View.GONE);
                mStepThumbnailImageView.setVisibility(View.GONE);
            } else {
                if (videoUrl.isEmpty()) {
                    mExoPlayerView.setVisibility(View.GONE);
                    mStepThumbnailImageView.setVisibility(View.VISIBLE);
                    uri = Uri.parse(thumbnailUrl);
                    Picasso.with(getContext())
                            .load(uri)
                            .placeholder(R.drawable.ic_icons8_load_96)
                            .error(R.drawable.ic_icons8_error_96)
                            .into(mStepThumbnailImageView);
                } else {
                    mExoPlayerView.setVisibility(View.VISIBLE);
                    mStepThumbnailImageView.setVisibility(View.GONE);
                    uri = Uri.parse(videoUrl);
                }
            }
            if (uri != null) {
                String userAgent = Util.getUserAgent(
                        getContext(),
                        getContext().getApplicationInfo().packageName
                );
                mMediaSource = new ExtractorMediaSource(
                        uri,
                        new DefaultDataSourceFactory(
                                getContext(),
                                userAgent
                        ),
                        new DefaultExtractorsFactory(),
                        null,
                        null
                );
            }

            mNetworkErrorTextView = mView.findViewById(R.id.tv_exopayer_network_error);
            boolean isNetworkAvailable = NetworkUtils.isNetworkAvailable(getContext());
            if ((mExoPlayerView.getVisibility() == View.VISIBLE
                    || mStepThumbnailImageView.getVisibility() == View.VISIBLE)
                    && !isNetworkAvailable) {
                mExoPlayerView.setVisibility(View.GONE);
                mStepThumbnailImageView.setVisibility(View.GONE);
                mNetworkErrorTextView.setVisibility(View.VISIBLE);
            } else if ((mExoPlayerView.getVisibility() == View.GONE
                    && mStepThumbnailImageView.getVisibility() == View.GONE)
                    || !isNetworkAvailable) {
                mExoPlayerView.setVisibility(View.GONE);
                mStepThumbnailImageView.setVisibility(View.GONE);
                mNetworkErrorTextView.setVisibility(View.GONE);
            } else if ((mExoPlayerView.getVisibility() == View.VISIBLE
                    || mStepThumbnailImageView.getVisibility() == View.VISIBLE)
                    && isNetworkAvailable) {
                mNetworkErrorTextView.setVisibility(View.GONE);
            }
        }

        if (mExoPlayerFullscreen) {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
            mFullScreenDialog.addContentView(
                    mExoPlayerView,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    )
            );
            mFullScreenIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                            getContext(),
                            R.drawable.ic_icons8_collapse_96
                    )
            );
            mFullScreenDialog.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) initExoPlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M || mExoPlayer == null) initExoPlayer();
    }


    @Override
    public void onPause() {

        super.onPause();

        if (mExoPlayerView != null && mExoPlayerView.getPlayer() != null) {
            mResumeWindow = mExoPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, mExoPlayerView.getPlayer().getContentPosition());
            mIsPlayWhenReady = mExoPlayer.getPlayWhenReady();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) releasePlayer();
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(
                getContext(),
                RecipeInfoListStepsFragment.class.getName());
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

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.removeListener(this);
            mExoPlayer.release();
            mExoPlayer = null;
        }
        mMediaSession.setActive(false);
    }

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
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(),
                    1f
            );
        } else if ((playbackState == Player.STATE_READY)) {
            mStateBuilder.setState(
                    PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(),
                    1f
            );
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

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
