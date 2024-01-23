package com.chat.usecases.adapters;

import java.util.List;
import java.util.function.Predicate;

import com.chat.domains.ChatEntity;

public interface GroupRepository<T extends ChatEntity> {
	List<T> getAll();

	T getById(String id);

	boolean add(T addingEntity);

	void deleteAll();

	T getFirst(Predicate<T> predicate);
}
