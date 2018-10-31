package com.lipnus.megabox2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;

import com.asha.vrlib.MDVRLibrary;

public class MainActivity extends AppCompatActivity {

    private MDVRLibrary mVRLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init VR Library
        initVRLibrary();
    }


    private void initVRLibrary(){
        // new instance
        mVRLibrary = MDVRLibrary.with(this)
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION)
                .asVideo(new MDVRLibrary.IOnSurfaceReadyCallback() {
                    @Override
                    public void onSurfaceReady(Surface surface) {
                        // IjkMediaPlayer or MediaPlayer
                        getPlayer().setSurface(surface);
                    }
                })
                .build(R.id.surface_view);
    }


}
