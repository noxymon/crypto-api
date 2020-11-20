package id.noxymon.miner.crawler.repository.entities;

import java.io.Serializable;
import java.sql.*;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "id.noxymon.miner.crawler.repository.entities.TbModelLinear")
@Table(name = "tb_model_linear")
@IdClass(TbModelLinear.PrimaryKeys.class)
public class TbModelLinear {
  @Data
  public static class PrimaryKeys implements Serializable {
    private String coefficient;
    private Integer index;
  }

  @Id
  @Column(name = "\"coefficient\"", nullable = false)
  private String coefficient;
  @Id
  @Column(name = "\"index\"", nullable = false)
  private Integer index;
  @Column(name = "\"value\"", nullable = false)
  private Double value;
}