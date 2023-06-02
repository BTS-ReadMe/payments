package com.readme.payments.payments.repository;

import com.readme.payments.payments.model.PurchaseRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<PurchaseRecord, Long> {

    List<PurchaseRecord> findAllByUuid(String uuid);

    Boolean existsByUuidAndEpisodeId(String uuid, Long episodeId);
}
