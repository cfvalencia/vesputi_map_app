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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.vesputichallengeapp.R
import com.example.vesputichallengeapp.databinding.MapFragmentViewBinding
import com.example.vesputichallengeapp.util.listOfFeaturesToGeometry
import com.example.vesputichallengeapp.util.locationsToFeatures
import com.example.vesputichallengeapp.viewmodels.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.*
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource


class MapFragment : Fragment(),MapboxMap.OnMapClickListener {

    private val SOURCE_ID = "SOURCE_ID"
    private val ICON_ID = "ICON_ID"
    private val LAYER_ID = "LAYER_ID"

    private val viewModel : MapViewModel by lazy {
         val activity = requireNotNull(this.activity){
        }
        ViewModelProvider(this,MapViewModel.Factory(activity.application)).get(MapViewModel::class.java)
    }

    private var mapView: MapView? = null
    private lateinit var bottomViewBehavior: BottomSheetBehavior<LinearLayoutCompat>
    private lateinit var mapboxMap : MapboxMap

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

        viewModel.lineFeatureList.observe(viewLifecycleOwner, Observer { features ->
            features?.apply {

                val featureCollection =
                    FeatureCollection.fromFeatures(
                        arrayOf(Feature.fromGeometry(
                            listOfFeaturesToGeometry(this))))

                mapView?.getMapAsync(OnMapReadyCallback { map ->
                    map.setStyle(Style.MAPBOX_STREETS,  Style.OnStyleLoaded(){ style ->

                        if (featureCollection.features() != null) {

                            if (featureCollection.features()!!.size > 0) {

                                style.addSource(GeoJsonSource("line-source", featureCollection));

                                // The layer properties for our line. This is where we make the line dotted, set the
                                // color, etc.
                                style.addLayer( LineLayer("linelayer", "line-source")
                                    .withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                                        PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                                        PropertyFactory.lineOpacity(.7f),
                                        PropertyFactory.lineWidth(2f),
                                        PropertyFactory.lineColor(Color.parseColor("#ed0707"))));
                            }
                        }

                    })
                })
            }
        })



//        viewModel.locationsList.observe(viewLifecycleOwner, Observer { locationList ->
//            locationList?.apply {
//                val listFromObserver = locationList
//                val multiPoint = MultiPoint.fromLngLats(
//                    listOf(
//                    Point.fromLngLat(
//                    -122.63748,
//                    45.52214),
//                    Point.fromLngLat(
//                        -122.64855,
//                        45.52218),
//                    Point.fromLngLat(
//                        -122.65497,
//                        45.52196),
//                    Point.fromLngLat(
//                        -122.65631,
//                        45.52104),
//                    Point.fromLngLat(
//                        -122.65867,
//                        45.51848),
//                    Point.fromLngLat(
//                        -122.65872,
//                        45.51293),
//                    Point.fromLngLat(
//                        -122.65872,
//                        45.51293),
//                    Point.fromLngLat(
//                        -122.66576,
//                        45.51295),
//                    Point.fromLngLat(
//                        -122.66745,
//                        45.51252),
//                    Point.fromLngLat(
//                        -122.66813,
//                        45.51244),
//                    Point.fromLngLat(
//                        -122.67359,
//                        45.51385),
//                    Point.fromLngLat(
//                        -122.67415,
//                        45.51406),
//                    Point.fromLngLat(
//                        -122.67481,
//                        45.51484),
//                    Point.fromLngLat(
//                        -122.676,
//                        45.51532),
//                    Point.fromLngLat(
//                        -122.68106,
//                        45.51668)
//
//                    )
//                )
//                val featureCollection =
//                    FeatureCollection.fromFeatures(
//                        arrayOf(
//                            Feature.fromGeometry(multiPoint)
//                        ))
//                mapView?.getMapAsync(OnMapReadyCallback { map ->
//
////                    mapboxMap.setStyle(Style.MAPBOX_STREETS,  Style.OnStyleLoaded() { style ->
////                        style.addSource( GeoJsonSource("line-source",
////                            FeatureCollection.fromFeatures(locationsToFeatures(listFromObserver))))
////
////                        // The layer properties for our line. This is where we make the line dotted, set the
////                        // color, etc.
////                        style.addLayer( LineLayer("linelayer", "line-source")
////                            .withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
////                                PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
////                                PropertyFactory.lineOpacity(.7f),
////                                PropertyFactory.lineWidth(7f),
////                                PropertyFactory.lineColor(Color.parseColor("#3bb2d0"))));
////                    })
////
//                    map.setStyle(Style.MAPBOX_STREETS,  Style.OnStyleLoaded(){ style ->
//
//                        Log.e("TEST","TRY")
//                        if (featureCollection.features() != null) {
//                            Log.e("TEST","FIRST PASS")
//
//                            if (featureCollection.features()!!.size > 0) {
//
//                                style.addSource(GeoJsonSource("line-source", featureCollection));
//
//                                // The layer properties for our line. This is where we make the line dotted, set the
//                                // color, etc.
//                                style.addLayer( LineLayer("linelayer", "line-source")
//                                    .withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
//                                        PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
//                                        PropertyFactory.lineOpacity(.7f),
//                                        PropertyFactory.lineWidth(7f),
//                                        PropertyFactory.lineColor(Color.parseColor("#3bb2d0"))));
//                            }
//                        }
//
//                    })
//
//
////                        Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
////                            .withImage(BLUE_PERSON_ICON_ID, BitmapFactory.decodeResource(
////                                resources,R.drawable.mapbox_compass_icon
////                            ))
////                            .withImage(BLUE_PIN_ICON_ID, BitmapFactory.decodeResource(
////                                resources,R.drawable.mapbox_logo_icon
////                            ))
////                            .withSource(
////                                GeoJsonSource(SOURCE_ID,
////                                    FeatureCollection.fromFeatures(locationsToFeatures(listFromObserver)))
////                            ), Style.OnStyleLoaded { style ->
////                            val singleLayer =  SymbolLayer("symbol-layer-id", SOURCE_ID);
////                            singleLayer.setProperties(
////                                PropertyFactory.iconImage(step(zoom(), literal(BLUE_PERSON_ICON_ID),
////                                    stop(ZOOM_LEVEL_FOR_SWITCH, BLUE_PIN_ICON_ID))),
////                                PropertyFactory.iconIgnorePlacement(true),
////                                PropertyFactory.iconAllowOverlap(true))
////                            style.addLayer(singleLayer)
//
//
////                        }
////                    )
////                    map.addOnMapClickListener(this@MapFragment)
////                    mapboxMap = map
//               })
//           }
//        })
//
//
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



}