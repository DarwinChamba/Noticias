package app.news.newsapp4.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import app.news.newsapp4.R
import app.news.newsapp4.databinding.ActivityMainBinding
import app.news.newsapp4.ui.viewmodel.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var model:ArticleViewModel
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.bar)
        model=ViewModelProvider(this).get(ArticleViewModel::class.java)


    }

    override fun onStart() {
        super.onStart()
        navController=findNavController(R.id.fragmentContainer)
        NavigationUI.setupWithNavController(binding.bottomView,navController)
        NavigationUI.setupActionBarWithNavController(this,navController)
    }
}