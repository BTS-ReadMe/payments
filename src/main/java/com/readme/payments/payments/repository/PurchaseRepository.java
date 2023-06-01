package com.readme.payments.payments.repository;

import com.readme.payments.payments.model.PurchaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<PurchaseRecord, Long> {

    Boolean existsByUuidAndEpisodeId(String uuid, Long episodeId);
}
