package com.naveen.musiclibrary.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.naveen.musiclibrary.R;
import com.naveen.musiclibrary.activities.DetailActivity;
import com.naveen.musiclibrary.modelclasss.MusicModel;
import com.naveen.musiclibrary.util.MusicModelSingleton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>
{

    private ArrayList<MusicModel> list;
    private Context context;

    public MusicAdapter(ArrayList<MusicModel> list, Context context)
    {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from( context ).inflate( R.layout.custom_view_music, viewGroup, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {

        String url = list.get( i ).getArtworkUrl60();
        if(url != null)
            Glide.with( context )
                    .load( url ).thumbnail(0.5f)
                    .skipMemoryCache( true )
                    .diskCacheStrategy( DiskCacheStrategy.NONE )
                    .error( R.drawable.ic_music_placeholder )
                    .into( viewHolder.image );
//            Glide.with( context )
//                    .load( url ).placeholder( R.drawable.ic_music_placeholder )
//                    .error( R.drawable.ic_music_placeholder )
//                    .into( viewHolder.image );

        Log.e( "adataper", "url :" + list.get( i ).getArtworkUrl30() );

        String trackname = list.get( i ).getTrackName();
        if(trackname != null)
            viewHolder.tv_artistName.setText( trackname );
        String  type = list.get( i ).getKind();
        viewHolder.tv_duration.setText(type);
        viewHolder.card_outer.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MusicModelSingleton.getInstance().setMusicModel( list.get( i ) );
                context.startActivity( new Intent( context, DetailActivity.class ) );
            }
        } );

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @SuppressLint("DefaultLocale")
    public String getduration(int millis)
    {
        return String.format( "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours( millis ),
                TimeUnit.MILLISECONDS.toMinutes( millis ) -
                        TimeUnit.HOURS.toMinutes( TimeUnit.MILLISECONDS.toHours( millis ) ), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds( millis ) -
                        TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( millis ) ) );
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_artistName)
        TextView tv_artistName;
        @BindView(R.id.tv_duration)
        TextView tv_duration;

        @BindView(R.id.image)
        CircleImageView image;
        @BindView(R.id.card_outer)
        CardView card_outer;

        public ViewHolder(@NonNull View itemView)
        {
            super( itemView );
            ButterKnife.bind( this, itemView );
        }
    }
}
