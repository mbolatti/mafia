package com.mafia;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Member {
  private final int survellianceThreashold;
  private Date startDate;
  private String name;
  private Stack<Member> boss = new Stack<>();
  private List<Member> subordinates = new ArrayList<>();
  private boolean inJail = false;
  private Member current = this;
  private boolean specialSurveillance;

  public Member(Date startDate, String name, int survellianceThreashold) {
    this.startDate = startDate;
    this.name = name;
    this.survellianceThreashold = survellianceThreashold;
  }

  public Date getStartDate() {
    return startDate;
  }

  public String getName() {
    return name;
  }

  public Member getCurrentBoss() {
    return boss.peek();
  }

  public List<Member> getSubordinates() {
    return subordinates;
  }

  public void setSubordinates(List<Member> subordinates) {
    this.subordinates = subordinates;
  }

  public boolean isSpecialSurveillance() {
    return specialSurveillance;
  }

  public boolean isInJail() {
    return inJail;
  }

  public Stack<Member> getBoss() {
    return boss;
  }

  /**
   * Add a new member to the current member (boss)
   * @param subordinate new destination boss
   * @return the current member (boss) with the new member assigned
   */
  public Member addSubordinate(Member subordinate) {
    if (!this.equals(subordinate)) {
      this.subordinates.add(subordinate);
      if (subordinate.boss.search(this) == -1) {
        subordinate.gotANewBoss(this);
      }
    }
    evaluateSpecialSurveillance();
    return this;
  }

  /**
   * Transfer subordinates from one boss to another when the boss goes out of the organization
   * @param newBoss new destination boss
   * @return the current member with the new boss assigned
   */
  public Member transferSubordinate(Member newBoss) {
    Member superBoss = this.getCurrentBoss();
    superBoss.getSubordinates().remove(current);
    if (this.boss.search(newBoss) != -1) {
      while (this.boss.peek() != newBoss) {
        this.boss.pop();
      }
    }
    this.getSubordinates().forEach(newBoss::addSubordinate);
    newBoss.addSubordinate(this);
    superBoss.evaluateSpecialSurveillance();
    evaluateSpecialSurveillance();
    return this;
  }

  /**
   * Setup a new boss for the current member
   * @param newBoss new boss for the current member
   * @return the current member with the new boss assigned
   */
  public Member gotANewBoss(Member newBoss) {
    boss.push(newBoss);
    return this;
  }

  public Member getSubordinateByName(String name) {
    return this.subordinates.stream().filter(sub -> sub.getName().equals(name)).findFirst().get();
  }

  /** Setup a boss into the jail */
  public void gotInJail() {
    if (!inJail) {
      this.inJail = true;
      this.getCurrentBoss().removeSubordinate(this);
    }
  }

  /** Setup a boss out the jail */
  public void gotOutJail() {
    if (inJail) {
      this.inJail = false;
      this.getCurrentBoss().addSubordinate(this);
    }
  }

  /**
   * Find the eldest member from a list
   * @param memberList list of members
   * @return the eldest member of the list
   */
  public Optional<Member> findTheEldest(List<Member> memberList) {
    return memberList.stream()
            .filter(mem -> !mem.equals(this))
            .sorted(Comparator.comparing((mem) -> mem.getStartDate().getTime()))
            .findFirst();
  }

  /**
   * Method to move all the subordinates of current boss to a new boss
   * @param newBoss new destination boss
   */
  public void moveMySubordinatesToAnotherBoss(Member newBoss) {
    this.getSubordinates().forEach(sub -> newBoss.addSubordinate(sub));
    if (newBoss.getCurrentBoss() != this.getCurrentBoss()) {
      this.getCurrentBoss().addSubordinate(newBoss);
    }
    removeSubordinates(this.getSubordinates());
    evaluateSpecialSurveillance();
  }

  /**
   * Remove the subordinate from the current member
   * @param subordinate subordinate to be removed
   */
  public void removeSubordinate(Member subordinate) {
    this.subordinates.remove(subordinate);
    evaluateSpecialSurveillance();
  }

  /**
   * Remove the subordinates from the current member
   * @param subordinates subordinates to be removed
   */
  private void removeSubordinates(List<Member> subordinates) {
    this.subordinates.removeAll(subordinates);
    evaluateSpecialSurveillance();
  }

  /**
   * Print the current structure below this member
   * @param level level into the tree
   */
  public void print(int level) {
    for (int i = 1; i < level; i++) {
      System.out.print("\t");
    }
    if (!this.inJail) {
      System.out.println(this.getName());
    }
    for (Member child : subordinates) {
      child.print(level + 1);
    }
  }

  /** Get the quantity of members below of this member */
  public int getFullSubordinatesMemberQuantity() {
    return this.flattened().map((mem) -> mem.getSubordinates().size()).reduce(Integer::sum).get();
  }

  /**
   * Get all the members at the same level
   *
   * @return a list of members at the same level in the organization
   */
  public Optional<List<Member>> getOtherMembersAtSameLevel() {
    return Optional.of(
            current.getCurrentBoss().getSubordinates().stream()
                    .filter(sub -> sub != current)
                    .filter(sub -> sub.boss.search(current) == -1)
                    .collect(Collectors.toList()));
  }

  private void evaluateSpecialSurveillance() {
    specialSurveillance = getFullSubordinatesMemberQuantity() > survellianceThreashold;
  }

  private Stream<Member> flattened() {
    return Stream.concat(
            Stream.of(this), this.getSubordinates().stream().flatMap(Member::flattened));
  }
}
