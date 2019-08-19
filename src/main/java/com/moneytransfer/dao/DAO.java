package com.moneytransfer.dao;

import java.util.List;

public interface DAO<T> {
	T get(T t);
    List<T> getAll();
    void save(T t);
    void delete(T t);
}
