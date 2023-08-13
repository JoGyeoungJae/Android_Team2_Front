package com.example.frontend.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.example.frontend.R
import com.example.frontend.member.LoginActivity
import com.example.frontend.member.SignupActivity
import com.example.frontend.databinding.ActivityMainBinding
import com.example.frontend.member.DeleteActivity
import com.example.frontend.member.ModifyActivity
import com.example.frontend.restaurant.AddRestaurantActivity



class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding       ///1kjkjajskjdfkjsdkjf12121212
    lateinit var toggle: ActionBarDrawerToggle  // 메뉴11






    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // SharedPreferences 객체생성=================저장된 값을 가져오기 위해=====================================
        val sharedPreferences = getSharedPreferences("logged_user", Context.MODE_PRIVATE)
        val uid = sharedPreferences.getString("uid", null)
        val uemail = sharedPreferences.getString("uemail", null)
        val upassword = sharedPreferences.getString("upassword", null)
        val uname = sharedPreferences.getString("uname", null)
        val unickname = sharedPreferences.getString("unickname", null)
        val uimg = sharedPreferences.getString("uimg", null)


//        Log.d("lys","uid : $uid")
//        Log.d("lys","uemail : $uemail")
//        Log.d("lys","upassword : $upassword")
//        Log.d("lys","uname : $uname")
//        Log.d("lys","unickname : $unickname")
//        Log.d("lys","uimg : $uimg")







        //====================토글 메뉴============================
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
            }else if(it.itemId == R.id.logout){

                //로그인할때 저장했던 객체 다시가져오기
                var logged = sharedPreferences.edit()

                //저장되어있는 값 null로 초기화
                logged.putString("uid",null)
                logged.putString("uemail",null)
                logged.putString("upassword",null)
                logged.putString("uname",null)
                logged.putString("unickname",null)
                logged.putString("uimg",null)

                // 변경 사항을 커밋하여 저장
                logged.apply()

                    Toast.makeText(this,"로그아웃 완료", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

            }
            else if(it.itemId == R.id.modify){
                val intent = Intent(this, ModifyActivity::class.java)
                startActivity(intent)
            }
            else if(it.itemId == R.id.delete){
                val intent = Intent(this, DeleteActivity::class.java)
                startActivity(intent)
            }

            else if(it.itemId == R.id.addrestaurant){
                val intent = Intent(this, AddRestaurantActivity::class.java)
                startActivity(intent)
            }
            true
        }
        //====================토글 메뉴==========================
        /*뷰페이저 2 구현 - 프레그먼트 방식으로 연결 */
        val adapter= MyFragmentPagerAdapter(this)
        binding.viewpager.adapter = adapter




        //쉐어드프리퍼런스의 값에따라 화면에 표시되도록하는 부분====================================================
        // 네비게이션 헤더의 TextView 찾기
        val headerView = binding.mainDrawerView.getHeaderView(0)

        // 네비게이션 메뉴 아이템 찾기
        val navigationMenu = binding.mainDrawerView.menu


        //헤더 안에있는 뷰 접근부분
        val userImageView = headerView.findViewById<ImageView>(R.id.userImageView)
        val loggedUserNickname = headerView.findViewById<TextView>(R.id.loggedUserNickname)
        val loggedUserEmail = headerView.findViewById<TextView>(R.id.loggedUserEmail)
        val requestLogin = headerView.findViewById<TextView>(R.id.requestLogin)


        if (uid!=null && uemail!=null && upassword!=null && uname!=null && unickname!=null && uimg!=null){

            requestLogin.visibility = View.GONE

            //프로필 이미지 설정
            if (uimg != null) {
                // 이미지가 있는 경우 Glide 등을 사용하여 이미지를 설정
                Glide.with(this)
                    .load(uimg)
                    .into(userImageView)
            } else {
                // 이미지가 없는 경우 기본 이미지 또는 처리를 해줄 수 있음
                userImageView.setImageResource(R.drawable.user_basic)
            }


            // 값을 TextView에 설정
            loggedUserNickname.text = "Nickname: $unickname"
            loggedUserEmail.text = "Email: $uemail"


            //쉐어드 프리퍼런스에 값이 null이 아닌경우 = 로그인된 경우            에만 ?
            //1. 회원가입, 로그인 버튼 안보이게
            //2. 로그아웃 버튼 보이게

                navigationMenu.findItem(R.id.joinmenu)?.isVisible = false
                navigationMenu.findItem(R.id.login)?.isVisible = false
                navigationMenu.findItem(R.id.logout)?.isVisible = true
            navigationMenu.findItem(R.id.modify)?.isVisible = true
            navigationMenu.findItem(R.id.delete)?.isVisible = true


        } else {

            userImageView.visibility = View.GONE
            loggedUserNickname.visibility = View.GONE
            loggedUserEmail.visibility = View.GONE




                navigationMenu.findItem(R.id.joinmenu)?.isVisible = true
                navigationMenu.findItem(R.id.login)?.isVisible = true
                navigationMenu.findItem(R.id.logout)?.isVisible = false
            navigationMenu.findItem(R.id.modify)?.isVisible = false
            navigationMenu.findItem(R.id.delete)?.isVisible = false

        }











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



