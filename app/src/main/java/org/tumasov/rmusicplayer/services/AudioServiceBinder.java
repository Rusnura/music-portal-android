package org.tumasov.rmusicplayer.services;

import android.os.Binder;
import org.tumasov.rmusicplayer.entities.Song;
import org.tumasov.rmusicplayer.helpers.player.MP3Player;
import java.util.LinkedList;
import java.util.List;

public class AudioServiceBinder extends Binder {
    private final MP3Player player = MP3Player.getInstance();
    private final List<Song> playlist = new LinkedList<>();

    public MP3Player getPlayer() {
        return player;
    }

    public List<Song> getPlaylist() {
        return playlist;
    }
}