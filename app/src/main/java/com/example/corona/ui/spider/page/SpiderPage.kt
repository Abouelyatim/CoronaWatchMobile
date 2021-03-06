package com.example.corona.ui.spider.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient

import com.example.corona.R


import kotlinx.android.synthetic.main.spider_page_fragment.*

class SpiderPage : Fragment() {

    companion object {
        fun newInstance() = SpiderPage()
    }
    private lateinit var sfeArgs: SpiderPageArgs


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.spider_page_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        sfeArgs=
            SpiderPageArgs.fromBundle(arguments!!)

        if(sfeArgs.url!="null"){
            web_view_page_spider.webViewClient= WebViewClient()
            web_view_page_spider.loadUrl(sfeArgs.url)
            web_view_page_spider.setWebChromeClient(WebChromeClient())
            web_view_page_spider.setWebViewClient(WebViewClient())
            web_view_page_spider.getSettings().setAppCacheEnabled(true)
            web_view_page_spider.getSettings().setJavaScriptEnabled(true)
        }
    }

}
