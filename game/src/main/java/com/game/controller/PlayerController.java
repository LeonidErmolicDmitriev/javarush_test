package com.game.controller;

import com.game.entity.Player;
import com.game.model.PlayerRequest;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.model.ResourceNotFoundException;
import com.game.model.WrongParamValueException;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {
    @Autowired
    PlayerService playerService;
    @GetMapping
    public ResponseEntity<List<Player>> getPlayersList(@RequestParam(value = "name", required = false) String name,
                                                       @RequestParam(value = "title", required = false) String title,
                                                       @RequestParam(value = "race", required = false) Race race,
                                                       @RequestParam(value = "profession", required = false) Profession profession,
                                                       @RequestParam(value = "after", required = false) Long after,
                                                       @RequestParam(value = "before", required = false) Long before,
                                                       @RequestParam(value = "banned", required = false) Boolean banned,
                                                       @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                       @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                       @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                       @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                                       @RequestParam(value = "order", required = false) PlayerOrder playerOrder,
                                                       @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                       @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if(playerOrder==null)
            playerOrder = PlayerOrder.ID;
        if(pageNumber==null)
            pageNumber = 0;
        if(pageSize==null)
            pageSize = 3;
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by(Sort.Direction.ASC, playerOrder.getFieldName()));
        List<Player> players = playerService.getPlayers(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, pageable);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }
    @GetMapping(value = "/count")
    public ResponseEntity<Integer> getPlayersCount(@RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "title", required = false) String title,
                                       @RequestParam(value = "race", required = false) Race race,
                                       @RequestParam(value = "profession", required = false) Profession profession,
                                       @RequestParam(value = "after", required = false) Long after,
                                       @RequestParam(value = "before", required = false) Long before,
                                       @RequestParam(value = "banned", required = false) Boolean banned,
                                       @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                       @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                       @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                       @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {
        List<Player> players = playerService.getPlayers(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, Pageable.unpaged());
        return new ResponseEntity<>(players.size(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Player> create(@RequestBody PlayerRequest playerRequest) {
        Player player;
        try {
            player = playerService.create(playerRequest);
        }
        catch (WrongParamValueException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(player, HttpStatus.OK);
    }
    @PostMapping(value = "/{id}")
    public ResponseEntity<Player> update(@PathVariable(value = "id") Long playerId, @RequestBody PlayerRequest playerRequest){
        if(!isIdValid(playerId))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player player;
        try {
            player = playerService.update(playerId, playerRequest);
        }
        catch (ResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (WrongParamValueException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(player, HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Player> get(@PathVariable(value = "id") Long playerId) {
        if(!isIdValid(playerId))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player player;
        try {
            player = playerService.getById(playerId);
        }
        catch (ResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(player, HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long playerId) {
        if(!isIdValid(playerId))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            playerService.delete(playerId);
        }
        catch (ResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    private boolean isIdValid(Long id){
        if(id<=0)
            return false;
        if(Math.ceil(id)!=id)
            return false;
        return true;
    }
}
