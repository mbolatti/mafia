package com.mafia;

import java.util.List;

public interface UpdateService {
    List<Member> getMembersUnderSurvelliance();
    Member addMember(Member boss, String name);
    void goOutJail(Member formerConvict);
    void goInJail(Member boss);
}
