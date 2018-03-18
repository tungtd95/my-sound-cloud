package com.framgia.tungvd.soundcloud.custom;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.Playlist;
import com.framgia.tungvd.soundcloud.data.source.PlaylistDataSource;
import com.framgia.tungvd.soundcloud.data.source.PlaylistRepository;
import com.framgia.tungvd.soundcloud.data.source.local.MyDBHelper;
import com.framgia.tungvd.soundcloud.data.source.local.PlaylistLocalDataSource;
import com.framgia.tungvd.soundcloud.data.source.remote.PlaylistRemoteDataSource;
import com.framgia.tungvd.soundcloud.util.AppExecutors;

public class CreatePlaylistDialog extends Dialog implements View.OnClickListener {

    private EditText mEditName;
    private Button mButtonCreate;
    private Button mButtonCancel;
    private PlaylistRepository mPlaylistRepository;
    private PlaylistDataSource.PlaylistInsertCallback mPlaylistInsertCallback;

    public CreatePlaylistDialog(@NonNull Context context,
                                PlaylistRepository playlistRepository,
                                PlaylistDataSource.PlaylistInsertCallback callback) {
        super(context);
        setContentView(R.layout.dialog_create_playlist);
        setCancelable(false);
        mPlaylistInsertCallback = callback;
        mEditName = findViewById(R.id.edit_playlist_name);
        mButtonCreate = findViewById(R.id.button_create);
        mButtonCancel = findViewById(R.id.button_cancel);
        mPlaylistRepository = playlistRepository;
        mButtonCancel.setOnClickListener(this);
        mButtonCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_cancel:
                dismiss();
                break;
            case R.id.button_create:
                String name = mEditName.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(getContext(),
                            getContext().getResources().getText(R.string.mgs_empty_name),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                mPlaylistRepository.savePlaylist(new Playlist(System.currentTimeMillis(), name),
                        mPlaylistInsertCallback);
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}
