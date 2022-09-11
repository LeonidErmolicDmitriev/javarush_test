package com.game.model;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

@JsonAutoDetect
public class PlayerRequest{

    private String name;
    private String title;
    private Race race;
    private Profession profession;
    private Integer experience;
    private Long birthday;
    private Boolean banned;
    private Long id;
    private Integer level;
    private Integer untilNextLevel;

    public Boolean getBanned() {
        return banned;
    }

    public Long getId() {
        return id;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Race getRace() {
        return race;
    }

    public Profession getProfession() {
        return profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public Long getBirthday() {
        return birthday;
    }


}
