package com.example.corona.ui.post.post

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.corona.R
import kotlinx.android.synthetic.main.post_fragment.*
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corona.ui.Util
import com.example.corona.ui.post.network.NetworkConnection
import com.example.corona.ui.post.adapter.ArticleAdapter
import com.example.corona.ui.post.entity.Article

import com.example.corona.ui.post.services.Service
import me.ibrahimsn.lib.SmoothBottomBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class post : Fragment()/*,FacebookListener*/ {


    companion object {
        fun newInstance() = post()


    }

    lateinit var toolbar: SmoothBottomBar

    //private lateinit var mFacebook:FacebookHelper

    private lateinit var viewModel: PostViewModel
    var articleList: MutableList<Article> = ArrayList()
    var ll: MutableList<Article> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //activity!!.bottom_bar.visibility = View.GONE

        return inflater.inflate(R.layout.post_fragment, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)


       /* val sante=activity!!.findViewById<FloatingActionButton>(R.id.sante)
        sante.setOnClickListener {

            val takenvideoAction =postDirections.actionPostFragmentToDiagnoseFragment()
            Navigation.findNavController(it).navigate(takenvideoAction)
        }*/

        toolbar = activity!!.findViewById(R.id.bottom_bar)
        toolbar.visibility=View.VISIBLE

        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        mtitel.text= getString(R.string.postTitle)
        tolb.visibility=View.VISIBLE
        val networkConnection=
            NetworkConnection(context!!)

        networkConnection.observe(this, Observer {isConnected->
            if (isConnected){
                recycler_view.visibility=View.VISIBLE
                disconected_view.visibility=View.GONE
                postFragment.setBackgroundColor(Color.parseColor("#59CFCCCC"))

                val recyclerView: RecyclerView = recycler_view as RecyclerView
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.setHasFixedSize(true)
                val adapter =
                    ArticleAdapter(context!!)
                recyclerView.adapter = adapter
                adapter.setArticle(ll)
                val retrofit = Retrofit.Builder()
                    .baseUrl(Util.getProperty("baseUrl", context!!))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create<Service>(Service::class.java)
                service.getAllArticles().enqueue(object: Callback<List<Article>> {
                    override fun onResponse(call: Call<List<Article>>, response: retrofit2.Response<List<Article>>?) {
                        if ((response != null) && (response.code() == 200)) {
                            val listBody:List<Article>? = response.body()
                            if (listBody != null) {
                                ll.clear()
                                ll.addAll(listBody)
                                adapter.notifyDataSetChanged()
                            }

                        }
                    }
                    override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                        Toast.makeText(activity!!.applicationContext, "Echec", Toast.LENGTH_LONG).show()
                    }
                })

                adapter.SetOnItemClickListner(object : ArticleAdapter.OnItemClickListner {
                    override fun onItemClick(article: Article) {

                        val nextAction=
                            postDirections.actionPostFragmentToArticlePageFragment()
                        nextAction.setUrl(Util.getProperty("baseUrl2", context!!)+article.url)
                        Navigation.findNavController(view!!).navigate(nextAction)
                    }

                })
            }else{
                recycler_view.visibility=View.GONE
                disconected_view.visibility=View.VISIBLE
                postFragment.setBackgroundColor(Color.WHITE)
            }

        })
        /* FacebookSdk.setApplicationId(resources.getString(R.string.facebook_app_id))
        FacebookSdk.sdkInitialize(context)
        mFacebook=FacebookHelper(this)

        loginFacebook.setOnClickListener {
            mFacebook.performSignIn(this)
        }*/
        // TODO: Use the ViewModel



    }
        /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mFacebook.onActivityResult(requestCode,resultCode,data)
    }

    override fun onFbSignInSuccess(authToken: String?, userId: String?) {

        Toast.makeText(context,""+ userId,Toast.LENGTH_SHORT).show()
    }

    override fun onFBSignOut() {
        Toast.makeText(context,"sign out",Toast.LENGTH_SHORT).show()
    }

    override fun onFbSignInFail(errorMsg: String?) {
        Toast.makeText(context,""+ errorMsg,Toast.LENGTH_SHORT).show()
    }
*/


}