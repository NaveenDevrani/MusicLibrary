package com.naveen.musiclibrary.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naveen.musiclibrary.R;
import com.naveen.musiclibrary.modelclasss.MusicModel;
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
        View view = LayoutInflater.from( context ).inflate( R.layout.custom_view_music, viewGroup,false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {
        Picasso.with( context ).
                load(list.get( i ).getArtworkUrl60())
                .placeholder( R.drawable.ic_music_placeholder )
                .error( R.drawable.ic_music_placeholder )
                .into( viewHolder.image );
        viewHolder.tv_artistName.setText( list.get(i).getArtistName() );
        viewHolder.tv_duration.setText( getduration( list.get( i ).getTrackTimeMillis() ) );

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView( R.id.tv_artistName)
        TextView tv_artistName;
        @BindView( R.id.tv_duration)
        TextView tv_duration;

        @BindView( R.id.image)
        CircleImageView image;

        public ViewHolder(@NonNull View itemView)
        {
            super( itemView );
            ButterKnife.bind( this,itemView );
        }
    }

    @SuppressLint("DefaultLocale")
    public String getduration(int millis)
    {
       return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
