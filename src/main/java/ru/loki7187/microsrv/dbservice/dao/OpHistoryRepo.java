package ru.loki7187.microsrv.dbservice.dao;

import org.springframework.data.repository.CrudRepository;
import ru.loki7187.microsrv.dbservice.entity.OpHistoryEntity;
import ru.loki7187.microsrv.dbservice.entity.OpHistoryEntityIdClass;

import java.util.Optional;

public interface OpHistoryRepo extends CrudRepository<OpHistoryEntity, OpHistoryEntityIdClass> {
}
