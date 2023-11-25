package app.news.newsapp4.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.news.newsapp4.R
import app.news.newsapp4.data.util.constants.Constants.Companion.KEY_ARTICLE
import app.news.newsapp4.data.util.resource.Resource
import app.news.newsapp4.databinding.FragmentSearchBinding
import app.news.newsapp4.ui.adapter.ArticleAdapter
import app.news.newsapp4.ui.viewmodel.ArticleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var model: ArticleViewModel
    private lateinit var cadapter: ArticleAdapter
    var isLoading=false
    var isLastPage=false
    var isScrolling=false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(ArticleViewModel::class.java)
        initRecycler()
        var job: Job? = null
        binding.search.addTextChangedListener {
            job?.cancel()
            job = CoroutineScope(Dispatchers.IO).launch {
                delay(500L)
                withContext(Dispatchers.Main) {
                    if (it.toString().isNotEmpty()) {
                        model.searchArticles(it.toString())
                    }
                }
            }
        }
        model.searchNewsResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> showProgressBar()
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let {
                        cadapter.differ.submitList(it.articles)
                        val numeroPagina=it.totalResults/20+2
                        isLastPage=numeroPagina==model.pageNumberSearch
                        if(isScrolling){
                            binding.recler.setPadding(0,0,0,0)
                        }
                    }
                }
            }
        })
        articleSelected()


        return binding.root
    }
    val scrollListener=object:RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val indiceVisible=layoutManager.findFirstVisibleItemPosition()
            val numeroElementosVisibles=layoutManager.childCount
            val totalElementos=layoutManager.itemCount

            val mayorAlIndiceVisible=indiceVisible>=0
            val mayorAlNumeroElementos=totalElementos>=20
            val mayorPagina=indiceVisible+numeroElementosVisibles>=totalElementos
            val isNotLastPageAndNotLoading=!isLastPage && !isLoading

            val posiblePaginacion=mayorAlIndiceVisible && mayorAlNumeroElementos && mayorPagina &&
                    isNotLastPageAndNotLoading && isScrolling

            if (posiblePaginacion){
                model.searchArticles(binding.search.toString())
                isScrolling=false
            }
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun initRecycler() {
        cadapter = ArticleAdapter()
        binding.recler.apply {
            adapter = cadapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
    }

    private fun articleSelected() {
        cadapter.itemSelecteArticle {
            val bundle = Bundle().apply {
                putSerializable(KEY_ARTICLE, it)
            }
            findNavController().navigate(R.id.articleFragment, bundle)
        }
    }


}