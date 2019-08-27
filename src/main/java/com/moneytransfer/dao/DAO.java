package com.moneytransfer.dao;

import java.util.List;

public interface DAO<T1,T2> {
	T1 get(T1 t);
    List<T1> getAll();
    void save(T1 t);
    void delete(T1 t);
    T1 getById(T2 id);
    void deleteById(T2 id);
}
