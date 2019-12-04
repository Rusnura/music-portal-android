package org.tumasov.rmusicplayer.events;

import org.tumasov.rmusicplayer.entities.Song;

public interface OnMediaClickListener {
    void OnMediaClickListener(Song song, int index);
}
