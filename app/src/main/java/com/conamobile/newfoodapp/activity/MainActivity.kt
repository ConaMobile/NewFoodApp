package com.conamobile.newfoodapp.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.conamobile.newfoodapp.R
import com.conamobile.newfoodapp.adapter.MenuAdapter
import com.conamobile.newfoodapp.databinding.ActivityMainBinding
import com.conamobile.newfoodapp.fragments.HomeFragment
import com.conamobile.newfoodapp.fragments.OrdersFragment
import com.conamobile.newfoodapp.utils.draw.widgets.DuoDrawerToggle

class MainActivity : AppCompatActivity() {
    private var mMenuAdapter: MenuAdapter? = null
    private lateinit var binding: ActivityMainBinding
    private var mTitles = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        hideStatusBarAndBottomBar()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        installNavigation()
        installView()
        menuManager()
    }

    @SuppressLint("ResourceAsColor")
    private fun menuManager() {
        binding.apply {
            menuView.accountBtn.setOnClickListener {
                toolBar.setBackgroundColor(ContextCompat.getColor(this@MainActivity,
                    R.color.default_gray))
                false.goToFragment(HomeFragment())
                drawerLayout.closeDrawer()
            }
            menuView.orderBtn.setOnClickListener {
                toolBar.setBackgroundColor(ContextCompat.getColor(this@MainActivity,
                    R.color.default_gray_full))
                false.goToFragment(OrdersFragment())
                drawerLayout.closeDrawer()
            }
        }
    }

    private fun installNavigation() {
        binding.apply {
            val drawerToggle = DuoDrawerToggle(
                this@MainActivity, drawerLayout, toolBar,
                R.string.open,
                R.string.close
            )

            drawerLayout.setDrawerListener(drawerToggle)
            drawerToggle.syncState()
        }
    }

    private fun installView() {
        binding.apply {
            toolBar.setNavigationIcon(R.drawable.ic_menu)
            false.goToFragment(HomeFragment())
        }
    }

    private fun Boolean.goToFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (this) {
            transaction.addToBackStack(null)
        }
        transaction.add(R.id.container, fragment).commit()
    }

    private fun String.toaster() {
        Toast.makeText(this@MainActivity, this, Toast.LENGTH_SHORT).show()
    }

    private fun hideStatusBarAndBottomBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}