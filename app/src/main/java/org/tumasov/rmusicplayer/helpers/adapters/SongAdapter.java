package org.tumasov.rmusicplayer.helpers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONObject;
import org.tumasov.rmusicplayer.R;
import java.util.LinkedList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private final LinkedList<JSONObject> jSongs = new LinkedList<>();

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
        return jSongs.size();
    }

    public LinkedList<JSONObject> getjSongs() {
        return jSongs;
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        private TextView songArtist;
        private TextView songTitle;

        SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_item_title);
        }

        void bind(int lastIndex) {
            try {
                songTitle.setText(jSongs.get(lastIndex).getString("title"));
                //songTitle.setOnClickListener();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
