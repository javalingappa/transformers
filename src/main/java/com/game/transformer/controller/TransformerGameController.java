package com.game.transformer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.transformer.api.TransformersApi;
import com.game.transformer.model.TransformerDeleteResponse;
import com.game.transformer.model.TransformerGameResponse;
import com.game.transformer.model.TransformerRequestResponse;
import com.game.transformer.service.TransformerService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Javalingappa
 */

@Log
@RestController
@RequestMapping("/api/v1")
public class TransformerGameController implements TransformersApi {

    @Autowired
    TransformerService transformerService;

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getAcceptHeader() {
        return Optional.empty();
    }

    @Override
    public ResponseEntity<TransformerDeleteResponse> deleteTransformerById(Integer id) {
        return  transformerService.deleteTransformerById(id);
    }

    @Override
    public ResponseEntity<TransformerDeleteResponse> deleteAllTransformers() {
        return transformerService.deleteAllTransformers();
    }

    @Override
    public ResponseEntity<List<TransformerRequestResponse>> getAllTransformers() {
        return transformerService.getAllTransformers();
    }

    @Override
    public ResponseEntity<TransformerRequestResponse> getTransformersById(Integer id) {
        return transformerService.getTransformersById(id);
    }

    @Override
    public ResponseEntity<TransformerRequestResponse> saveTransformer(@Valid TransformerRequestResponse transformer) {
        return transformerService.saveTransformer(transformer);
    }

    @Override
    public ResponseEntity<TransformerGameResponse> playTransformerGame(@Valid List<Integer> transformersIds) {
        return transformerService.playTransformerGame(transformersIds);
    }

    @Override
    public ResponseEntity<TransformerRequestResponse> updateTransformers(@Valid TransformerRequestResponse transformer) {
        return transformerService.updateTransformers(transformer);
    }

    @Override
    public ResponseEntity<List<TransformerRequestResponse>> saveAllTransformer(@Valid List<TransformerRequestResponse> transformers) {
        return transformerService.saveAllTransformer(transformers);
    }
}
