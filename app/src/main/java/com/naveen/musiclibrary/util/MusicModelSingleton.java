package com.naveen.musiclibrary.util;

import com.naveen.musiclibrary.modelclasss.MusicModel;

public  class  MusicModelSingleton
{

 public  static    MusicModelSingleton obj;
  public    MusicModel musicModel;

    public  static  MusicModelSingleton getInstance()
    {
        if(obj==null)
        {
            obj=new MusicModelSingleton();
        }
        return obj;
    }

    public  void setMusicModel(MusicModel musicModel)
    {
        this.musicModel=musicModel;
    }

    public MusicModel getMusicModel()
    {
        return musicModel;
    }

}
