package app.news.newsapp4.data.util.converter

import androidx.room.TypeConverter
import app.news.newsapp4.data.model.Source

class Converter {
    @TypeConverter
    fun fromSource(source:Source):String{
        return source.name
    }
    @TypeConverter
    fun toSource(name:String):Source{
        return Source(name,name)
    }
}