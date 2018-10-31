package com.lipnus.facedetect;

/*
 * 이 클래스는 화면에 사각형을 그리기 위한 클래스다.
 * SurfaceView 위에 그릴려고 했는데 에러가 나더라. SurfaceView 의 타입에 따라 그릴 수 없는게 있는 모양이다.
 * SURFACE_TYPE_PUSH_BUFFERS 타입의 SurfaceView 에는 그리기를 할 수 없는 모양이다.
 * 그래서 같은 크기의 View를 SurfaceView 위에 얹었다.
 * 기존의 화면인식함수는 처리된 화면만 내보내기때문에 프레임 수가 매우 작다.
 * 화면이 뚝뚝 끊기면서 움직이는데....
 * 내가 만든건 SurfaceView 는 그냥 두고 처리가 끝나는 즉시 찾은 얼굴에 표시를 하기때문에...
 * 프레임이 빠르다. 찾은 결과가 나오기전에는 그냥 그자리에 표시가 되어있을뿐이라서...
 * 화면의 움직임에 비해 반응이 느리다. 얼굴 표시가 화면을 따라다닌다고 해야하나...
 *
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyView extends View {
    public MyView(Context context) { super(context); init(); }
    public MyView(Context context, AttributeSet attrs) { super(context, attrs); init(); }
    public MyView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); init(); }

    private FaceDetector myFaceDetector;  // 디텍트 함수.
    private FaceDetector.Face[] detectedFace; // 찾은 얼굴 저장 배열
    private PointF faceMidPoint; // 얼굴 중앙을 찾기위해 쓰는 포인트 클래스.
    private Face tempFace; // 얼굴 그리기를 위해 쓰는 임시 변수.

    private int maxfaces = 5; // 찾을 얼굴 숫자. 100 개 이상 안찾는다.
    float eyes_distance; // 눈사이 간격
    int number_of_faces; // 찾은 얼굴 숫자.

    public PreviewData myData; // 이미지를 가져올 클래스.
    private Paint pnt; // 얼굴을 표시하기 위한 설정.

    private boolean proccessFlag = false; // 그리는 중에는 onDraw 호출을 무시.

    //==========================================================
    //사람이 앞에 있는지 없는지 확인하는 변수
    //1은 하나라도 생기면 바로 캐치, 0이 5개 연속될경우 얼굴이 사라진 것으로 간주
    //값이 5이상이면 얼굴이 없어진것.

    int no_Face=0; //카운트
    boolean face_exist = false; //얼굴이 존재?
    boolean ready_mode = true; //대기상태(false면 깝치다가 잠시 멈춘거)
    //==========================================================

    // 초기화.
    private void init(){
        myData = MainActivity.myData;

        pnt = new Paint();
        pnt.setColor(0xFFff0000);
        pnt.setStyle(Style.STROKE);
        pnt.setStrokeWidth(3);
        pnt.setAntiAlias(true);

        myFaceDetector = new FaceDetector(MainActivity.SCREEN_WIDTH, MainActivity.SCREEN_HEIGHT, maxfaces); //
        detectedFace = new FaceDetector.Face[maxfaces];
        faceMidPoint = new PointF(); // 얼굴 중앙을 찾기위해 쓰는넘.
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 데이터가 있고 처리중이 아닐 경우만...
        if(myData != null && myData.renewFlag && !proccessFlag){
            proccessFlag = true; // 계산 시작 표시.
            number_of_faces = myFaceDetector.findFaces(myData.getbitmap(), detectedFace); // 얼굴 찾아서 저장.

            //======================================================================================
            //얼굴이 앞에 있는지 없는지를 체크하고 값을 어플리케이션에 전송
            //
            // faceState : 0:대기모드(정지)  1:얼굴최초발견(인사?하기)  2:얼굴재발견(일시정지)  3:움직
            //======================================================================================


            //Log.d("SSSS", "얼굴의 수:" + number_of_faces);

            //최초얼굴인식
            if(face_exist==false && ready_mode==true && number_of_faces > 0){
                Log.d("SSSS", "최초의 얼굴인식(대기모드에서)");
                face_exist = true; //얼굴이 존재
                ready_mode = false; //대기모드 해제
                CustomApplication.faceState = 1;
            }
            //얼굴을 재인식
            else if(face_exist==false && ready_mode==false && number_of_faces > 0){
                Log.d("SSSS", "까불다가 들킴 [일시정지]");
                face_exist = true; //얼굴이 존재
                CustomApplication.faceState = 2;
            }

            //카운트
            if (number_of_faces == 0) { no_Face++; }
            else if(number_of_faces > 0){ no_Face=0; }


            if(face_exist==true && no_Face>15){
                face_exist = false;
                Log.d("SSSS", "얼굴이 있다가 사라짐 깝치기 시작 [움직임모드]");
                CustomApplication.faceState = 3;
            }
            else if(face_exist ==false && ready_mode ==false && no_Face>200) {
                Log.d("SSSS", "얼굴이 아예 사라져버림. 대기모드 전환");
                ready_mode=true;
                CustomApplication.faceState = 0;
            }
            else if(no_Face == 0){
                //Log.d("SSSS", "얼굴이 앞에있는 상태");
            }



            /* 사각형을 그려주는 부분인데 이 프로젝트에서는 인식만 하면 되므로 필요없다.
            for(int i=0; i < number_of_faces; i++){
                tempFace = detectedFace[i];
                tempFace.getMidPoint(faceMidPoint);
                eyes_distance = tempFace.eyesDistance();

                canvas.drawRect(
                        (int)(faceMidPoint.x - eyes_distance),
                        (int)(faceMidPoint.y - eyes_distance),
                        (int)(faceMidPoint.x + eyes_distance),
                        (int)(faceMidPoint.y + eyes_distance),
                        pnt);
            }
            */
            proccessFlag = false;
        }else{ // 그리기할 경우가 아니면 있는거만 갱신.

            super.onDraw(canvas);
        }
        this.invalidate(); // 무한 화면 갱신. onDraw 를 무한으로 부른다. 한마디로 무한루프다. 나갈 방법은 없다.
    }
}
