package id.noxymon.miner.crawler.repository;

import id.noxymon.miner.crawler.repository.entities.TbEthPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface EtheriumPredictionRepository extends JpaRepository<TbEthPrediction, Timestamp> {
}