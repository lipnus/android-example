package com.lipnus.windowservicetest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


public class WindowService extends Service
{
    WindowManager   mWindowManager = null;

    View            mWindowLayout = null;

    @Override
    public int onStartCommand( Intent intent,
                               int flags,
                               int startId )
    {
        // 1. 레이아웃을 생성하기 위한 LayoutInflater 서비스 객체와
        //    윈도우를 추가하기 위한 윈도우 매니저 객체를 얻어온다.
        // ====================================================================
        LayoutInflater inflate = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // ====================================================================

        // 2. LayoutInflater 서비스를 이용하여 레이아웃을 생성한다.
        // ====================================================================
        mWindowLayout = inflate.inflate( R.layout.window_layout,
                null);

        // 레이아웃에 있는 버튼 객체를 얻어온다.
        Button closeWindwBtn = (Button) mWindowLayout.findViewById(
                R.id.close_window_btn );

        // 버튼을 클릭했을 때 윈도우를 종료하도록 한다.
        closeWindwBtn.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                mWindowManager.removeView( mWindowLayout );
            }
        } );
        // ====================================================================

        // 3. LayoutParams 객체를 생성하여 윈도우 정보를 설정한다.
        // ====================================================================
        WindowManager.LayoutParams windowParams =
                new WindowManager.LayoutParams();

        // 1) 윈도우 명을 설정한다.
        windowParams.setTitle("테스트윈도우");

        // 2) 윈도우 타입을 설정한다.
        windowParams.type = WindowManager.LayoutParams.TYPE_PHONE;

        // 3) 화면에 배치될 윈도우의 위치와 크기를 설정한다.
        windowParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowParams.x = 100;
        windowParams.y = 0;
        windowParams.width = 300;
        windowParams.height = 1000;
        // ====================================================================

        // 4. 윈도우매니저를 통해 윈도우를 추가한다.
        // ====================================================================
        mWindowManager.addView( mWindowLayout , windowParams );
        // ====================================================================

        return super.onStartCommand( intent, flags, startId );
    }

    @Override
    public void onDestroy()
    {
        mWindowManager.removeView( mWindowLayout );
        super.onDestroy();
    }

    @Override
    public IBinder onBind( Intent intent ){ return null; }
}