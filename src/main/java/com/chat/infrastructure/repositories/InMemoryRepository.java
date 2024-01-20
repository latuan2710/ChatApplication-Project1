package com.chat.infrastructure.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.chat.domains.BaseEntity;
import com.chat.usecases.adapters.UserRepository;

public class InMemoryRepository<T extends BaseEntity> implements UserRepository<T> {
	private List<T> _enities;

	public InMemoryRepository() {
		_enities = new ArrayList<>();
	}

	@Override
	public T getById(String id) {
		Optional<T> entity = _enities.stream().filter(e -> e.getId().equals(id)).findFirst();
		return entity.get();
	}

	@Override
	public boolean add(T entity) {

		if (entity == null) {
			return false;
		}

		_enities.add(entity);
		return true;
	}

	@Override
	public void deleteAll() {
		_enities.clear();
	}

	@Override
	public T getFirst(Predicate<T> predicate) {
		Optional<T> entity = _enities.stream().filter(predicate).findFirst();
		return entity.isPresent() ? entity.get() : null;
	}

	@Override
	public List<T> getAll() {
		return _enities;
	}

}
