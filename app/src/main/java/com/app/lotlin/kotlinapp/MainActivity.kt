package com.app.lotlin.kotlinapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.app.lotlin.kotlinapp.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val rv = findViewById<RecyclerView>(R.id.recyclerView)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        fab.setOnClickListener { view ->
            val repository = SearchRepositoryProvider.provideSearchRepository()

            var add = compositeDisposable.add(
                    repository.searchUsers("Lagos", "Java")
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                Log.d("Result", "There are ${result.items.size} Java developers in Lagos")


                                val users = ArrayList<User>()

                                users.add(User(
                                        result.items.get(0).login,
                                        result.items.get(0).id,
                                        result.items.get(0).url,
                                        result.items.get(0).html_url,
                                        result.items.get(0).followers_url,
                                        result.items.get(0).following_url,
                                        result.items.get(0).starred_url,
                                        result.items.get(0).gists_url,
                                        result.items.get(0).type,
                                        result.items.get(0).score))


                                var adapter = CustomAdapter(users, applicationContext)
                                rv.adapter = adapter
                                rv.adapter.notifyDataSetChanged()
                            }, { error ->
                                error.printStackTrace()
                            })
            )
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
