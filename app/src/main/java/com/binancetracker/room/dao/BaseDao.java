package com.binancetracker.room.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.binancetracker.ui.main.AssetModel;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(T model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(Collection<T> models);

    @Delete
    public abstract void delete(T model);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(T model);

    public void deleteAll()
    {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                "DELETE FROM " + getTname()
        );
        postQuery(query);
    }

    public List<T> getAll()
    {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                "SELECT * FROM " + getTname()
        );
        return postQueryGetList(query);
    }

    private String getTname()
    {
        Class tclass = (Class)((ParameterizedType)getClass().getSuperclass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tclass.getSimpleName();
    }

    @RawQuery
    protected abstract T postQuery(SupportSQLiteQuery query);

    @RawQuery
    protected abstract List<T> postQueryGetList(SupportSQLiteQuery query);
}
