package com.app.fire.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.app.fire.R
import com.app.fire.databinding.ActivityHomeBinding
import com.app.fire.ui.fragment.ListAccidentFragment
import com.app.fire.ui.fragment.ListOrganizationFragment
import com.app.fire.ui.fragment.ListStockFragment
import com.app.fire.util.BaseView
import com.app.fire.util.SessionManager

class HomeActivity : BaseView() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userType = SessionManager.getTypeUser(this)
        setSupportActionBar(binding.toolbar)
        // Dynamically set menu based on user type
        binding.bottomNavigation.menu.clear()
        when (userType) {
            2 -> {
                binding.bottomNavigation.inflateMenu(R.menu.menu_admin)
            }

            1 -> {
                binding.bottomNavigation.inflateMenu(R.menu.menu_general_user)
            }

            else -> {
                binding.bottomNavigation.inflateMenu(R.menu.menu_visitor)
            }
        }

        loadFragment(ListAccidentFragment())
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_accident_list ->{
                    loadFragment(ListAccidentFragment())
                    supportActionBar?.title="Accidents"
                }
                R.id.nav_organization_list -> {
                    supportActionBar?.title="Organizations"
                    loadFragment(ListOrganizationFragment())
                }
                R.id.nav_stock_distribution ->{
                    supportActionBar?.title="Stock"
                    loadFragment(ListStockFragment())
                }
                else -> false
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_notifications -> {
                goToPage(NotificationActivity::class.java)
                // Handle notification menu click
                return true
            }
            R.id.action_logout -> {
                SessionManager.clearData(this)
                goToPageAndClearPrevious(LoginActivity::class.java)
                // Handle notification menu click
                return true
            }

            R.id.action_chat -> {
                if(SessionManager.getTypeUser(this)==1) {
                    goToPage(ListChatActivity::class.java)
                    return true
                }
                goToPage(ListRoomChat::class.java)
                // Handle chat menu click
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}