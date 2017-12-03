package com.example.kotlin.app

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.kotlin.app.databinding.ActivityMainBinding
import com.wuhenzhizao.adapter.RecyclerViewAdapter
import com.wuhenzhizao.adapter.RecyclerViewBindingAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        RecyclerViewAdapter(this, arrayListOf())
                .match<String>(R.layout.activity_main)
                .match<Any>(R.layout.content_main)
                .clickInterceptor { position, item, vh ->

                }
                .longClickInterceptor { position, item, vh ->

                }
                .viewHolderCreateInterceptor { vh ->

                }
                .viewHolderBindInterceptor { position, item, vh ->

                }

        RecyclerViewAdapter(this, arrayListOf())
                .layoutInterceptor { position, item ->
                    return@layoutInterceptor R.layout.content_main
                }
                .clickInterceptor { position, item, vh ->

                }
                .longClickInterceptor { position, item, vh ->

                }
                .viewHolderCreateInterceptor { vh ->

                }
                .viewHolderBindInterceptor { position, item, vh ->

                }

        RecyclerViewBindingAdapter(this, arrayListOf())
                .layoutInterceptor { position, item ->
                    return@layoutInterceptor R.layout.content_main
                }
                .clickInterceptor { position, item, vh ->

                }
                .longClickInterceptor { position, item, vh ->

                }
                .viewHolderCreateInterceptor { vh ->
                    var binding = vh.convert<ActivityMainBinding>()
                }
                .viewHolderBindInterceptor { position, item, vh ->

                }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
