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
import org.tumasov.rmusicplayer.helpers.MP3Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private final List<Song> songs = new ArrayList<>();
    private final String selectedAlbumId;
    private final MP3Player mp3Player = MP3Player.getInstance();
    private Context context;

    public SongAdapter(String selectedAlbumId) { // TODO: Change this behaviour
        this.selectedAlbumId = selectedAlbumId;
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
            songTitle.setText(songs.get(lastIndex).getTitle());
            songArtist.setText(songs.get(lastIndex).getArtist());
            songTitle.setOnClickListener((l) -> {
                try {
                    mp3Player.play(context, selectedAlbumId, songs.get(lastIndex).getId());
                } catch (IOException e) {
                    Log.e("SONG_ADAPTER", "Cannot to start playing MP3: " + e.getMessage(), e);
                }
            });
        }
    }
}
