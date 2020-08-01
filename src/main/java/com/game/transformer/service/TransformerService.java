package com.game.transformer.service;

import com.game.transformer.constants.TransformerConstant;
import com.game.transformer.entity.Transformer;
import com.game.transformer.exception.TransformerException;
import com.game.transformer.exception.TransformerNotFoundException;
import com.game.transformer.model.TransformerDeleteResponse;
import com.game.transformer.model.TransformerGameResponse;
import com.game.transformer.model.TransformerRequestResponse;
import com.game.transformer.repository.TransformerRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Javalingappa
 */

@Log
@Service
public class TransformerService implements ITransformerService {

    @Autowired
    TransformerRepository transformerRepository;

    @Override
    public ResponseEntity<TransformerDeleteResponse> deleteTransformerById(Integer id) {
        log.info("Deleting a transformer from the database for the transformerID " + id);
        transformerRepository.deleteById(id);
        log.info("Deleted a transformer from the database for the transformerId " + id);
        TransformerDeleteResponse response = new TransformerDeleteResponse();
        response.setMessage("Deleted a transformer from the database for the transformerId " + id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TransformerDeleteResponse> deleteAllTransformers() {
        log.info("Deleting all the transformers from the database");
        transformerRepository.deleteAll();
        log.info("Deleted all the transformers from the database");
        TransformerDeleteResponse response = new TransformerDeleteResponse();
        response.setMessage("Deleted all the transformers from the database");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TransformerRequestResponse>> getAllTransformers() {
        List<TransformerRequestResponse> response = convertEntityListToResponse(transformerRepository.findAll());
        if (response.size() > 0) {
            log.info("There are " + response.size() + " transformes in the database");
        } else {
            log.warning("There are no transformers exists in the database");
            throw new TransformerNotFoundException("There are no transformers exists in the database");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TransformerRequestResponse> getTransformersById(Integer id) {
        Optional<Transformer> optional = transformerRepository.findById(id);
        Transformer transformer = null;
        if (optional != null && optional.isPresent()) {
            transformer = optional.get();
        } else {
            throw new TransformerNotFoundException("The transformer does not exist for the given id " + id);
        }
        TransformerRequestResponse response = convertEntityToResponse(transformer);
        if (response != null) {
            log.info("The transformer exist for the given id " + id);
        } else {
            log.warning("The transformer does not exist for the given id " + id);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TransformerRequestResponse> saveTransformer(@Valid TransformerRequestResponse transformers) {
        return new ResponseEntity<>(saveTransformer(convertRequestToEntity(transformers)), HttpStatus.CREATED);
    }

    private TransformerRequestResponse saveTransformer(Transformer transformer) {
        Transformer checkTransformer = transformerRepository.findByIdOrTransformer(transformer.getId(), transformer.getTransformer());
        if (checkTransformer != null) {
            log.info("Transformer with id " + transformer.getId() + " or transformer name " + transformer.getTransformer() + " already present");
            throw new TransformerException("Transformer with id " + transformer.getId() + " or transformer name " + transformer.getTransformer() + " already present");
        }
        checkTransformer = transformerRepository.findByTransformer(transformer.getTransformer());
        if (checkTransformer != null) {
            log.info(" Transformer with name " + transformer.getTransformer() + " already present");
            throw new TransformerException("Transformer with name " + transformer.getTransformer() + " already present");
        }
        return convertEntityToResponse(transformerRepository.save(transformer));
    }

    private TransformerRequestResponse updateTransformer(Transformer transformer) {
        Transformer checkTransformer = transformerRepository.findByIdOrTransformer(transformer.getId(), transformer.getTransformer());
        if (checkTransformer != null) {
            return convertEntityToResponse(transformerRepository.save(transformer));
        } else {
            log.info("Can not update since, Transformer with id " + transformer.getId() + " or transformer name " + transformer.getTransformer() + " not present");
            throw new TransformerException("Can not update since, Transformer with id " + transformer.getId() + " or transformer name " + transformer.getTransformer() + " not  present");
        }
    }

    @Override
    public ResponseEntity<TransformerGameResponse> playTransformerGame(@Valid List<Integer> transformersIds) {
        List<Transformer> decepticons = new ArrayList<>();
        List<Transformer> autobots = new ArrayList<>();
        TransformerGameResponse response = new TransformerGameResponse();
        for (Integer id : transformersIds) {
            Optional<Transformer> optional = transformerRepository.findById(id);
            if (optional != null && optional.isPresent()) {
                if (TransformerConstant.DECEPTICONS.equalsIgnoreCase(optional.get().getType())) {
                    decepticons.add(optional.get());
                } else if (TransformerConstant.AUTOBOTS.equalsIgnoreCase(optional.get().getType())) {
                    autobots.add(optional.get());
                }
            } else {
                log.warning("Invalid transformer ID: " + id + " no transformer exist");
            }
        }
        if (decepticons.size() <= 0 && autobots.size() > 0) {
            response = new TransformerGameResponse();
            response.setBattle(0);
            response.setWinner("There are no Opponents(Decepticons) team");
            List<String> bots = autobots.stream().flatMap(p -> Stream.of(p.getTransformer())).collect(Collectors.toList());
            response.setSurvivor("Survivor List: " + bots.toString());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (decepticons.size() > 0 && autobots.size() <= 0) {
            response = new TransformerGameResponse();
            response.setBattle(0);
            response.setWinner("There are no Opponents(Autobots) team");
            List<String> decep = decepticons.stream().flatMap(p -> Stream.of(p.getTransformer())).collect(Collectors.toList());
            response.setSurvivor("Survivor List: " + decep.toString());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (decepticons.size() == 0 && autobots.size() == 0) {
            response = new TransformerGameResponse();
            response.setBattle(0);
            response.setWinner("There are no teams(Neither Decepticons nor Autobots) ");
            response.setSurvivor("No Survivors");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        decepticons.sort((Transformer o1, Transformer o2) -> o2.getRank() - o1.getRank());
        autobots.sort((Transformer o1, Transformer o2) -> o2.getRank() - o1.getRank());

        int count = decepticons.size() > autobots.size() ? autobots.size() : decepticons.size();
        Map<String, Integer> finalCount = new HashMap<>();
        finalCount.put(TransformerConstant.AUTOBOTS, 0);
        finalCount.put(TransformerConstant.DECEPTICONS, 0);
        finalCount.put(TransformerConstant.BATTLE, 0);
        for (int i = 0; i < count; i++) {
            Transformer decepticon = decepticons.get(i);
            Transformer autobot = autobots.get(i);

            if (TransformerConstant.DIRECT_WINNING_TEAMS.contains(decepticon.getTransformer())) {
                finalCount.put(TransformerConstant.DECEPTICONS, finalCount.get(TransformerConstant.DECEPTICONS) + 1);
                finalCount.put(TransformerConstant.BATTLE, finalCount.get(TransformerConstant.BATTLE) + 1);
                log.info("DECEPTICONS WINS in DIRECT NAME");
                continue;
            } else if (TransformerConstant.DIRECT_WINNING_TEAMS.contains(autobot.getTransformer())) {
                finalCount.put(TransformerConstant.AUTOBOTS, finalCount.get(TransformerConstant.AUTOBOTS) + 1);
                finalCount.put(TransformerConstant.BATTLE, finalCount.get(TransformerConstant.BATTLE) + 1);
                log.info("AUTOBOTS WINS in DIRECT NAME");
                continue;
            } else {


                if ((decepticon.getCourage() - autobot.getCourage()) >= 4 &&
                        (decepticon.getStrength() - autobot.getStrength()) >= 3) {
                    finalCount.put(TransformerConstant.DECEPTICONS, finalCount.get(TransformerConstant.DECEPTICONS) + 1);
                    finalCount.put(TransformerConstant.BATTLE, finalCount.get(TransformerConstant.BATTLE) + 1);
                    log.info("DECEPTICONS WINS in COURAGE & STRENGTH ");
                    continue;
                }
                if ((autobot.getCourage() - decepticon.getCourage()) >= 4 &&
                        (autobot.getStrength() - decepticon.getStrength()) >= 3) {
                    finalCount.put(TransformerConstant.AUTOBOTS, finalCount.get(TransformerConstant.AUTOBOTS) + 1);
                    finalCount.put(TransformerConstant.BATTLE, finalCount.get(TransformerConstant.BATTLE) + 1);
                    log.info("AUTOBOTS WINS in COURAGE & STRENGTH ");
                    continue;
                }


                if ((decepticon.getSkill()- autobot.getSkill()) >= 3 ) {
                    finalCount.put(TransformerConstant.DECEPTICONS, finalCount.get(TransformerConstant.DECEPTICONS) + 1);
                    finalCount.put(TransformerConstant.BATTLE, finalCount.get(TransformerConstant.BATTLE) + 1);
                    log.info("DECEPTICONS WINS in SKILLS");
                    continue;
                }
                if ((autobot.getSkill() - decepticon.getSkill()) >= 3) {
                    finalCount.put(TransformerConstant.AUTOBOTS, finalCount.get(TransformerConstant.AUTOBOTS) + 1);
                    finalCount.put(TransformerConstant.BATTLE, finalCount.get(TransformerConstant.BATTLE) + 1);
                    log.info("AUTOBOTS WINS in SKILLS");
                    continue;
                }

                Integer otDecepticons = getOverAllRating(decepticon);
                Integer otAutobots = getOverAllRating(autobot);

                if (otDecepticons > otAutobots) {
                    finalCount.put(TransformerConstant.DECEPTICONS, finalCount.get(TransformerConstant.DECEPTICONS) + 1);
                    finalCount.put(TransformerConstant.BATTLE, finalCount.get(TransformerConstant.BATTLE) + 1);
                    log.info("DECEPTICONS WINS in Overall Tracking");
                    continue;
                } else if (otDecepticons < otAutobots) {
                    finalCount.put(TransformerConstant.AUTOBOTS, finalCount.get(TransformerConstant.AUTOBOTS) + 1);
                    finalCount.put(TransformerConstant.BATTLE, finalCount.get(TransformerConstant.BATTLE) + 1);
                    log.info("AUTOBOTS WINS in Overall Tracking");
                    continue;
                } else {
                    log.info("No one win in any criteria");
                    finalCount.put(TransformerConstant.BATTLE, finalCount.get(TransformerConstant.BATTLE) + 1);
                    continue;
                }

            }
        }

        Integer dCount = finalCount.get(TransformerConstant.DECEPTICONS);
        Integer aCount = finalCount.get(TransformerConstant.AUTOBOTS);
        Integer bCount = finalCount.get(TransformerConstant.BATTLE);
        response = new TransformerGameResponse();
        response.setBattle(bCount);
        if (dCount > aCount) {
            log.info("DECEPTICONS WINS in overall win count ");
            response.setWinner("The Winner is DECEPTICONS");
            if (decepticons.size() < autobots.size()) {
                response.setSurvivor("The Servivor is from AUTOBOTS");
            } else if (decepticons.size() == autobots.size()) {
                response.setSurvivor("There are no Servivors");
            }
        } else if (dCount < aCount) {
            log.info("AUTOBOTS WINS in overall win count ");
            response.setWinner("The Winner is AUTOBOTS");
            if (decepticons.size() > autobots.size()) {
                response.setSurvivor("The Servivor is from DECEPTICONS");
            } else if (decepticons.size() == autobots.size()) {
                response.setSurvivor("There are no Servivors");
            }
        } else {
            log.info("AUTOBOTS WINS in overall win count ");
            response.setWinner("There is no WINNER (draw match)");
            if (decepticons.size() > autobots.size()) {
                response.setSurvivor("The Servivor is from DECEPTICONS");
            } else if (decepticons.size() < autobots.size()) {
                response.setSurvivor("The Servivor is from AUTOBOTS");
            } else if (decepticons.size() == autobots.size()) {
                response.setSurvivor("There are no Servivors");
            }
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Integer getOverAllRating(Transformer transformer) {
        return (transformer.getStrength() + transformer.getIntelligence() + transformer.getSpeed()
                + transformer.getEndurance() + transformer.getFirepower());
    }

    @Override
    public ResponseEntity<TransformerRequestResponse> updateTransformers(@Valid TransformerRequestResponse transformers) {
        return new ResponseEntity<>(updateTransformer(convertRequestToEntity(transformers)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TransformerRequestResponse>> saveAllTransformer(@Valid List<TransformerRequestResponse> transformers) {
        List<TransformerRequestResponse> retList = new ArrayList<>();
        for (TransformerRequestResponse trans : transformers) {
            log.info("Saving transformer id " + trans.getId() + " name " + trans.getTransformer());
            retList.add(saveTransformer(convertRequestToEntity(trans)));
        }
        return new ResponseEntity<>(retList, HttpStatus.OK);
    }

    private List<TransformerRequestResponse> convertEntityListToResponse(List<Transformer> transformers) {
        List<TransformerRequestResponse> list = new ArrayList<>();
        for (Transformer trade : transformers) {
            list.add(convertEntityToResponse(trade));
        }
        return list;
    }

    private TransformerRequestResponse convertEntityToResponse(Transformer transformerEntity) throws TransformerException {
        TransformerRequestResponse transformerResponse = new TransformerRequestResponse();
        if (transformerEntity != null) {
            transformerResponse.setId(transformerEntity.getId());
            transformerResponse.setTransformer(transformerEntity.getTransformer());
            transformerResponse.setCourage(transformerEntity.getCourage());
            transformerResponse.setEndurance(transformerEntity.getEndurance());
            transformerResponse.setFirepower(transformerEntity.getFirepower());
            transformerResponse.setIntelligence(transformerEntity.getIntelligence());
            transformerResponse.setRank(transformerEntity.getRank());
            transformerResponse.setSkill(transformerEntity.getSkill());
            transformerResponse.setSpeed(transformerEntity.getSpeed());
            transformerResponse.setStrength(transformerEntity.getStrength());
            transformerResponse.setType(transformerEntity.getType());
        } else {
            log.info("Transformer Entity is null");
        }
        return transformerResponse;
    }

    private Transformer convertRequestToEntity(TransformerRequestResponse transformer) throws TransformerException {
        Transformer transformerEntity = new Transformer();
        if (transformer != null) {
            transformerEntity.setId(transformer.getId());
            transformerEntity.setTransformer(transformer.getTransformer());
            transformerEntity.setCourage(transformer.getCourage());
            transformerEntity.setEndurance(transformer.getEndurance());
            transformerEntity.setFirepower(transformer.getFirepower());
            transformerEntity.setIntelligence(transformer.getIntelligence());
            transformerEntity.setRank(transformer.getRank());
            transformerEntity.setSkill(transformer.getSkill());
            transformerEntity.setSpeed(transformer.getSpeed());
            transformerEntity.setStrength(transformer.getStrength());
            transformerEntity.setType(transformer.getType());
        } else {
            log.info("Transformer Entity is null");
        }
        return transformerEntity;
    }
}