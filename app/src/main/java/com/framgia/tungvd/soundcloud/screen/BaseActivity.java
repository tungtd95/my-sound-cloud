package com.framgia.tungvd.soundcloud.screen;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.screen.play.PlayFragment;

public class BaseActivity extends AppCompatActivity {

    protected void showPlayScreen() {
        PlayFragment playFragment = new PlayFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.linear_layout_replace, playFragment);
        ft.commit();
    }
}
