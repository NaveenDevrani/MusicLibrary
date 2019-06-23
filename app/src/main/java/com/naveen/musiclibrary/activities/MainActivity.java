package com.naveen.musiclibrary.activities;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.naveen.musiclibrary.R;
import com.naveen.musiclibrary.adapters.MusicAdapter;
import com.naveen.musiclibrary.modelclasss.MusicModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    ArrayList<MusicModel> list;
    private Context context;

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        ButterKnife.bind( this );
        list=new ArrayList<>(  );

        context = this;
        recyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
        setRecyclerViewData();

    }

    private void setRecyclerViewData()
    {
        CollectData();
        if(list!=null && list.size()>0)
        {
            MusicAdapter musicAdapter=new MusicAdapter( list,context );
            recyclerView.setAdapter( musicAdapter );
        }
    }

    public void CollectData()
    {
        String data = getData();
        if(data != null && !data.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject( data );
//            JSONObject jsonObject1=jsonObject.getJSONObject( "results" );
                MusicModel musicModel;
                JSONArray jsonArray = jsonObject.getJSONArray( "results" );
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject( i );
                     musicModel = new MusicModel();
                    if(object.has( "wrapperType" )) {
                        musicModel.setWrapperType( object.getString( "wrapperType" ) );
                    }

                    if(object.has( "kind" )) {
                        musicModel.setKind( object.getString( "kind" ) );
                    }
                    if(object.has( "artistId" )) {
                        musicModel.setArtistId( object.getInt( "artistId" ) );
                    }
                    if(object.has( "collectionId" )) {
                        musicModel.setCollectionId( object.getInt( "collectionId" ) );
                    }
                    if(object.has( "trackId" )) {
                        musicModel.setTrackId( object.getInt( "trackId" ) );
                    }

                    if(object.has( "artistName" )) {
                        musicModel.setArtistName( object.getString( "artistName" ) );
                    }


                    if(object.has( "collectionName" )) {
                        musicModel.setCollectionName( object.getString( "collectionName" ) );
                    }

                    if(object.has( "trackName" )) {
                        musicModel.setTrackName( object.getString( "trackName" ) );
                    }
                    if(object.has( "collectionCensoredName" )) {
                        musicModel.setCollectionCensoredName( object.getString( "collectionCensoredName" ) );
                    }

                    if(object.has( "trackCensoredName" )) {
                        musicModel.setTrackCensoredName( object.getString( "trackCensoredName" ) );
                    }

                    if(object.has( "artistViewUrl" )) {
                        musicModel.setArtistViewUrl( object.getString( "artistViewUrl" ) );
                    }

                    if(object.has( "collectionViewUrl" )) {
                        musicModel.setCollectionViewUrl( object.getString( "collectionViewUrl" ) );
                    }

                    if(object.has( "trackViewUrl" )) {
                        musicModel.setTrackViewUrl( object.getString( "trackViewUrl" ) );
                    }

                    if(object.has( "previewUrl" )) {
                        musicModel.setPreviewUrl( object.getString( "previewUrl" ) );
                    }

                    if(object.has( "artworkUrl30" )) {
                        musicModel.setArtworkUrl30( object.getString( "artworkUrl30" ) );
                    }

                    if(object.has( "artworkUrl60" )) {
                        musicModel.setArtworkUrl60( object.getString( "artworkUrl60" ) );
                    }

                    if(object.has( "artworkUrl100" )) {
                        musicModel.setArtworkUrl100( object.getString( "artworkUrl100" ) );
                    }

                    if(object.has( "collectionPrice" )) {
                        musicModel.setCollectionPrice( object.getInt( "collectionPrice" ) );
                    }

                    if(object.has( "releaseDate" )) {
                        musicModel.setReleaseDate( object.getString( "releaseDate" ) );
                    }

                    if(object.has( "collectionExplicitness" )) {
                        musicModel.setCollectionExplicitness( object.getString( "collectionExplicitness" ) );
                    }

                    if(object.has( "trackExplicitness" )) {
                        musicModel.setTrackExplicitness( object.getString( "trackExplicitness" ) );
                    }

                    if(object.has( "discCount" )) {
                        musicModel.setDiscCount( object.getInt( "discCount" ) );
                    }

                    if(object.has( "discNumber" )) {
                        musicModel.setDiscNumber( object.getInt( "discNumber" ) );
                    }
                    if(object.has( "trackCount" )) {
                        musicModel.setTrackCount( object.getInt( "trackCount" ) );
                    }
                    if(object.has( "trackNumber" )) {
                        musicModel.setTrackNumber( object.getInt( "trackNumber" ) );
                    }
                    if(object.has( "trackTimeMillis" )) {
                        musicModel.setTrackTimeMillis( object.getInt( "trackTimeMillis" ) );
                    }
                    if(object.has( "country" )) {
                        musicModel.setCountry( object.getString( "country" ) );
                    }
                    if(object.has( "currency" )) {
                        musicModel.setCurrency( object.getString( "currency" ) );
                    }
                    if(object.has( "primaryGenreName" )) {
                        musicModel.setPrimaryGenreName( object.getString( "primaryGenreName" ) );
                    }
                    if(object.has( "isStreamable" )) {
                        musicModel.setIsStreamable( object.getBoolean( "isStreamable" ) );
                    }

                    list.add( musicModel);
                }



            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getData()
    {
        try {
            AssetManager am = context.getAssets();
            InputStream instream = am.open( "musicdata.txt" );
            if(instream != null) {
                InputStreamReader inputreader = new InputStreamReader( instream );
                BufferedReader buffreader = new BufferedReader( inputreader );
                String line, line1 = "";
                try {
                    while((line = buffreader.readLine()) != null)
                        line1 += line;
                } catch(Exception e) {
                    e.printStackTrace();
                }

                return line1;
            }


        } catch(Exception e) {
            String error = "";
            error = e.getMessage();
            return "";
        }
        return "";
    }
}
