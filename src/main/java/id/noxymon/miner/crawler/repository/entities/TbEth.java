package id.noxymon.miner.crawler.repository.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity(name = "id.noxymon.miner.crawler.repository.entities.TbEth")
@Table(name = "tb_eth")
public class TbEth {

  @Id
  @Column(name = "\"tbe_date\"", nullable = false)
  private Timestamp tbeDate;
  @Column(name = "\"tbe_open\"", nullable = true)
  private java.math.BigDecimal tbeOpen;
  @Column(name = "\"tbe_high\"", nullable = true)
  private java.math.BigDecimal tbeHigh;
  @Column(name = "\"tbe_low\"", nullable = true)
  private java.math.BigDecimal tbeLow;
  @Column(name = "\"tbe_close\"", nullable = true)
  private java.math.BigDecimal tbeClose;
}