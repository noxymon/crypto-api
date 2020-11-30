package id.noxymon.miner.crawler.repository;

import id.noxymon.miner.crawler.repository.entities.TbEth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    List<TbEth> findAllByTbeDateBetweenOrderByTbeDateDesc(Timestamp maxHistoricalTimestamp, Timestamp previousMinutes);
}
