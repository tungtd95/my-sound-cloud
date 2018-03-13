package com.framgia.tungvd.soundcloud.screen;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.MusicService;
import com.framgia.tungvd.soundcloud.screen.play.PlayFragment;

public class BaseActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "BaseActivity";

    protected PlayFragment mPlayFragment;
    protected RelativeLayout mRelativeSubController;
    protected MusicService mMusicService;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mMusicService = ((MusicService.MyBinder) iBinder).getMusicService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    protected void initMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        startService(intent);
    }

    protected void showPlayScreen() {
        mPlayFragment = new PlayFragment();
        mPlayFragment.setMusicService(mMusicService);
        mPlayFragment.show(getSupportFragmentManager(), mPlayFragment.getTag());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_sub_controller:
                showPlayScreen();
                break;
            default:
                break;
        }
    }
}
