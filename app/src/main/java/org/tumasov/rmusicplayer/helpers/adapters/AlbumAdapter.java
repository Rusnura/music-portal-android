package org.tumasov.rmusicplayer.helpers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONException;
import org.json.JSONObject;
import org.tumasov.rmusicplayer.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private List<JSONObject> jAlbums = new ArrayList<>();

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int albumsItemLayout = R.layout.album_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View albumItem = layoutInflater.inflate(albumsItemLayout, parent, false);

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
                albumTitle.setText(jAlbums.get(listIndex).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
