package app.news.newsapp4.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.news.newsapp4.R
import app.news.newsapp4.data.util.constants.Constants.Companion.KEY_ARTICLE
import app.news.newsapp4.data.util.resource.Resource
import app.news.newsapp4.databinding.FragmentBreakingsNewsBinding
import app.news.newsapp4.ui.adapter.ArticleAdapter
import app.news.newsapp4.ui.viewmodel.ArticleViewModel


class BreakingsNewsFragment : Fragment() {
    private lateinit var binding: FragmentBreakingsNewsBinding
    private lateinit var model: ArticleViewModel
    private lateinit var cadapter: ArticleAdapter
    var isLoading = false
    var isScrolling = false
    var isLastPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreakingsNewsBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(ArticleViewModel::class.java)
        initRecycler()
        model.getAllArticles("us")
        model.listaNewsResponse.observe(viewLifecycleOwner, Observer {
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
                        cadapter.differ.submitList(it.articles.toList())

                        val cantidad = it.totalResults / 20 + 2
                        isLastPage = cantidad == model.pageNumber
                        if (isLastPage) {
                            binding.recycler.setPadding(0, 0, 0, 0)
                        }
                    }
                }
            }
        })
        articleSelected()

        return binding.root
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }



    private fun articleSelected() {
        cadapter.itemSelecteArticle {
            val bundle = Bundle().apply {
                putSerializable(KEY_ARTICLE, it)
            }
            findNavController().navigate(
                R.id.articleFragment, bundle
            )
        }

    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val indiceVisible = layoutManager.findFirstVisibleItemPosition()
            val cantidadElementosVisibles = layoutManager.childCount
            val cantidadTotalElementos = layoutManager.itemCount

            val mayorIndice = indiceVisible >= 0
            val mayorCantidadElementos = cantidadTotalElementos >= 20
            val cantidad = indiceVisible + cantidadElementosVisibles >= cantidadTotalElementos
            val isLoadingAndLastPage = !isLoading && !isLastPage

            val posiblePaginacion =
                mayorIndice && mayorCantidadElementos && cantidad && isLoadingAndLastPage
                        && isScrolling

            if (posiblePaginacion) {
                model.getAllArticles("us")
                isScrolling = false
            }
        }
    }
    private fun initRecycler() {
        cadapter = ArticleAdapter()
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cadapter
            setHasFixedSize(true)
            addOnScrollListener(this@BreakingsNewsFragment.scrollListener)
        }
    }


}