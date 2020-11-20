package id.noxymon.miner.crawler.repository;

import id.noxymon.miner.crawler.repository.entities.TbEthHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EtheriumHourlyRepository extends JpaRepository<TbEthHour, Timestamp> {
    List<TbEthHour> findAllByTbDateBetweenOrderByTbDateDesc(Timestamp maxHistoricalTimestamp, Timestamp previousHour);
}
