package org.tumasov.rmusicplayer.helpers.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONException;
import org.json.JSONObject;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.ui.AudioListActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private Context context;
    private final List<JSONObject> jAlbums = new LinkedList<>();
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
        return jAlbums.size();
    }

    public List<JSONObject> getjAlbums() {
        return jAlbums;
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {
        private TextView albumTitle;
        private TextView albumDescription;

        AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumTitle = itemView.findViewById(R.id.album_item_title);
        }

        void bind(int listIndex) {
            try {
                String albumId = jAlbums.get(listIndex).getString("id");
                albumTitle.setText(jAlbums.get(listIndex).getString("name"));
                albumTitle.setOnClickListener(e -> onClick(albumId));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        void onClick(String albumId) {
            openAlbumContentsIntent.putExtra("albumId", albumId);
            startActivity(context, openAlbumContentsIntent, null);
        }
    }
}
