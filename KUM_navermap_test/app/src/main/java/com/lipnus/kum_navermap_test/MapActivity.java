package com.lipnus.kum_navermap_test;

import android.os.Bundle;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

public class MapActivity extends NMapActivity implements NMapView.OnMapStateChangeListener {

    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "982LkWj_x1NChbfsS7DJ";// 애플리케이션 클라이언트 아이디 값

    NMapController mMapController;
    NMapViewerResourceProvider mMapViewerResourceProvider;
    NMapOverlayManager mOverlayManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapView = (NMapView)findViewById(R.id.mapView);
        mMapView.setClientId(CLIENT_ID); // 클라이언트 아이디 값 설정
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();
        mMapView.setScalingFactor(3.0f); //확대가 많이 되도록 설정
        mMapView.setOnMapStateChangeListener(this); //리스너

        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mMapView.getMapController();

        // create resource provider
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

        // create overlay manager
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);




        //오버레이 마커표시

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
}
