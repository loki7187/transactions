package ru.loki7187.microsrv.dbservice.dao;

import org.springframework.data.repository.CrudRepository;
import ru.loki7187.microsrv.dbservice.entity.OpHistoryEntity;

import java.util.Optional;

public interface OpHistoryRepo extends CrudRepository<OpHistoryEntity, Long> {

    Optional<OpHistoryEntity> findOpHistoryEntityByTrnIdAndDirection(Long id, String direction);
}
