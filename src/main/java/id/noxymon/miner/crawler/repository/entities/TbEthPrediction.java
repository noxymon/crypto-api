package id.noxymon.miner.crawler.repository.entities;

import java.sql.*;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "id.noxymon.miner.crawler.repository.entities.TbEthPrediction")
@Table(name = "tb_eth_prediction")
public class TbEthPrediction {

  @Id
  @Column(name = "\"predicted_timestamp\"", nullable = false, updatable = false)
  private Timestamp predictedTimestamp;
  @Column(name = "\"predicted_5_minutes\"", nullable = true)
  private Double predicted5Minutes;
  @Column(name = "\"predicted_10_minutes\"", nullable = true)
  private Double predicted10Minutes;
  @Column(name = "\"predicted_15_minutes\"", nullable = true)
  private Double predicted15Minutes;
  @Column(name = "\"predicted_1_hour\"", nullable = true)
  private Double predicted1Hour;
  @Column(name = "\"predicted_3_hour\"", nullable = true)
  private Double predicted3Hour;
  @Column(name = "\"predicted_6_hour\"", nullable = true)
  private Double predicted6Hour;
  @Column(name = "\"predicted_12_hour\"", nullable = true)
  private Double predicted12Hour;
  @Column(name = "\"predicted_24_hour\"", nullable = true)
  private Double predicted24Hour;
  @Column(name = "\"predicted_3_days\"", nullable = true)
  private Double predicted3Days;
  @Column(name = "\"predicted_7_days\"", nullable = true)
  private Double predicted7Days;
  @Column(name = "\"predicted_14_days\"", nullable = true)
  private Double predicted14Days;
  @Column(name = "\"predicted_30_days\"", nullable = true)
  private Double predicted30Days;
}