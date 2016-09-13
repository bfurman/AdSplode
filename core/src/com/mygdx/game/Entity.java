package com.mygdx.game;

/**
 * Created by Bradley on 9/12/2016.
 */

//change type to return enum
public interface Entity {
    public int type();
    public void onContact();
    public String contactDebug();
}
