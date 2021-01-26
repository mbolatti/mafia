package mafia;

import com.mafia.Member;
import com.mafia.UpdateServiceImpl;
import jdk.jfr.Description;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UpdateServiceTest {
    private static List<Member> organizationMembers = null;
    private Member godFather, johnny, sully, vitto;
    private UpdateServiceImpl updateService;

    @Test
    @Description("each boss goes to jail and out in a sequenced way in->out")
    public void checkTransferBack(){
        genericInit();
        List<Member> expectedUnderSurvellinaceMembes;
        updateService.goInJail(johnny);
        Assert.assertEquals(23, organizationMembers.size());
        Assert.assertEquals(21, godFather.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, johnny.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(10, sully.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(9, vitto.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(1, organizationMembers.stream().filter((mem) -> mem.isInJail()).count());
        expectedUnderSurvellinaceMembes = new ArrayList<>();
        expectedUnderSurvellinaceMembes.add(godFather);
        expectedUnderSurvellinaceMembes.add(sully);
        assertSurvellinace(expectedUnderSurvellinaceMembes);
        updateService.goOutJail(johnny);
        Assert.assertEquals(23, organizationMembers.size());
        Assert.assertEquals(22, godFather.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(3, johnny.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(7, sully.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(9, vitto.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, organizationMembers.stream().filter((mem) -> mem.isInJail()).count());
        expectedUnderSurvellinaceMembes = new ArrayList<>();
        expectedUnderSurvellinaceMembes.add(godFather);
        assertSurvellinace(expectedUnderSurvellinaceMembes);
        updateService.goInJail(sully);
        Assert.assertEquals(23, organizationMembers.size());
        Assert.assertEquals(21, godFather.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(10, johnny.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, sully.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(9, vitto.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(1, organizationMembers.stream().filter((mem) -> mem.isInJail()).count());
        expectedUnderSurvellinaceMembes = new ArrayList<>();
        expectedUnderSurvellinaceMembes.add(godFather);
        expectedUnderSurvellinaceMembes.add(johnny);
        assertSurvellinace(expectedUnderSurvellinaceMembes);
        updateService.goOutJail(sully);
        Assert.assertEquals(23, organizationMembers.size());
        Assert.assertEquals(22, godFather.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(3, johnny.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(7, sully.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(9, vitto.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, organizationMembers.stream().filter((mem) -> mem.isInJail()).count());
        expectedUnderSurvellinaceMembes = new ArrayList<>();
        expectedUnderSurvellinaceMembes.add(godFather);
        assertSurvellinace(expectedUnderSurvellinaceMembes);
        updateService.goInJail(vitto);
        Assert.assertEquals(23, organizationMembers.size());
        Assert.assertEquals(21, godFather.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(12, johnny.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(7, sully.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, vitto.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(1, organizationMembers.stream().filter((mem) -> mem.isInJail()).count());
        expectedUnderSurvellinaceMembes = new ArrayList<>();
        expectedUnderSurvellinaceMembes.add(godFather);
        expectedUnderSurvellinaceMembes.add(johnny);
        assertSurvellinace(expectedUnderSurvellinaceMembes);
        updateService.goOutJail(vitto);
        Assert.assertEquals(23, organizationMembers.size());
        Assert.assertEquals(22, godFather.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(3, johnny.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(7, sully.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(9, vitto.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, organizationMembers.stream().filter((mem) -> mem.isInJail()).count());
        expectedUnderSurvellinaceMembes = new ArrayList<>();
        expectedUnderSurvellinaceMembes.add(godFather);
        assertSurvellinace(expectedUnderSurvellinaceMembes);
    }

    @Test
    @Description("all the bosses goes to jail in sequence and out in reverse sequence")
    public void checkOtherTransferBack(){
        genericInit();
        List<Member> underSurvellinace;
        Member j0 = johnny.getSubordinateByName("J0");
        updateService.goInJail(johnny);
        Assert.assertEquals(23, organizationMembers.size());
        Assert.assertEquals(21, godFather.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, johnny.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(10, sully.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(9, vitto.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(1, organizationMembers.stream().filter((mem) -> mem.isInJail()).count());

        updateService.goInJail(sully);
        Assert.assertEquals(23, organizationMembers.size());
        Assert.assertEquals(20, godFather.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, johnny.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, sully.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(19, vitto.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(2, organizationMembers.stream().filter((mem) -> mem.isInJail()).count());

        updateService.goInJail(vitto);
        Assert.assertEquals(23, organizationMembers.size());
        Assert.assertEquals(19, godFather.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, johnny.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, sully.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, vitto.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(18, j0.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(3, organizationMembers.stream().filter((mem) -> mem.isInJail()).count());

        updateService.goOutJail(vitto);
        Assert.assertEquals(23, organizationMembers.size());
        Assert.assertEquals(20, godFather.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, johnny.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, sully.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(19, vitto.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(0, j0.getFullSubordinatesMemberQuantity());
        Assert.assertEquals(2, organizationMembers.stream().filter((mem) -> mem.isInJail()).count());
    }

    private void assertSurvellinace(List<Member> expectedUnderSurvellianceMembers) {
        List<Member> current = updateService.getMembersUnderSurvelliance();
        Assert.assertEquals(expectedUnderSurvellianceMembers, current);
    }

    private void genericInit() {
        organizationMembers = new ArrayList<>();
        updateService = new UpdateServiceImpl(organizationMembers);
        godFather = updateService.addMember(null, "Michelle");

        try {
            johnny = updateService.addMember(godFather, "Johnny");
            addMember(johnny, "J", 3);
            Thread.sleep(2000);
            sully = updateService.addMember(godFather, "Sully");
            addMember(sully, "S", 7);
            Thread.sleep(2000);
            vitto = updateService.addMember(godFather, "Vitto");
            addMember(vitto, "V", 9);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addMember(
            Member boss, String prefixNameSubordinate, int membersQuantity) {
        for (int i = 0; i < membersQuantity; i++) {
            updateService.addMember(boss, prefixNameSubordinate+i);
        }
    }
}
