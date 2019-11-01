package org.tumasov.rmusicplayer.helpers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.entities.Song;
import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private final List<Song> songs = new ArrayList<>();

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

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
            songTitle = itemView.findViewById(R.id.song_item_title);
        }

        void bind(int lastIndex) {
            songTitle.setText(songs.get(lastIndex).getTitle());
            //songTitle.setOnClickListener();
        }
    }
}
