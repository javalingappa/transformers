package com.game.transformer.service;

import com.game.transformer.model.TransformerDeleteResponse;
import com.game.transformer.model.TransformerGameResponse;
import com.game.transformer.model.TransformerRequestResponse;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Javalingappa
 */

interface ITransformerService {

    ResponseEntity<TransformerDeleteResponse> deleteTransformerById(Integer id);

    ResponseEntity<TransformerDeleteResponse> deleteAllTransformers();

    ResponseEntity<List<TransformerRequestResponse>> getAllTransformers();

    ResponseEntity<TransformerRequestResponse> getTransformersById(Integer id);

    ResponseEntity<TransformerRequestResponse> saveTransformer(@Valid TransformerRequestResponse transformers);

    ResponseEntity<TransformerGameResponse> playTransformerGame(@Valid List<Integer> transformersIds);

    ResponseEntity<TransformerRequestResponse> updateTransformers(@Valid TransformerRequestResponse transformers);

    ResponseEntity<List<TransformerRequestResponse>> saveAllTransformer(@Valid List<TransformerRequestResponse> transformers);
}
