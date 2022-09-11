package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
    @Query("SELECT p FROM Player p "
            + "WHERE (:name is null or p.name like %:name%) "
            + "and (:title is null or p.title like %:title%) "
            + "and (:race is null or p.race = :race) "
            + "and (:profession is null or p.profession = :profession) "
            + "and (:before is null or p.birthday <= :before) "
            + "and (:after is null or p.birthday >= :after) "
            + "and (:minExperience is null or p.experience >= :minExperience) "
            + "and (:maxExperience is null or p.experience <= :maxExperience) "
            + "and (:minLevel is null or p.level >= :minLevel) "
            + "and (:maxLevel is null or p.level <= :maxLevel) "
            + "and (:banned is null or p.banned = :banned)")
    List<Player> findPlayers(@Param("name") String name, @Param("title") String title, @Param("race") Race race, @Param("profession") Profession profession, @Param("after") Date after, @Param("before") Date before, @Param("minExperience") Integer minExperience, @Param("maxExperience") Integer maxExperience, @Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel, @Param("banned") Boolean banned, Pageable pageable);
}
