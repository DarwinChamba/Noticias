package app.news.newsapp4.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.news.newsapp4.data.model.Article
import app.news.newsapp4.databinding.ItemRecyclerBinding
import com.bumptech.glide.Glide

class ArticleViewHolder(view:View):RecyclerView.ViewHolder(view) {

    val binding=ItemRecyclerBinding.bind(view)

    fun render(article:Article,articleSelected:((Article)->Unit)?) {
        binding.description.text = article.description
        binding.title.text = article.title
        binding.publishedAt.text = article.publishedAt
        binding.source.text = article.source?.name
        Glide.with(binding.title).load(article.urlToImage).into(binding.image)
        itemView.setOnClickListener {
            articleSelected?.invoke(article)
        }
    }
}