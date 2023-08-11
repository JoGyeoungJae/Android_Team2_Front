package com.example.frontend.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.frontend.R
import com.example.frontend.member.LoginActivity
import com.example.frontend.member.SignupActivity
import com.example.frontend.databinding.ActivityMainBinding
import com.example.frontend.restaurant.AddRestaurantActivity


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding       ///1kjkjajskjdfkjsdkjf
    lateinit var toggle: ActionBarDrawerToggle  // 메뉴11


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
            }else if(it.itemId == R.id.addrestaurant){
                val intent = Intent(this, AddRestaurantActivity::class.java)
                startActivity(intent)
            }
            true
        }
        //====================토글 메뉴==========================
        /*뷰페이저 2 구현 - 프레그먼트 방식으로 연결 */
        val adapter= MyFragmentPagerAdapter(this)
        binding.viewpager.adapter = adapter


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    class MyFragmentPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity){
        val fragments: List<Fragment>
        init {
            fragments= listOf(MainOneFragment(), MainTwoFragment())
            Log.d("kkang" ,"fragments size : ${fragments.size}")
        }
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }

}



