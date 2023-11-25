package app.news.newsapp4.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import app.news.newsapp4.data.model.Article

class ArticleDiff:DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url==newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem==newItem
    }
}