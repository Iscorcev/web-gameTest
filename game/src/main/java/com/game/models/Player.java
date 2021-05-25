package com.game.models;

import com.game.entity.Profession;
import com.game.entity.Race;



import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "Player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    @Enumerated(EnumType.STRING)
    private Race race;
    @Enumerated(EnumType.STRING)
    private Profession profession;
    private Integer experience;
    private Integer untilNextLevel;
    private Date birthday;
    private Boolean banned;
    private Integer level;
    //@Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")

    public Player() {
    }

    public Player( String name, String title, Race race, Profession profession, Integer experience, Date birthday, Boolean banned) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        //this.level=level;
        //this.untilNextLevel = untilNextLevel;
        this.birthday = birthday;
        this.banned=banned;

    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", race=" + race +
                ", profession=" + profession +
                ", experience=" + experience +
                ", untilNextLevel=" + untilNextLevel +
                ", birthday=" + birthday +
                ", banned=" + banned +
                ", level=" + level +
                '}';
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
   // @Column(name = "race")
    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
   // @Column(name = "profession")
    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }



   public void calculateLevel() {
       Integer newLevel =(int) (Math.sqrt(2500+200*getExperience())-50)/ 100;
       setLevel(newLevel);
    }
    public void calculateUntilNextLevel() {
        Integer untilNextLevel = 50*(level+1)*(level+2)-getExperience();
        setUntilNextLevel(untilNextLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id) && name.equals(player.name) && title.equals(player.title) && race == player.race && profession == player.profession && experience.equals(player.experience) && Objects.equals(untilNextLevel, player.untilNextLevel) && birthday.equals(player.birthday) && banned.equals(player.banned) && Objects.equals(level, player.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, race, profession, experience, untilNextLevel, birthday, banned, level);
    }

    public static void main(String[] args) {

    }
}
