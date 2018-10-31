package com.lipnus.facedetect;

/*
 * 이 클래스는 전달받은 이미지 포멧을 변환하는 클래스다.
 * SurfaceView 에서 PreviewCallback으로 전달받은 이미지는 YUV420 이라는 듣도보도 못한 희안한 포멧이다.
 * 따라서 그냥 BitmapFactory 를 이용해서 Bitmap 으로 만들 수 없다.
 * 저 포멧은 아날로그 시절 TV용 포멧... 암튼 다음과 같은 함수를 이용해서 바꾼다.
 */
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class PreviewData {
    private Bitmap bitmap = null; // 비트맵. SurfaceView 는 이걸 항상 갱신 시킨다.
    public boolean renewFlag = false; // 갱신되면 true, 아니면 false
    public boolean preccessFlag = false; // 현재 이미지 변환중이면 true, 아니면 false

    private int[] rgbdata = new int[MainActivity.SCREEN_WIDTH * MainActivity.SCREEN_HEIGHT]; // 이것은 RGB 데이터를 저장할 배열.
    private final int frameSize = MainActivity.SCREEN_WIDTH * MainActivity.SCREEN_HEIGHT;

    public PreviewData() {
        bitmap = Bitmap.createBitmap(320, 240, Config.RGB_565);
    }

    // 이 함수를 이용해서 전달받은 이미지를 rgb 포멧으로 바꾼다.
    // 미안하지만 이 내용은 나도 모르겠다. yuv 라는 포멧의 정체를 모르니 할수없다.
    // 알아서 공부하시고.. 혹시 알게되면 저에게도 좀 갈켜주시기 바랍니다.
    public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
        preccessFlag = true;
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0) y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0) r = 0; else if (r > 262143) r = 262143;
                if (g < 0) g = 0; else if (g > 262143) g = 262143;
                if (b < 0) b = 0; else if (b > 262143) b = 262143;

                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
        preccessFlag = false;
    }

    public void setbitmap(byte[] data){
        // 변환중에는 새 데이터를 받지 말자.
        if(data != null && bitmap != null && !preccessFlag){
            synchronized (bitmap) {
                decodeYUV420SP(rgbdata, data, 320, 240);
                bitmap.setPixels(rgbdata, 0, 320, 0, 0, 320, 240);
                renewFlag = true;
            }
        }
    }

    public Bitmap getbitmap(){
        synchronized (bitmap) {
            return bitmap;
        }
    }

    public void setRenewFlag(boolean value){
        renewFlag = value;
    }

}
