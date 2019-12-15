package org.tumasov.rmusicplayer.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.entities.Playlist;
import org.tumasov.rmusicplayer.ui.SongsActivity;

import java.util.ArrayList;
import java.util.List;
import static androidx.core.content.ContextCompat.startActivity;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private Context context;
    private final List<Playlist> playlists = new ArrayList<>();
    private static Intent openPlaylistContentsIntent;

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        int playlistItemLayout = R.layout.playlist_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View playlistItem = layoutInflater.inflate(playlistItemLayout, parent, false);

        openPlaylistContentsIntent = new Intent(context, SongsActivity.class);
        return new PlaylistViewHolder(playlistItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private TextView playlistTitle;
        private TextView playlistDescription;

        PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistTitle = itemView.findViewById(R.id.playlist_item_title);
            playlistDescription = itemView.findViewById(R.id.playlist_item_description);
        }

        void bind(int listIndex) {
            playlistTitle.setText(playlists.get(listIndex).getName());
            playlistDescription.setText(playlists.get(listIndex).getDescription());

            // TODO: Change this
            playlistTitle.setOnClickListener(e -> onClick(playlists.get(listIndex)));
            playlistDescription.setOnClickListener(e -> onClick(playlists.get(listIndex)));
        }

        void onClick(Playlist playlist) {
            openPlaylistContentsIntent.putExtra("playlistId", playlist.getId());
            startActivity(context, openPlaylistContentsIntent, null);
        }
    }
}
