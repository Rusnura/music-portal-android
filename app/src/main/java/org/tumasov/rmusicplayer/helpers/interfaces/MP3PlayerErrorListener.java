package org.tumasov.rmusicplayer.helpers.interfaces;

import org.tumasov.rmusicplayer.helpers.player.MP3Player;

public interface MP3PlayerErrorListener {
    boolean onError(MP3Player mp3Player, int what, int extra);
}
