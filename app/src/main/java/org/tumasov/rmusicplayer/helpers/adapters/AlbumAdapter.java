package org.tumasov.rmusicplayer.helpers.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.entities.Album;
import org.tumasov.rmusicplayer.ui.AudioListActivity;
import java.util.ArrayList;
import java.util.List;
import static androidx.core.content.ContextCompat.startActivity;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private Context context;
    private final List<Album> albums = new ArrayList<>();
    private static Intent openAlbumContentsIntent;

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        int albumsItemLayout = R.layout.album_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View albumItem = layoutInflater.inflate(albumsItemLayout, parent, false);

        openAlbumContentsIntent = new Intent(context, AudioListActivity.class);
        return new AlbumViewHolder(albumItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public List<Album> getAlbums() {
        return albums;
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {
        private TextView albumTitle;
        private TextView albumDescription;

        AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumTitle = itemView.findViewById(R.id.album_item_title);
            albumDescription = itemView.findViewById(R.id.album_item_description);
        }

        void bind(int listIndex) {
            albumTitle.setText(albums.get(listIndex).getName());
            albumDescription.setText(albums.get(listIndex).getDescription());

            // TODO: Change this
            albumTitle.setOnClickListener(e -> onClick(albums.get(listIndex)));
            albumDescription.setOnClickListener(e -> onClick(albums.get(listIndex)));
        }

        void onClick(Album album) {
            openAlbumContentsIntent.putExtra("albumId", album.getId());
            startActivity(context, openAlbumContentsIntent, null);
        }
    }
}
