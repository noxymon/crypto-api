package id.noxymon.miner.crawler.repository.entities;

import java.sql.*;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "id.noxymon.miner.crawler.repository.entities.TbEthHour")
@Table(name = "tb_eth_hour")
public class TbEthHour {

  @Id
  @Column(name = "\"tb_date\"", nullable = false)
  private Timestamp tbDate;
  @Column(name = "\"open_avg\"", nullable = true)
  private Double openAvg;
  @Column(name = "\"high_avg\"", nullable = true)
  private Double highAvg;
  @Column(name = "\"low_avg\"", nullable = true)
  private Double lowAvg;
  @Column(name = "\"close_avg\"", nullable = true)
  private Double closeAvg;
}