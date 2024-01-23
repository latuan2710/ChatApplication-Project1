package com.chat.usecases.adapters;

import java.util.List;
import java.util.function.Predicate;

import com.chat.domains.BaseEntity;
import com.chat.domains.User;

public interface Repository<T extends BaseEntity> {
	T getById(String id);

	boolean add(T addingEntity);

	void deleteAll();

	T getFirst(Predicate<T> predicate);

	List<T> getAll();
}
