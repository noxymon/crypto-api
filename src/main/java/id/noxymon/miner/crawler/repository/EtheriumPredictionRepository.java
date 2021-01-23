package id.noxymon.miner.crawler.repository;

import id.noxymon.miner.crawler.repository.entities.TbEthPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Repository
public interface EtheriumPredictionRepository extends JpaRepository<TbEthPrediction, Timestamp> {

    @Query(nativeQuery = true,
            value = "select" +
                    "        *" +
                    "    from" +
                    "        `tb_eth_prediction` `tep`" +
                    "    where" +
                    "        `tep`.`predicted_timestamp` between (:currentTimestamp - interval 5 minute) and :currentTimestamp" +
                    "        and `tep`.`predicted_5_minutes` is not null" +
                    "    order by" +
                    "        `tep`.`predicted_timestamp` desc" +
                    "    limit 1")
    @Transactional(readOnly = true)
    TbEthPrediction findPredictionFromLast5Minutes(@Param("currentTimestamp") Timestamp currentTimestamp);

    @Query(nativeQuery = true,
            value = "select" +
                    "        *" +
                    "    from" +
                    "        `tb_eth_prediction` `tep`" +
                    "    where" +
                    "        `tep`.`predicted_timestamp` between (:currentTimestamp - interval 10 minute) and :currentTimestamp" +
                    "        and `tep`.`predicted_10_minutes` is not null" +
                    "    order by" +
                    "        `tep`.`predicted_timestamp` desc" +
                    "    limit 1")
    @Transactional(readOnly = true)
    TbEthPrediction findPredictionFromLast10Minutes(@Param("currentTimestamp") Timestamp currentTimestamp);

    @Query(nativeQuery = true,
            value = "select" +
                    "        *" +
                    "    from" +
                    "        `tb_eth_prediction` `tep`" +
                    "    where" +
                    "        `tep`.`predicted_timestamp` between (:currentTimestamp - interval 15 minute) and :currentTimestamp" +
                    "        and `tep`.`predicted_15_minutes` is not null" +
                    "    order by" +
                    "        `tep`.`predicted_timestamp` desc" +
                    "    limit 1")
    @Transactional(readOnly = true)
    TbEthPrediction findPredictionFromLast15Minutes(@Param("currentTimestamp") Timestamp currentTimestamp);

    @Query(nativeQuery = true,
            value = "select" +
                    "        *" +
                    "    from" +
                    "        `tb_eth_prediction` `tep`" +
                    "    where" +
                    "        `tep`.`predicted_timestamp` between (:currentTimestamp - interval 60 minute) and :currentTimestamp" +
                    "        and `tep`.`predicted_1_hour` is not null" +
                    "    order by" +
                    "        `tep`.`predicted_timestamp` desc" +
                    "    limit 1")
    @Transactional(readOnly = true)
    TbEthPrediction findPredictionFromLast60Minutes(@Param("currentTimestamp") Timestamp currentTimestamp);
}