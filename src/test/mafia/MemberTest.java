package mafia;

import com.mafia.Member;
import jdk.jfr.Description;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MemberTest {
    private int SURVEILLANCE_THREASHOLD = 9;

    @Test
    @Description("check to move all the subordinates from one boss to another")
    public void moveFromSubordinatesFromOneToAnotherBoss() {
        Member tonyGF = addMember(null, "Tony");
        Member sullyB = addMember(tonyGF, "Sully");
        addMember(sullyB, "S1");
        addMember(sullyB, "S1");
        Member nicoB = addMember(tonyGF, "Nico");
        addMember(nicoB, "N1");
        sullyB.moveMySubordinatesToAnotherBoss(nicoB);
        Assert.assertEquals(0, sullyB.getSubordinates().size());
        Assert.assertEquals(3, nicoB.getSubordinates().size());
        Assert.assertEquals(
                0, nicoB.getSubordinates().stream().filter(sub -> sub.getCurrentBoss() != nicoB).count());
    }

    @Test
    @Description("check to add a subordinate")
    public void addSubordinate() {
        Member tonyGF = addMember(null, "Tony");
        Member sullyB = addMember(tonyGF, "Sully");
        addMember(sullyB, "S1");
        addMember(sullyB, "S1");
        Member nicoB = addMember(tonyGF, "Nico");
        addMember(nicoB, "N1");
        sullyB.addSubordinate(nicoB);
        Assert.assertEquals(3, sullyB.getSubordinates().size());
        Assert.assertTrue(nicoB.getSubordinates().stream().anyMatch(sub -> sub != nicoB));
    }

    @Test
    @Description("check to transfer subordinates from one boss to another")
    public void transferSubordinates() {
        Member tonyGF = addMember(null, "Tony");
        Member sullyB = addMember(tonyGF, "Sully");
        addMember(sullyB, "S1");
        addMember(sullyB, "S1");
        Member nicoB = addMember(tonyGF, "Nico");
        addMember(nicoB, "N1");
        sullyB.transferSubordinate(nicoB);
        Assert.assertEquals(4, nicoB.getSubordinates().size());
        Assert.assertEquals(0, nicoB.getSubordinates().stream().filter(sub -> sub.getCurrentBoss() != nicoB).count());
    }

    @Test
    @Description("set a new boss")
    public void gotANewBoss() {
        Member tonyGF = addMember(null, "Tony");
        Member sullyB = addMember(tonyGF, "Sully");
        addMember(sullyB, "S1");
        addMember(sullyB, "S1");
        Member nicoB = addMember(tonyGF, "Nico");
        addMember(nicoB, "N1");
        sullyB.gotANewBoss(nicoB);
        Assert.assertEquals(nicoB, sullyB.getCurrentBoss());
    }


    private Member addMember(Member boss, String name) {
        Member member = new Member(new Date(), name, SURVEILLANCE_THREASHOLD);
        if (boss != null) {
            boss.addSubordinate(member);
        }
        return member;
    }

    @Test
    @Description("check some guys go in/out of jail")
    public void goToJail() {
        Member tonyGF = addMember(null, "Tony");
        Member sullyB = addMember(tonyGF, "Sully");
        sullyB.gotInJail();
        Assert.assertTrue(sullyB.isInJail());
    }

    @Test
    @Description("find the Eldest")
    public void findTheEldest() {
        try {
            Member tonyGF = addMember(null, "Tony");
            Member sullyB = addMember(tonyGF, "Sully");
            addMember(sullyB, "S1");
            Thread.sleep(1000);
            addMember(sullyB, "S2");
            addMember(sullyB, "S3");
            addMember(sullyB, "S4");
            addMember(sullyB, "S5");
            Member nicoB = addMember(tonyGF, "Nico");
            addMember(nicoB, "N1");
            addMember(nicoB, "N2");
            addMember(nicoB, "N3");
            Member tempMember = sullyB.getSubordinates().get(2);
            sullyB.getSubordinates().set(2, sullyB.getSubordinates().get(0));
            sullyB.getSubordinates().set(0, tempMember);
            Assert.assertEquals(
                    sullyB.getSubordinates().get(2), sullyB.findTheEldest(sullyB.getSubordinates()).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Description("Move my subordinates to another boss")
    public void moveMySubordinatesToAnotherBoss() {
        Member tonyGF = addMember(null, "Tony");
        Member sullyB = addMember(tonyGF, "Sully");
        addMember(sullyB, "S1");
        addMember(sullyB, "S2");
        addMember(sullyB, "S3");
        addMember(sullyB, "S4");
        addMember(sullyB, "S5");
        Member nicoB = addMember(tonyGF, "Nico");
        addMember(nicoB, "N1");
        addMember(nicoB, "N2");
        addMember(nicoB, "N3");
        sullyB.moveMySubordinatesToAnotherBoss(nicoB);
        Assert.assertEquals(0, sullyB.getSubordinates().size());
        Assert.assertEquals(8, nicoB.getSubordinates().size());
    }

    @Test
    @Description("Remove subordinate")
    public void removeSubordinate() {
        Member tonyGF = addMember(null, "Tony");
        Member sullyB = addMember(tonyGF, "Sully");
        addMember(sullyB, "S1");
        addMember(sullyB, "S2");
        addMember(sullyB, "S3");
        addMember(sullyB, "S4");
        addMember(sullyB, "S5");
        Member nicoB = addMember(tonyGF, "Nico");
        addMember(nicoB, "N1");
        addMember(nicoB, "N2");
        addMember(nicoB, "N3");
        Member tempMember = sullyB.getSubordinates().get(0);
        sullyB.removeSubordinate(sullyB.getSubordinates().get(0));
        Assert.assertFalse(sullyB.getSubordinates().stream().anyMatch(sub -> sub == tempMember));
        Assert.assertTrue(sullyB.getCurrentBoss() == tonyGF);
    }

    @Test
    @Description("Go out of jail")
    public void goOutOfJail() {
        Member tonyGF = addMember(null, "Tony");
        Member sullyB = addMember(tonyGF, "Sully");
        sullyB.gotInJail();
        Assert.assertTrue(sullyB.isInJail());
        sullyB.gotOutJail();
        Assert.assertFalse(sullyB.isInJail());
    }

    @Test
    @Description("Quantity of subordinates")
    public void getFullSubordinatesMemberQuantity() {
        Member tonyGF = addMember(null, "Tony");
        Member sullyB = addMember(tonyGF, "Sully");
        addMember(sullyB, "S1");
        addMember(sullyB, "S2");
        addMember(sullyB, "S3");
        addMember(sullyB, "S4");
        addMember(sullyB, "S5");
        Member nicoB = addMember(tonyGF, "Nico");
        addMember(nicoB, "N1");
        addMember(nicoB, "N2");
        addMember(nicoB, "N3");
        Assert.assertEquals(10, tonyGF.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(5, sullyB.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(3, nicoB.getFullSubordinatesMemberQuantity());
        Member marcoB = addMember(tonyGF, "Marco");
        addMember(marcoB, "M1");
        addMember(marcoB, "M3");
        addMember(marcoB, "M2");
        addMember(marcoB, "M4");
        addMember(marcoB, "M5");
        addMember(marcoB, "M6");
        addMember(sullyB, "S6");
        addMember(nicoB, "N4");
        addMember(nicoB, "N5");
        Assert.assertEquals(20, tonyGF.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(6, sullyB.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(5, nicoB.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(6, marcoB.getFullSubordinatesMemberQuantity());
    }

    @Test
    @Description("Other members at the same level")
    public void getOtherMembersAtSameLevel() {
        Member tonyGF = addMember(null, "Tony");
        Member sullyB = addMember(tonyGF, "Sully");
        addMember(sullyB, "S1");
        addMember(sullyB, "S2");
        addMember(sullyB, "S3");
        addMember(sullyB, "S4");
        addMember(sullyB, "S5");
        addMember(sullyB, "S6");
        Member nicoB = addMember(tonyGF, "Nico");
        addMember(nicoB, "N1");
        addMember(nicoB, "N2");
        addMember(nicoB, "N3");
        addMember(nicoB, "N4");
        addMember(nicoB, "N5");
        Member marcoB = addMember(tonyGF, "Marco");
        addMember(marcoB, "M1");
        addMember(marcoB, "M3");
        addMember(marcoB, "M2");
        addMember(marcoB, "M4");
        addMember(marcoB, "M5");
        addMember(marcoB, "M6");
        Optional<List<Member>> bosses = sullyB.getOtherMembersAtSameLevel();
        Assert.assertEquals(2, bosses.get().size());
        Assert.assertNotNull(bosses.get().stream().filter(sub -> sub.equals(nicoB)).findFirst());
        Assert.assertNotNull(bosses.get().stream().filter(sub -> sub.equals(marcoB)).findFirst());
    }
}
