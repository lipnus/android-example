package com.lipnus.windowmovetest;

/**
 * Created by Sunpil on 2016-05-01.
 */

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

/**
 미디어 스캐너. 저장된 파일을 단말기가 인식해서 갤러리에 업데이트 시켜준다.
 */
public class MediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {
    private MediaScannerConnection mScanner;
    private String mFilepath = null;

    public MediaScanner(Context context) {
        mScanner = new MediaScannerConnection(context, this);
    }

    public void startScan(String filepath) {
        mFilepath = filepath;
        Log.d("WindowTest", mFilepath);
        mScanner.connect(); // 이 함수 호출 후 onMediaScannerConnected가 호출 됨.
    }

    @Override
    public void onMediaScannerConnected() {
        if(mFilepath != null) {
            String filepath = new String(mFilepath);
            mScanner.scanFile(filepath, null); // MediaStore의 정보를 업데이트
        }

        mFilepath = null;
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mScanner.disconnect(); // onMediaScannerConnected 수행이 끝난 후 연결 해제
    }
}