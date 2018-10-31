package com.lipnus.kum_map;
// (중요) 패키지명은 애플리케이션 설정의 Android 패키지명과 반드시 일치해야 함

import android.os.Bundle;
import android.util.Log;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

public class MainActivity_backup extends NMapActivity  implements NMapView.OnMapStateChangeListener {

    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "982LkWj_x1NChbfsS7DJ";// 애플리케이션 클라이언트 아이디 값

    NMapViewerResourceProvider mMapViewerResourceProvider;
    NMapOverlayManager mOverlayManager;
    NMapController mMapController;

    NMapView MM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create map view
        mMapView = new NMapView(this);


        // set Client ID for Open MapViewer Library
        mMapView.setClientId(CLIENT_ID);

        //확대가 많이 되도록 설정
        mMapView.setScalingFactor(3.0f);

        //네이버맵의 리스너 설정인듯..
        mMapView.setOnMapStateChangeListener(this);

        // set the activity content to the map view
        setContentView(mMapView);

        // initialize map view
        mMapView.setClickable(true);

        // create resource provider
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

        // create overlay manager
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);


        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mMapView.getMapController();






        //지도에 표시하는 부분

        int markerId = NMapPOIflagType.PIN;

        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(0);
        poiData.addPOIitem(127.0214863, 37.5902947, "황선필하우스", markerId, 0);
        poiData.endPOIdata();

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

        // show all POI data
        poiDataOverlay.showAllPOIdata(0);






        NMapPlacemark aa = new NMapPlacemark();
        aa.toString();
    }

    @Override
    public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
        //지도의 초기위치를 설정
        mMapController.setMapCenter(new NGeoPoint(127.0214863, 37.5902947), 13);
    }

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {

    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }

    public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errorInfo) {

        if (errorInfo != null) {
            Log.e("NaverMAP", "Failed to findPlacemarkAtLocation: error=" + errorInfo.toString());
            return;
        }

        Log.i("NaverMAP", "onReverseGeocoderResponse: placeMark=" + placeMark.toString());
    }

}