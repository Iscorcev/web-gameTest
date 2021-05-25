package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.models.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> findAll(Pageable pageable) {
        return playerRepository.findAll(pageable).stream().collect(Collectors.toList());
    }

    @Override
    public Player add(Map<String, String> params) {
        try {
            String name = params.getOrDefault("name", null);
            String title = params.getOrDefault("title", null);
            Race race = Race.valueOf(params.get("race"));
            Profession profession = Profession.valueOf(params.get("profession"));
            Date birthday = new Date(Long.parseLong(params.get("birthday")));
            Boolean banned = Boolean.parseBoolean(params.getOrDefault("banned", "false"));
            Integer experience = Integer.parseInt(params.get("experience"));
            Player player = new Player(name, title, race, profession, experience, birthday, banned);
            player.calculateLevel();
            player.calculateUntilNextLevel();
            return playerRepository.save(player);
        } catch (NullPointerException | IllegalArgumentException | ClassCastException e) {
            return null;
        }
    }

    @Override
    public Player updatePlayer(Long id, Map<String, String> params) {
        if (!playerRepository.findById(id).isPresent() || params == null) return null;
        Player result = playerRepository.findById(id).get();
        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);
        Profession profession = params.containsKey("profession") ? Profession.valueOf(params.get("profession")) : null;
        Race race = params.containsKey("race") ? Race.valueOf(params.get("race")) : null;
        Date birthday = params.containsKey("birthday") ? new Date(Long.parseLong(params.get("birthday"))) : null;
        Boolean banned = params.containsKey("banned") ? "true".equals(params.get("banned")) : null;
        Integer experience = params.containsKey("experience") ? Integer.parseInt(params.get("experience")) : null;
        if (name != null) result.setName(name);
        if (title != null) result.setTitle(title);
        if (profession != null) result.setProfession(profession);
        if (race != null) result.setRace(race);
        if (birthday != null) result.setBirthday(birthday);
        if (banned != null) result.setBanned(banned);
        if (experience != null) {
            result.setExperience(experience);
            result.calculateLevel();
            result.calculateUntilNextLevel();
        }
        playerRepository.saveAndFlush(result);
        return result;
    }

    @Override
    public boolean deleteById(Long id) {
        boolean isEXX = playerRepository.existsById(id);
        if (isEXX) {
            playerRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Player findById(Long id) {
        return playerRepository.findById(id).get();
    }


    @Override
    public List<Player> findByParams(Map<String, String> params) {
        String name = (String) params.getOrDefault("name", null);
        String title = (String) params.getOrDefault("title", null);
        Profession profession = params.containsKey("profession") ? Profession.valueOf((String) params.get("profession")) : null;
        Race race = params.containsKey("race") ? Race.valueOf((String) params.get("race")) : null;
        Calendar calendar = Calendar.getInstance();
        Date after = null;
        if (params.containsKey("after")) {
            after = new Date(Long.parseLong(params.get("after")));
        }
        Date before = null;
        if (params.containsKey("before")) {
            before = new Date(Long.parseLong(params.get("before")));
        }
        Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : null;
        Integer minExperience = params.containsKey("minExperience") ? Integer.parseInt(params.get("minExperience")) : null;
        Integer maxExperience = params.containsKey("maxExperience") ? Integer.parseInt(params.get("maxExperience")) : null;
        Integer minLevel = params.containsKey("minLevel") ? Integer.parseInt(params.get("minLevel")) : null;
        Integer maxLevel = params.containsKey("maxLevel") ? Integer.parseInt(params.get("maxLevel")) : null;
        Integer untilNextLevel = params.containsKey("untilNextLevel") ? Integer.parseInt(params.get("untilNextLevel")) : null;
        Pageable pageable;
        int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));
        if (params.containsKey("order")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, (PlayerOrder.valueOf(params.get("order"))).getFieldName());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        return playerRepository.findAllByParams(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, untilNextLevel, pageable).stream().collect(Collectors.toList());

    }

    @Override
    public Integer countByParams(Map<String, String> params) {
        String name = (String) params.getOrDefault("name", null);
        String title = (String) params.getOrDefault("title", null);
        Profession profession = params.containsKey("profession") ? Profession.valueOf((String) params.get("profession")) : null;
        Race race = params.containsKey("race") ? Race.valueOf((String) params.get("race")) : null;
        Calendar calendar = Calendar.getInstance();
        Date after = null;
        if (params.containsKey("after")) {
            after = new Date(Long.parseLong(params.get("after")));
        }
        Date before = null;
        if (params.containsKey("before")) {
            before = new Date(Long.parseLong(params.get("before")));
        }
        Boolean banned = params.containsKey("banned") ? Boolean.parseBoolean(params.get("banned")) : null;
        Integer minExperience = params.containsKey("minExperience") ? Integer.parseInt(params.get("minExperience")) : null;
        Integer maxExperience = params.containsKey("maxExperience") ? Integer.parseInt(params.get("maxExperience")) : null;
        Integer minLevel = params.containsKey("minLevel") ? Integer.parseInt(params.get("minLevel")) : null;
        Integer maxLevel = params.containsKey("maxLevel") ? Integer.parseInt(params.get("maxLevel")) : null;
        Integer untilNextLevel = params.containsKey("untilNextLevel") ? Integer.parseInt(params.get("untilNextLevel")) : null;
        return playerRepository.countByParams(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, untilNextLevel);
    }

    @Override
    public Integer count() {
        try {
            return Math.toIntExact(playerRepository.count());
        } catch (ArithmeticException e) {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean existsById(Long id) {
        return playerRepository.existsById(id);
    }

    @Override
    public boolean isIdValid(Long id) {
        return (id > 0);
    }

    @Override
    public boolean isParamsValid(Map<String, String> params) {
        return validName(params.get("name")) &&
                validTitle(params.get("title")) &&
                validBirthday(params.get("birthday")) &&
                validExperience(params.get("experience")) &&
                validProfession(params.get("profession")) &&
                validRace(params.get("race"));
    }

    @Override
    public boolean validParamsForUpdate(Map<String, String> params) {
        boolean result = true;
        if (params.containsKey("name")) {
            result = validName(params.get("name"));
        }
        if (!result) {
            return false;
        }

        if (params.containsKey("title")) {
            result = validTitle(params.get("title"));
        }
        if (!result) {
            return false;
        }

        if (params.containsKey("birthday")) {
            result = validBirthday(params.get("birthday"));
        }
        if (!result) {
            return false;
        }

        if (params.containsKey("experience")) {
            result = validExperience(params.get("experience"));
        }
        if (!result) {
            return false;
        }

        if (params.containsKey("profession")) {
            result = validProfession(params.get("profession"));
        }
        if (!result) {
            return false;
        }

        if (params.containsKey("race")) {
            result = validRace(params.get("race"));
        }
        if (!result) {
            return false;
        }

        return true;
    }

    public boolean validName(String name) {
        return name != null && name.length() <= 12 && name.length() > 0;
    }

    public boolean validTitle(String title) {
        return title != null && title.length() <= 30 && title.length() > 0;
    }

    public boolean validBirthday(String strBirthday) {
        try {
            long birthday = Long.parseLong(strBirthday);
            if (birthday > 0) {
                Date dateBirthday = new Date(birthday);
                Date minDate = Date.valueOf(LocalDate.of(2000, 1, 1));
                Date maxDate = Date.valueOf(LocalDate.of(3000, 1, 1));
                return dateBirthday.after(minDate) && dateBirthday.before(maxDate);
            }
            return false;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public boolean validExperience(String experience) {
        try {
            int exp = Integer.parseInt(experience);
            return exp<=10000000 && exp >= 0;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public boolean validProfession(String profession) {
        try {
            Profession.valueOf(profession);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    public boolean validRace(String race) {
        try {
            Race.valueOf(race);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean isAllParamsFound(Map<String, String> params) {
        return params.containsKey("name")
                && params.containsKey("title")
                && params.containsKey("race")
                && params.containsKey("profession")
                && params.containsKey("birthday")
                //&& params.containsKey("banned")
                && params.containsKey("experience")
                /*&& params.containsKey("level")*/
                //  && params.containsKey("untilNextLevel")
                ;
    }
}
