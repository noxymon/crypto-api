package id.noxymon.miner.crawler.repository;

import id.noxymon.miner.crawler.repository.entities.TbModelLinear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtheriumModelLinearRepository extends JpaRepository<TbModelLinear, TbModelLinear.PrimaryKeys> {
}
