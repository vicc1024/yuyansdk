package com.yuyan.imemodule.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.yuyan.imemodule.db.BaseDao
import com.yuyan.imemodule.db.entry.Phrase

@Dao
interface PhraseDao : BaseDao<Phrase> {

    @Query("select * from phrase ORDER BY isKeep DESC, time DESC")
    fun getAll(): List<Phrase>

    @Query("delete from phrase where content = :content")
    fun deleteByContent(content: String)

    @Query("delete from phrase")
    fun deleteAll()
}
