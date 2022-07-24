package ru.loki7187.app.dao;


import org.springframework.data.repository.CrudRepository;
import ru.loki7187.app.entity.CardEntity;

public interface CardRepo extends CrudRepository<CardEntity, Long> {
}
