package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.model.PlayerRequest;
import com.game.model.WrongParamValueException;
import com.game.repository.PlayerRepository;
import com.game.model.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerService {
    @Autowired
    PlayerRepository playerRepository;

    public Player create(PlayerRequest playerRequest) throws WrongParamValueException{
        if(playerRequest.getBirthday()==null||playerRequest.getExperience()==null||playerRequest.getName()==null||playerRequest.getProfession()==null
            ||playerRequest.getRace()==null||playerRequest.getTitle()==null)
            throw new WrongParamValueException();
        if(playerRequest.getName().length()>12||playerRequest.getTitle().length()>30)
            throw new WrongParamValueException();
        if(playerRequest.getName().isEmpty())
            throw new WrongParamValueException();
        if(!isExperienceValid(playerRequest))
            throw new WrongParamValueException();
        if(!isBirthdateValid(playerRequest))
            throw new WrongParamValueException();
        Date date = new Date(playerRequest.getBirthday());
        Boolean banned = playerRequest.getBanned();
        if(banned==null)
            banned = false;
        Player newPlayer = new Player();
        newPlayer.setBanned(banned);
        newPlayer.setBirthday(date);
        newPlayer.setExperience(playerRequest.getExperience());
        newPlayer.setName(playerRequest.getName());
        newPlayer.setProfession(playerRequest.getProfession());
        newPlayer.setRace(playerRequest.getRace());
        newPlayer.setTitle(playerRequest.getTitle());
        newPlayer.updateLevelInfo();
        playerRepository.save(newPlayer);
        return newPlayer;
    }

    public Player update(Long playerId, PlayerRequest playerRequest)  throws ResourceNotFoundException {
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);
        if (!optionalPlayer.isPresent()){
            throw new ResourceNotFoundException();
        }
        Player player = optionalPlayer.get();
        if(playerRequest.getBanned()!=null)
            player.setBanned(playerRequest.getBanned());
        if(playerRequest.getBirthday()!=null){
            if(!isBirthdateValid(playerRequest))
                throw new WrongParamValueException();
            player.setBirthday(new Date(playerRequest.getBirthday()));
        }
        if(playerRequest.getName()!=null)
            player.setName(playerRequest.getName());
        if(playerRequest.getProfession()!=null)
            player.setProfession(playerRequest.getProfession());
        if(playerRequest.getRace()!=null)
            player.setRace(playerRequest.getRace());
        if(playerRequest.getTitle()!=null)
            player.setTitle(playerRequest.getTitle());
        if(playerRequest.getExperience()!=null){
            if(!isExperienceValid(playerRequest))
                throw new WrongParamValueException();
            player.setExperience(playerRequest.getExperience());
        }
        player.updateLevelInfo();
        playerRepository.save(player);
        return player;
    }

    public Player getById(Long playerId) throws ResourceNotFoundException{
        return playerRepository.findById(playerId).orElseThrow(() -> new ResourceNotFoundException());
    }

    public List<Player> getPlayers(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel, Pageable pageable) {
        Date afterDate = null;
        Date beforeDate = null;
        if(after!=null&&after>0)
            afterDate = new Date(after);
        if(before!=null&&before>0)
            beforeDate = new Date(before);
        return playerRepository.findPlayers(name, title, race, profession, afterDate, beforeDate, minExperience, maxExperience, minLevel, maxLevel, banned, pageable);
    }

    public void delete(Long playerId) throws ResourceNotFoundException {
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);
        if(!optionalPlayer.isPresent())
            throw new ResourceNotFoundException();
        if (optionalPlayer.get().getId()==playerId){
            playerRepository.deleteById(playerId);
        }
        else throw new ResourceNotFoundException();
    }

    private boolean isExperienceValid(PlayerRequest playerRequest){
        if(playerRequest.getExperience()<0||playerRequest.getExperience()>10000000)
            return false;
        return true;
    }
    private boolean isBirthdateValid(PlayerRequest playerRequest){
        if(playerRequest.getBirthday()<0)
            return false;
        Date date = new Date(playerRequest.getBirthday());
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        if(year<2000||year>3000)
            return false;
        return true;
    }
}