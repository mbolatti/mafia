package com.mafia;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to mantain the Mafia structure
 */

public class UpdateServiceImpl implements UpdateService{
    private final List<Member> organization;
    private static int SURVEILLANCE_THREASHOLD = 9;


    public UpdateServiceImpl(List<Member> organization) {
        this.organization = organization;
    }

    @Override
    public List<Member> getMembersUnderSurvelliance() {
    return organization.stream()
        .filter((mem) -> !mem.isInJail() && mem.isSpecialSurveillance())
        .collect(Collectors.toList());
    }

    @Override
    public Member addMember(Member boss, String name) {
        Member member = new Member(new Date(), name, SURVEILLANCE_THREASHOLD);
        addToOrganization(member);
        if (boss != null) {
            boss.addSubordinate(member);
        }
        return member;
    }

    @Override
    public void goOutJail(Member formerConvict) {
        formerConvict.gotOutJail();
        List<Member> myFormerSubordinates =
                formerConvict.getCurrentBoss().getSubordinates().stream()
                        .filter((sub) -> !sub.isInJail())
                        .flatMap(x -> x.getSubordinates().stream())
                        .filter(sub -> sub.getBoss().search(formerConvict) != -1)
                        .collect(Collectors.toList());
        List<Member> formerSubordinatesPromoted =
                formerConvict.getCurrentBoss().getSubordinates().stream()
                        .filter((sub) -> !sub.isInJail())
                        .filter(sub -> sub.getBoss().search(formerConvict) != -1)
                        .collect(Collectors.toList());
        myFormerSubordinates.forEach((sub) -> sub.transferSubordinate(formerConvict));
        formerSubordinatesPromoted.forEach((sub) -> sub.transferSubordinate(formerConvict));

    }

    @Override
    public void goInJail(Member boss) {
        boss.gotInJail();
        Member newBoss =
                boss.findTheEldest(boss.getOtherMembersAtSameLevel().get())
                        .orElseGet(() -> boss.findTheEldest(boss.getSubordinates()).get());
        boss.moveMySubordinatesToAnotherBoss(newBoss);
    }

    private void addToOrganization(Member member) {
        organization.add(member);
    }
}
