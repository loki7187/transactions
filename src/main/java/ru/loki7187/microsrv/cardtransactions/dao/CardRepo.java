package ru.loki7187.microsrv.cardtransactions.dao;

import org.springframework.data.repository.CrudRepository;
import ru.loki7187.microsrv.cardtransactions.entity.CardEntity;

public interface CardRepo extends CrudRepository<CardEntity, Long> {
}
