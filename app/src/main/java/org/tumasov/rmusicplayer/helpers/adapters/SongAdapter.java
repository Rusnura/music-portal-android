package org.tumasov.rmusicplayer.helpers.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.entities.Song;
import org.tumasov.rmusicplayer.events.OnMediaClickListener;
import org.tumasov.rmusicplayer.helpers.MP3Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private final List<Song> songs = new ArrayList<>();
    private final String selectedAlbumId;
    private final MP3Player mp3Player = MP3Player.getInstance();
    private final OnMediaClickListener onMediaClickListener;
    private Context context;

    public SongAdapter(String selectedAlbumId, OnMediaClickListener onMediaClickListener) { // TODO: Change this behaviour
        this.selectedAlbumId = selectedAlbumId;
        this.onMediaClickListener = onMediaClickListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        int songItemLayout = R.layout.song_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View songItem = inflater.inflate(songItemLayout, parent, false);

        return new SongViewHolder(songItem);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public List<Song> getSongs() {
        return songs;
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        private TextView songArtist;
        private TextView songTitle;

        SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songArtist = itemView.findViewById(R.id.song_item_artist);
            songTitle = itemView.findViewById(R.id.song_item_title);
        }

        void bind(int lastIndex) {
            Song song = songs.get(lastIndex);
            songTitle.setText(song.getTitle());
            songArtist.setText(song.getArtist());
            songTitle.setOnClickListener(l -> {
                if (onMediaClickListener != null) {
                    onMediaClickListener.OnMediaClickListener(song);
                }
            });
        }
    }
}
