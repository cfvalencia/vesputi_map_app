package com.example.vesputichallengeapp.ui

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.whenCreated
import com.example.vesputichallengeapp.R
import com.example.vesputichallengeapp.databinding.MapFragmentViewBinding
import com.example.vesputichallengeapp.util.locationsToFeatures
import com.example.vesputichallengeapp.viewmodels.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource


class MapFragment : Fragment(),MapboxMap.OnMapClickListener,MapboxMap.OnMoveListener{

    private val SOURCE_ID = "SOURCE_ID"
    private val ICON_ID = "ICON_ID"
    private val LAYER_ID = "LAYER_ID"

    private val viewModel : MapViewModel by lazy {
         val activity = requireNotNull(this.activity){
        }
        ViewModelProvider(this,MapViewModel.Factory(activity.application)).get(MapViewModel::class.java)
    }

    private var mapView: MapView? = null
    private var mapViewTwo: MapView? = null
    private lateinit var bottomViewBehavior: BottomSheetBehavior<LinearLayoutCompat>
    private lateinit var mapboxMap : MapboxMap
    private lateinit var mapboxMapTwo : MapboxMap


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.context?.let { Mapbox.getInstance(it,getString(R.string.mapbox_access_token)) }

        val binding = MapFragmentViewBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        bottomViewBehavior = BottomSheetBehavior.from(binding.bottomSheetView)
        bottomViewBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomViewBehavior.halfExpandedRatio = 0.1f


        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)

        mapViewTwo = binding.mapViewTwo
        mapViewTwo?.onCreate(savedInstanceState)




        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clickedOnMarker.observe(viewLifecycleOwner, Observer {
            it?.apply {
                when(bottomViewBehavior.state){
                    BottomSheetBehavior.STATE_HIDDEN -> bottomViewBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> bottomViewBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    BottomSheetBehavior.STATE_EXPANDED -> bottomViewBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    else -> bottomViewBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }

            }
        })


        viewModel.locationsList.observe(viewLifecycleOwner, Observer { locationList ->
            locationList?.apply {
                val listFromObserver = locationList
                mapView?.getMapAsync(OnMapReadyCallback { map ->

                    map.setStyle(
                        Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                            .withImage(ICON_ID, BitmapFactory.decodeResource(
                                resources,R.drawable.mapbox_compass_icon
                            ))
                            .withSource(
                                GeoJsonSource(SOURCE_ID,
                                    FeatureCollection.fromFeatures(locationsToFeatures(listFromObserver)))
                            )
                            .withLayer(
                                SymbolLayer(LAYER_ID,SOURCE_ID)
                                    .withProperties(
                                        PropertyFactory.iconImage(ICON_ID),
                                        PropertyFactory.iconAllowOverlap(true),
                                        PropertyFactory.iconIgnorePlacement(true)
                                    )
                            ), Style.OnStyleLoaded {
//                                style ->
//                            style.addLayer(
//                                LineLayer("linelayer", SOURCE_ID)
//                                    .withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
//                                        PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
//                                        PropertyFactory.lineOpacity(.7f),
//                                        PropertyFactory.lineWidth(7f),
//                                        PropertyFactory.lineColor(
//                                            Color.parseColor("#3bb2d0")))
//                            )
                        }
                    )
                    map.addOnMapClickListener(this@MapFragment)
                    mapboxMap = map
                    map.addOnMoveListener(this@MapFragment)
                })
                mapViewTwo?.getMapAsync(OnMapReadyCallback { mapTwo ->

                    mapTwo.setStyle(
                        Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                            .withImage(ICON_ID, BitmapFactory.decodeResource(
                                resources,R.drawable.mapbox_logo_icon
                            ))
                            .withSource(
                                GeoJsonSource(SOURCE_ID,
                                    FeatureCollection.fromFeatures(locationsToFeatures(listFromObserver)))
                            )
                            .withLayer(
                                SymbolLayer(LAYER_ID,SOURCE_ID)
                                    .withProperties(
                                        PropertyFactory.iconImage(ICON_ID),
                                        PropertyFactory.iconAllowOverlap(true),
                                        PropertyFactory.iconIgnorePlacement(true)
                                    )
                            ), Style.OnStyleLoaded {
                        }
                    )
                    mapTwo.addOnMapClickListener(this@MapFragment)
                    mapboxMapTwo = mapTwo
                    mapTwo.addOnMoveListener(this@MapFragment)
                })
            }
        })


    }




    override fun onMapClick(point: LatLng): Boolean {
        // Convert LatLng coordinates to screen pixel and only query the rendered features.
        val pixel: PointF = mapboxMap.getProjection().toScreenLocation(point)


        val features: List<Feature> = mapboxMap.queryRenderedFeatures(pixel)

        if (features.size > 0) {
            val feature = features[0]
            Log.e("TAG", String.format("ID = %s", feature.properties()!!.get("id")))
            if(feature.properties()!!.get("id") == null){
                bottomViewBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }else{
                viewModel.getLocationWithId(feature.properties()!!)
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView!!.onDestroy()
    }

    override fun onMoveBegin(detector: MoveGestureDetector) {
        Log.e("ZOOM",mapboxMap.cameraPosition.zoom.toString())
        Log.e("ZOOM",mapboxMapTwo.cameraPosition.zoom.toString())
    }

    override fun onMove(detector: MoveGestureDetector) {
        Log.e("ZOOM",mapboxMap.cameraPosition.zoom.toString())
        Log.e("ZOOM",mapboxMapTwo.cameraPosition.zoom.toString())    }

    override fun onMoveEnd(detector: MoveGestureDetector) {
        Log.e("ZOOM",mapboxMap.cameraPosition.zoom.toString())
        Log.e("ZOOM",mapboxMapTwo.cameraPosition.zoom.toString())
        var zoomMapOne = mapboxMap.cameraPosition.zoom
        var zoomMapTwo = mapboxMapTwo.cameraPosition.zoom

        if(zoomMapTwo>8) {
            mapView?.visibility = View.VISIBLE
            mapViewTwo?.visibility = View.GONE
            val position = mapboxMapTwo.cameraPosition.target
            mapboxMap.cameraPosition = CameraPosition.Builder().target(position).zoom(7.9).build()
        }
//        if (zoomMapOne < 8){
//            mapView?.visibility = View.GONE
//            mapViewTwo?.visibility = View.VISIBLE
//            val position = mapboxMap.cameraPosition.target
//            mapboxMapTwo.cameraPosition = CameraPosition.Builder().target(position).zoom(8.1).build()
//        }
    }

    private fun manageOneMapViews() {
        if(mapViewTwo?.isVisible?.equals(true)!!){
            Log.e("ZOOM","MAP ONE ENTER")
            val position = mapboxMapTwo.cameraPosition.target
            mapboxMap.cameraPosition = CameraPosition.Builder().target(position).zoom(7.9).build()
        }
    }

    private fun manageMapTwoVisible() {
        if(mapView?.isVisible?.equals(true)!!){
            Log.e("ZOOM","MAP TWO ENTER")
            val position = mapboxMap.cameraPosition.target
            mapboxMapTwo.cameraPosition = CameraPosition.Builder().target(position).zoom(8.1).build()
        }
    }


}