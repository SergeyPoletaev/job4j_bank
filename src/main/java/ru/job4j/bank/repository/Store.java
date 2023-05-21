package ru.job4j.bank.repository;

import ru.job4j.bank.model.Id;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Store<T extends Id> {
    protected final Map<Integer, T> store = new ConcurrentHashMap<>();
    protected final AtomicInteger idGenerator = new AtomicInteger(0);

    public T save(T model) {
        int id = idGenerator.incrementAndGet();
        model.setId(id);
        store.put(id, model);
        return model;
    }

    public boolean update(T model) {
        return store.computeIfPresent(model.getId(), (k, v) -> model) != null;
    }

    public List<T> findAll() {
        return new CopyOnWriteArrayList<>(store.values());
    }

    public Optional<T> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }
}
