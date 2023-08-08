package com.example.frontend.main

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.frontend.member.LoginActivity
import com.example.frontend.R
import com.example.frontend.databinding.ActivityItemBinding
import com.example.frontend.member.SignupActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class ItemActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    lateinit var toggle: ActionBarDrawerToggle  // 메뉴
    lateinit var binding: ActivityItemBinding
    val serviceKey =
        "Jo/F8Pswa2Ul50H9F2/iWeQFCrrF2CuVqL+0cEJJVXlLPLQ0TCqZta52lfANIq63d6lc/4VTIeQoIYEFR84pDQ=="
    val resultType = "json"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //====================토글 메뉴==========================
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            R.string.drawer_opened,
            R.string.drawer_closed
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        binding.mainDrawerView.setNavigationItemSelectedListener {
            if(it.itemId == R.id.joinmenu){
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
            }else if(it.itemId == R.id.login){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            true
        }
        //====================토글 메뉴==========================
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // 값을 받기 위해 Intent 가져오기
        val intent = intent

        // 전달된 값 가져오기
        val id = intent.getStringExtra("id")
        val TITLE = intent.getStringExtra("TITLE")
        val CNTCT_TEL = intent.getStringExtra("CNTCT_TEL")


        binding.id.text = id
        binding.TITLE.text = TITLE
        binding.CNTCTTEL.text = CNTCT_TEL
        val img = intent.getStringExtra("MAIN_IMG_NORMAL")
        Glide.with(this)
            .asBitmap()
            .load(img)
            .into(object : CustomTarget<Bitmap>(200, 200) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    binding.imgpath.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }
            })

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        val intent = intent
        val LAT = intent.getStringExtra("LAT")?.toDoubleOrNull() // String을 Double로 변환
        val LNG = intent.getStringExtra("LNG")?.toDoubleOrNull()

        if (LAT != null && LNG != null) {
            Log.d("joj", LAT.toString())
            Log.d("joj", LNG.toString())
            mMap = googleMap

            // Add a marker at the specified location and move the camera
            val location = LatLng(LAT, LNG)
            mMap.addMarker(MarkerOptions().position(location).title("Marker"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20f))
        } else {
            Log.d("joj", "LAT or LNG is null.")
        }
    }
}