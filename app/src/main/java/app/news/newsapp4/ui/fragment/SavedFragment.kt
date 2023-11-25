package app.news.newsapp4.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.news.newsapp4.R
import app.news.newsapp4.data.model.Article
import app.news.newsapp4.data.util.constants.Constants.Companion.KEY_ARTICLE
import app.news.newsapp4.databinding.FragmentArticleBinding
import app.news.newsapp4.databinding.FragmentSavedBinding
import app.news.newsapp4.ui.adapter.ArticleAdapter
import app.news.newsapp4.ui.adapter.DeleteItem
import app.news.newsapp4.ui.viewmodel.ArticleViewModel
import com.google.android.material.snackbar.Snackbar

class SavedFragment : Fragment() {

    private lateinit var binding: FragmentSavedBinding
    private lateinit var model: ArticleViewModel
    private lateinit var cadapter: ArticleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSavedBinding.inflate(inflater,container,false)
        model=ViewModelProvider(requireActivity()).get(ArticleViewModel::class.java)
        initRecycler()
        model.getAllArticlesDatabase().observe(viewLifecycleOwner, Observer {
            cadapter.differ.submitList(it)
        })
        deleteElement()
        showArticle()
        return binding.root
    }
    private fun initRecycler(){
        cadapter= ArticleAdapter()
        binding.recycler.apply {
            adapter=cadapter
            layoutManager=LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
    private fun deleteElement(){
        val itemSelected=object :DeleteItem(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                val article=cadapter.differ.currentList[position]
                model.deleteArticle(article)
                Snackbar.make(requireView(),"article deleted",Snackbar.LENGTH_LONG).apply {
                    setAction("deshacer"){
                        model.insertArticle(article)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemSelected).apply {
            attachToRecyclerView(binding.recycler)
        }
    }
    private fun showArticle(){
        cadapter.itemSelecteArticle {
            val bundle=Bundle().apply {
                putSerializable(KEY_ARTICLE,it)
            }
            findNavController().navigate(
                R.id.articleFragment,bundle
            )
        }
    }


}