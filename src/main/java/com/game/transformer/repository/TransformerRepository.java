package com.game.transformer.repository;

import com.game.transformer.entity.Transformer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Javalingappa
 */

@Repository
public interface TransformerRepository extends JpaRepository<Transformer, Integer> {
    Transformer findByIdOrTransformer(Integer id, String transformer);
    Transformer findByTransformer(String transformer);
}
