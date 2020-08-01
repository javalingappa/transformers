package com.game.transformer.entity;
/**
 * @author Javalingappa
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "TRANSFORMER")
public class Transformer implements Comparable<Transformer> {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "transformer", unique = true)
    private String transformer = null;

    @Column(name = "type")
    private String type = null;

    @Column(name = "strength")
    private Integer strength = null;

    @Column(name = "intelligence")
    private Integer intelligence = null;

    @Column(name = "speed")
    private Integer speed = null;

    @Column(name = "endurance")
    private Integer endurance = null;

    @Column(name = "rank")
    private Integer rank = null;

    @Column(name = "courage")
    private Integer courage = null;

    @Column(name = "firepower")
    private Integer firepower = null;

    @Column(name = "skill")
    private Integer skill = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTransformer() {
        return transformer;
    }

    public void setTransformer(String transformer) {
        this.transformer = transformer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getEndurance() {
        return endurance;
    }

    public void setEndurance(Integer endurance) {
        this.endurance = endurance;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getCourage() {
        return courage;
    }

    public void setCourage(Integer courage) {
        this.courage = courage;
    }

    public Integer getFirepower() {
        return firepower;
    }

    public void setFirepower(Integer firepower) {
        this.firepower = firepower;
    }

    public Integer getSkill() {
        return skill;
    }

    public void setSkill(Integer skill) {
        this.skill = skill;
    }

    @Override
    public String toString() {
        return "Transformer{" +
                "id='" + id + '\'' +
                ", transformer='" + transformer + '\'' +
                ", type='" + type + '\'' +
                ", strength=" + strength +
                ", intelligence=" + intelligence +
                ", speed=" + speed +
                ", endurance=" + endurance +
                ", rank=" + rank +
                ", courage=" + courage +
                ", firepower=" + firepower +
                ", skill=" + skill +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transformer that = (Transformer) o;
        return id.equals(that.id) &&
                transformer.equals(that.transformer) &&
                type.equals(that.type) &&
                strength.equals(that.strength) &&
                intelligence.equals(that.intelligence) &&
                speed.equals(that.speed) &&
                endurance.equals(that.endurance) &&
                rank.equals(that.rank) &&
                courage.equals(that.courage) &&
                firepower.equals(that.firepower) &&
                skill.equals(that.skill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transformer, type, strength, intelligence, speed, endurance, rank, courage, firepower, skill);
    }

    @Override
    public int compareTo(Transformer o) {
        return this.getRank().compareTo(o.getRank());
    }
}