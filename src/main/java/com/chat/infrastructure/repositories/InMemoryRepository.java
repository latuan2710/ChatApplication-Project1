package com.chat.infrastructure.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.chat.domains.BaseEntity;
import com.chat.usecases.adapters.Repository;

public class InMemoryRepository<T extends BaseEntity> implements Repository<T> {
	private List<T> _enities;

	public InMemoryRepository() {
		_enities = new ArrayList<>();
	}

	@Override
	public T findById(String id) {
		return getFirst(e -> e.getId().equals(id));
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

	@Override
	public void deleteById(String id) {
		T entity = this.getFirst(t -> t.getId().equals(id));
		_enities.remove(entity);
	}
}
