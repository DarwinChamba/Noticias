package app.news.newsapp4.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import app.news.newsapp4.R
import app.news.newsapp4.data.model.Article

class ArticleAdapter:RecyclerView.Adapter<ArticleViewHolder>() {
    private val diffItem=ArticleDiff()
    val differ=AsyncListDiffer(this,diffItem)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_recycler,parent,false
        ))
    }
    var setOnClick:((Article)->Unit)?=null
    fun itemSelecteArticle(listener:((Article)->Unit)?){
        setOnClick=listener
    }

    override fun getItemCount()=differ.currentList.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.render(differ.currentList[position],setOnClick)
    }
}