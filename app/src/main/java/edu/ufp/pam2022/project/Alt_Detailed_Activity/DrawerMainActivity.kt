package edu.ufp.pam2022.project.Alt_Detailed_Activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.ufp.pam2022.project.R
import edu.ufp.pam2022.project.databinding.ActivityDrawerMainBinding
import edu.ufp.pam2022.project.library.User
import edu.ufp.pam2022.project.listMovie.ScrollingProjectActivityViewModel
import edu.ufp.pam2022.project.listMovie.ScrollingProjectActivityViewModelFactory
import edu.ufp.pam2022.project.main.login.ui.login.LoginMainActivity

class DrawerMainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDrawerMainBinding
    private lateinit var user : User
    lateinit var scrollingProjectActivityViewModel : DrawerMainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDrawerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarDrawerMain.toolbar)

        user= User(intent.getIntExtra("UserId",0),
            "Username",
            "Email")
        if(user.UserId==0){
            val intent = Intent(this@DrawerMainActivity, LoginMainActivity::class.java)
            intent.putExtra("EXIT", false)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        scrollingProjectActivityViewModel = ViewModelProvider(this, DrawerMainViewModelFactory(this))[DrawerMainViewModel::class.java]
        scrollingProjectActivityViewModel.Rest_Table_Users(user)

//        binding.appBarDrawerMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_drawer_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_drawer_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}