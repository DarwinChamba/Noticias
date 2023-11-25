package app.news.newsapp4.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import app.news.newsapp4.R
import app.news.newsapp4.databinding.FragmentArticleBinding
import app.news.newsapp4.ui.viewmodel.ArticleViewModel
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment() {
    private lateinit var binding: FragmentArticleBinding
    private lateinit var model: ArticleViewModel
    val args:ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentArticleBinding.inflate(inflater,container,false)
        model=ViewModelProvider(requireActivity()).get(ArticleViewModel::class.java)
        val article=args.article
        binding.webView.apply {
            webViewClient= WebViewClient()
            article.url?.let { loadUrl(it) }
        }
        binding.favorite.setOnClickListener {
            model.insertArticle(article)
            Snackbar.make(requireView(),"article saved successfully",Snackbar.LENGTH_SHORT).show()
        }




        return binding.root
    }


}