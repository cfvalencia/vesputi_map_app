package com.example.vesputichallengeapp.ui

import android.graphics.BitmapFactory
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
import com.example.vesputichallengeapp.util.locationsToFeatures
import com.example.vesputichallengeapp.viewmodels.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource


class MapFragment : Fragment(),MapboxMap.OnMapClickListener{

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
                        }
                    )
                    map.addOnMapClickListener(this@MapFragment)
                    mapboxMap = map
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


}