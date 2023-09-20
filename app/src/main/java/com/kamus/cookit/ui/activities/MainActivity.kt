package com.kamus.cookit.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.fragment.app.Fragment
import com.kamus.cookit.R
import com.kamus.cookit.databinding.ActivityMainBinding
import com.kamus.cookit.ui.fragments.AccountFragment
import com.kamus.cookit.ui.fragments.HomeFragment
import com.kamus.cookit.ui.fragments.SearchFragment
import java.util.Objects

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Objects.requireNonNull(supportActionBar)?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.mipmap.ic_cookit)

        binding.bottomMenu.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.btnHome -> {
                    replaceFragment(HomeFragment.newInstance())
                    true
                }
                R.id.btnSearch -> {
                    replaceFragment(SearchFragment.newInstance())
                    true
                }
                R.id.btnAccount ->{
                    replaceFragment(AccountFragment.newInstance())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment.newInstance())
    }


    
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }
}