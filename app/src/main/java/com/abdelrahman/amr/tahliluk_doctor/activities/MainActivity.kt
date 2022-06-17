package com.abdelrahman.amr.tahliluk_doctor.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.abdelrahman.amr.tahliluk_doctor.R
import com.abdelrahman.amr.tahliluk_doctor.databinding.ActivityMainBinding
import com.abdelrahman.amr.tahliluk_doctor.fragments.InProgressReservationsFragment
import com.abdelrahman.amr.tahliluk_doctor.fragments.ReservationsFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation

class MainActivity : AppCompatActivity() {
    private lateinit var mActivityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mActivityMainBinding.root)

        setListeners()
        mActivityMainBinding.bottomNavigation.show(1, false)
        setScreenElements()
    }



    private fun setListeners() {
        mActivityMainBinding.bottomNavigation.setOnShowListener {
            var fragment: Fragment? = null
            when (it.id) {
                1 -> {
                    fragment = ReservationsFragment()
                }
                2 -> {
                    fragment = InProgressReservationsFragment()
                }

            }
            if (fragment != null) {
                replaceFragment(fragment)
            }
        }
        mActivityMainBinding.bottomNavigation.setOnClickMenuListener {
            //SupportClass.showToast(this,"click ${it.id}")
        }
        mActivityMainBinding.bottomNavigation.setOnReselectListener {
            return@setOnReselectListener
        }
    }
    private fun setScreenElements() {
        mActivityMainBinding.bottomNavigation.add(
            MeowBottomNavigation.Model(
                1,
                R.drawable.ic_reservations
            )
        )
        mActivityMainBinding.bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_completed_reservations))

    }




    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = this.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.fui_slide_in_right,
            R.anim.fragmentanimation,
            R.anim.fui_slide_in_right,
            R.anim.fragmentanimation
        )
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.isAddToBackStackAllowed
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        replaceFragment(ReservationsFragment.newInstance())
    }
}