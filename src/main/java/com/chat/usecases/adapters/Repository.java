package com.chat.usecases.adapters;

import java.util.List;
import java.util.function.Predicate;

import com.chat.domains.BaseEntity;

public interface Repository<T extends BaseEntity> {
	T findById(String id);

	boolean add(T addingEntity);

	void deleteAll();

	void deleteById(String id);

	T getFirst(Predicate<T> predicate);

	List<T> getAll();
}
