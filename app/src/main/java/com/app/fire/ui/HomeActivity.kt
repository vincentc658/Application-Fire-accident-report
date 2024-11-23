package com.app.fire.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.fire.R
import com.app.fire.databinding.ActivityHomeBinding
import com.app.fire.model.NotificationItem
import com.app.fire.ui.fragment.ListAccidentFragment
import com.app.fire.ui.fragment.ListLogistikPlanFragment
import com.app.fire.ui.fragment.ListOrganizationFragment
import com.app.fire.ui.fragment.ListStockFragment
import com.app.fire.util.BaseView
import com.app.fire.util.FirestoreUtil
import com.app.fire.util.SessionManager
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : BaseView() {
    private lateinit var binding: ActivityHomeBinding
    private val notifications = mutableListOf<NotificationItem>()
    private var roomId: String? = null
    private lateinit  var badge : TextView
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
                R.id.nav_accident_list -> {
                    loadFragment(ListAccidentFragment())
                    supportActionBar?.title = "Accidents"
                }

                R.id.nav_organization_list -> {
                    supportActionBar?.title = "Organizations"
                    loadFragment(ListOrganizationFragment())
                }

                R.id.nav_stock_distribution -> {
                    supportActionBar?.title = "Stock"
                    loadFragment(ListStockFragment())
                }

                R.id.nav_distribution_plan -> {
                    supportActionBar?.title = "Distribution"
                    loadFragment(ListLogistikPlanFragment())
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
        val menuItem = menu?.findItem(R.id.action_notifications)

        // Inflate custom view for badge
        val actionView = layoutInflater.inflate(R.layout.menu_notification_badge, null)
        menuItem?.actionView = actionView

        // Find badge TextView and set count
        badge = actionView.findViewById<TextView>(R.id.badge)
//        badge.visibility = View.VISIBLE // Show the badge
//        badge.text = "5" // Update with your notification count

        // Handle clicks on the menu item
        actionView.setOnClickListener {
            onOptionsItemSelected(menuItem!!)
        }
        loadChatData()
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
                if (SessionManager.getTypeUser(this) == 1) {
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

    private fun getNotification() {
        FirebaseFirestore.getInstance().collection("notifications")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.forEach { document ->
                        val senderId = document.data["senderId"].toString()
                        val roomId = document.data["roomId"].toString()
                        if (senderId != SessionManager.getId(this)) {
                            val notificationItem = NotificationItem(
                                message = document.data["title"].toString(),
                                title = document.data["message"].toString(),
                                timestamp = document.data["timestamp"].toString(),
                                roomId = document.data["roomId"].toString(),
                                senderId = document.data["senderId"].toString(),
                                isRead = (document.data["isRead"] ?: "false").toString()
                                    .toBoolean(),
                                icon = R.drawable.ic_notifications
                            )
                            notificationItem.id = document.id
                            if (SessionManager.getTypeUser(this) == 2) { // admin
                                notifications.add(notificationItem)
                            } else {
                                if (this.roomId == roomId) {
                                    notifications.add(notificationItem)
                                }
                            }
                        }
                    }
                    if(notifications.filter { !it.isRead }.isNotEmpty()){
                        badge.visibility = View.VISIBLE
                        // Show the badge
                        badge.text = notifications.filter { !it.isRead }.size.toString()
                    }else{
                        badge.visibility = View.GONE
                    }
                }
            }
    }

    private fun loadChatData() {
        notifications.clear()
        if (SessionManager.getTypeUser(this) == 2) { // admin
            getNotification()
            return
        }
        FirestoreUtil.findRoomByCriteria(
            field = "senderId",
            value = SessionManager.getId(this),
            onSuccess = { roomIds ->
                roomIds.firstOrNull()?.let { id ->
                    roomId = id
                    getNotification()
                }
            },
            onFailure = { e ->
                Log.e("FirestoreError", "Failed to find rooms: ${e.message}")
            }
        )
    }
}