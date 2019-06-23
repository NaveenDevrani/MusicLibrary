package com.naveen.musiclibrary.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.naveen.musiclibrary.R;
import com.naveen.musiclibrary.modelclasss.MusicModel;
import com.naveen.musiclibrary.util.AppConst;
import com.naveen.musiclibrary.util.MusicModelSingleton;
import com.naveen.musiclibrary.util.UtilClass;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindFont;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity
{
    @BindView(R.id.tv_artistName)
    TextView tv_artistName;
    @BindView(R.id.tv_kind)
    TextView tv_kind;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_collectionName)
    TextView tv_collectionName;
    @BindView(R.id.tv_country)
    TextView tv_country;

    @BindView(R.id.tv_currency)
    TextView tv_currency;

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.fab_play)
    FloatingActionButton fab_play;
    @BindView(R.id.current_progress_text_view)
    TextView current_progress_text_view;
    @BindView(R.id.file_length_text_view)
    TextView file_length_text_view;
    //stores minutes and seconds of the length of the file.
    long minutes = 0;
    long seconds = 0;
    private MusicModel musicModel;
    private Context context;
    private int duration;
    private Handler mHandler = new Handler();
    private MediaPlayer mMediaPlayer = null;
    private boolean isPlaying = false;
    private String previewUrl = "";



    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detail );
        ButterKnife.bind( this );
        context = this;
        setSupportActionBar( toolbar );
        musicModel = MusicModelSingleton.getInstance().getMusicModel();

        String url = musicModel.getArtworkUrl100();
        String kind = musicModel.getKind();
        String artistname = musicModel.getArtistName();
        String musicname = musicModel.getTrackName();
        String collectionName = musicModel.getCollectionName();
        String country = musicModel.getCountry();
        String currency = musicModel.getCurrency();
        previewUrl = musicModel.getPreviewUrl();



        if(url != null)
        Glide.with(context)
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_music_placeholder)
                .into(image);

        if(kind != null)
            tv_kind.setText( kind );
        if(artistname != null)
            tv_artistName.setText( artistname );

        if(collectionName != null)
            tv_collectionName.setText( collectionName );
        if(currency != null)
            tv_currency.setText( currency );
        if(country != null)
            tv_country.setText( country );

        collapsingToolbarLayout.setTitle( musicname );
        toolbar.setTitle( musicname );

        seekbar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener()
        {
            @SuppressLint("DefaultLocale")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if(mMediaPlayer != null && fromUser) {
                    mMediaPlayer.seekTo( progress );
                    mHandler.removeCallbacks( mRunnable );

                    long minutes = TimeUnit.MILLISECONDS.toMinutes( mMediaPlayer.getCurrentPosition() );
                    long seconds = TimeUnit.MILLISECONDS.toSeconds( mMediaPlayer.getCurrentPosition() )
                            - TimeUnit.MINUTES.toSeconds( minutes );
                    current_progress_text_view.setText( String.format( "%02d:%02d", minutes, seconds ) );

                    updateSeekBar();

                } else if(mMediaPlayer == null && fromUser) {
                    prepareMediaPlayerFromPoint( progress );
                    updateSeekBar();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                if(mMediaPlayer != null) {
                    // remove message Handler from updating progress bar
                    mHandler.removeCallbacks( mRunnable );
                }
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                if(mMediaPlayer != null) {
                    mHandler.removeCallbacks( mRunnable );
                    mMediaPlayer.seekTo( seekBar.getProgress() );

                    long minutes = TimeUnit.MILLISECONDS.toMinutes( mMediaPlayer.getCurrentPosition() );
                    long seconds = TimeUnit.MILLISECONDS.toSeconds( mMediaPlayer.getCurrentPosition() )
                            - TimeUnit.MINUTES.toSeconds( minutes );
                    current_progress_text_view.setText( String.format( "%02d:%02d", minutes, seconds ) );
                    updateSeekBar();
                }
            }
        } );
    }
    @OnClick(R.id.fab_play)
    public void onclick()
    {
        onPlay( isPlaying );
        isPlaying=!isPlaying;
    }


    // Play start/stop
    private void onPlay(boolean isPlaying)
    {
        if(!isPlaying) {
            //currently MediaPlayer is not playing audio
            if(mMediaPlayer == null) {
                if(UtilClass.isNetworkConnected( this )) {
                    UtilClass.showSimpleProgressDialog( this );
                    startPlaying(); //start from beginning
                }else
                {
                    Toast.makeText( context, "Please check your internet Connnection", Toast.LENGTH_SHORT ).show();
                }
            } else {
                resumePlaying(); //resume the currently paused MediaPlayer
            }

        } else {
            //pause the MediaPlayer
            pausePlaying();
        }
    }

    private void startPlaying()
    {
        fab_play.setImageResource( R.drawable.ic_pause );
        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource( previewUrl );
            mMediaPlayer.prepare();
            int time=mMediaPlayer.getDuration();
            seekbar.setMax( mMediaPlayer.getDuration() );
            file_length_text_view.setText(getduration(time));

            mMediaPlayer.setOnPreparedListener( new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp)
                {
                    UtilClass.hideSimpleProgressDialog();
                    mMediaPlayer.start();
                }
            } );
        } catch(IOException e) {
        }

        mMediaPlayer.setOnCompletionListener( new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                stopPlaying();
            }
        } );

        updateSeekBar();
    }

    private void pausePlaying()
    {
        fab_play.setImageResource( R.drawable.ic_play );
        mHandler.removeCallbacks( mRunnable );
        mMediaPlayer.pause();
    }

    private void resumePlaying()
    {
        fab_play.setImageResource( R.drawable.ic_pause );
        mHandler.removeCallbacks( mRunnable );
        mMediaPlayer.start();
        updateSeekBar();
    }

    private void stopPlaying()
    {
        fab_play.setImageResource( R.drawable.ic_play );
        mHandler.removeCallbacks( mRunnable );
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;

        seekbar.setProgress( seekbar.getMax() );
        isPlaying = !isPlaying;

        current_progress_text_view.setText( file_length_text_view.getText() );
        seekbar.setProgress( seekbar.getMax() );
    }

    private void updateSeekBar()
    {
        mHandler.postDelayed( mRunnable, 1000 );
    }

    private void prepareMediaPlayerFromPoint(int progress)
    {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource( previewUrl );
            mMediaPlayer.prepare();
            seekbar.setMax( mMediaPlayer.getDuration() );
            mMediaPlayer.seekTo( progress );
            mMediaPlayer.setOnCompletionListener( new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    stopPlaying();
                }
            } );

        } catch(IOException e) {
        }
    }

    //updating mSeekBar
    private Runnable mRunnable = new Runnable()
    {
        @SuppressLint("DefaultLocale")
        @Override
        public void run()
        {
            if(mMediaPlayer != null) {

                int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                seekbar.setProgress( mCurrentPosition );

                long minutes = TimeUnit.MILLISECONDS.toMinutes( mCurrentPosition );
                long seconds = TimeUnit.MILLISECONDS.toSeconds( mCurrentPosition )
                        - TimeUnit.MINUTES.toSeconds( minutes );
                current_progress_text_view.setText( String.format( "%02d:%02d", minutes, seconds ) );
                updateSeekBar();
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();

        if (mMediaPlayer != null) {
            stopPlaying();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            stopPlaying();
        }
    }
    @SuppressLint("DefaultLocale")
    public String getduration(int millis)
    {
        return String.format( "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes( millis ) -
                        TimeUnit.HOURS.toMinutes( TimeUnit.MILLISECONDS.toHours( millis ) ), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds( millis ) -
                        TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( millis ) ) );
    }
}
