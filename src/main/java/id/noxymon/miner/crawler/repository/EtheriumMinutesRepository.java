package id.noxymon.miner.crawler.repository;

import id.noxymon.miner.crawler.repository.entities.TbEth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EtheriumMinutesRepository extends JpaRepository<TbEth, Timestamp> {
    @Query(nativeQuery = true,
            value = "select " +
                    "   DATE_FORMAT(tbe_date, '%Y-%m-%d %H:00') as tbe_date, " +
                    "   AVG(tbe_open) as tbe_open, " +
                    "   AVG(tbe_high) as tbe_high, " +
                    "   AVG(tbe_low)  as tbe_low, " +
                    "   AVG(tbe_close)as tbe_close " +
                    "from tb_eth te " +
                    "where tbe_date BETWEEN :startTimestamp and :endTimestamp " +
                    "group by DATE_FORMAT(tbe_date, '%Y-%m-%d %H:00')"
    )
    List<TbEth> summarizeHourly(@Param("startTimestamp") Timestamp start, @Param("endTimestamp") Timestamp end);

    @Transactional(readOnly = true)
    @Query(nativeQuery = true,
            value = "select " +
            "    `tb_eth`.`tbe_date` + interval :intervalMinutes minute AS `tbe_date`, " +
            "    avg(`tb_eth`.`tbe_open`) AS `tbe_open`," +
            "    avg(`tb_eth`.`tbe_high`) AS `tbe_high`," +
            "    avg(`tb_eth`.`tbe_low`) AS `tbe_low`," +
            "    avg(`tb_eth`.`tbe_close`) AS `tbe_close`" +
            "from" +
            "    `tb_eth` " +
            "WHERE `tbe_date` BETWEEN (:startTimestamp - interval :maxLag hour) AND :startTimestamp " +
            "group by" +
            "    floor(unix_timestamp(`tb_eth`.`tbe_date`) / (60 * (:intervalMinutes + 1))) " +
            "ORDER BY tbe_date ASC")
    List<TbEth> findRecordMinutePrediction(@Param("startTimestamp") Timestamp startTimestamp,
                                           @Param("maxLag") int maxLagInHour,
                                           @Param("intervalMinutes") int intervalMinutes);

    @Transactional(readOnly = true)
    @Query(nativeQuery = true,
            value = "select " +
                    "    `tb_eth`.`tbe_date` + interval :intervalHours hour AS `tbe_date`, " +
                    "    avg(`tb_eth`.`tbe_open`) AS `tbe_open`," +
                    "    avg(`tb_eth`.`tbe_high`) AS `tbe_high`," +
                    "    avg(`tb_eth`.`tbe_low`) AS `tbe_low`," +
                    "    avg(`tb_eth`.`tbe_close`) AS `tbe_close`" +
                    "from" +
                    "    `tb_eth` " +
                    "WHERE `tbe_date` BETWEEN (:startTimestamp - interval (:maxLag+1) hour) AND :startTimestamp " +
                    "group by" +
                    "    floor(unix_timestamp(`tb_eth`.`tbe_date`) / (3600 * (:intervalHours + 1))) " +
                    "ORDER BY tbe_date ASC")
    List<TbEth> findRecordHourPrediction(@Param("startTimestamp") Timestamp startTimestamp,
                                         @Param("maxLag") int maxLagInHour,
                                         @Param("intervalHours") int intervalHours);

    @Transactional(readOnly = true)
    @Query(nativeQuery = true,
            value = "select " +
                    "    `tb_eth`.`tbe_date` + interval (:intervalDays - 1) day AS `tbe_date`, " +
                    "    avg(`tb_eth`.`tbe_open`) AS `tbe_open`," +
                    "    avg(`tb_eth`.`tbe_high`) AS `tbe_high`," +
                    "    avg(`tb_eth`.`tbe_low`) AS `tbe_low`," +
                    "    avg(`tb_eth`.`tbe_close`) AS `tbe_close`" +
                    "from" +
                    "    `tb_eth` " +
                    "WHERE `tbe_date` BETWEEN (:startTimestamp - interval :maxLag hour) AND :startTimestamp " +
                    "group by" +
                    "    floor(unix_timestamp(`tb_eth`.`tbe_date`) / (3600 * 24 * (:intervalDays))) " +
                    "ORDER BY tbe_date ASC")
    List<TbEth> findRecordDailyPrediction(@Param("startTimestamp") Timestamp startTimestamp,
                                          @Param("maxLag") int maxLagInHour,
                                          @Param("intervalDays") int intervalDays);
}
