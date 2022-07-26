package ru.loki7187.microsrv.dbservice.dao;

import org.springframework.data.repository.CrudRepository;
import ru.loki7187.microsrv.dbservice.entity.CardEntity;

public interface CardRepo extends CrudRepository<CardEntity, Long> {
}
