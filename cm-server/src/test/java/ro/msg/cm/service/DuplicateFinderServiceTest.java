package ro.msg.cm.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import ro.msg.cm.model.Candidate;
import ro.msg.cm.pojo.Duplicate;
import ro.msg.cm.repository.CandidateRepository;
import ro.msg.cm.types.DuplicateType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

public class DuplicateFinderServiceTest {

    @InjectMocks
    private DuplicateFinderService duplicateFinderService;

    @Mock
    private CandidateRepository mockCandidateRepository;

    private List<Candidate> candidateList;

    private void setUpMockCandidateRepository() {
        when(mockCandidateRepository.findAllByFirstNameAndLastName(Mockito.anyString(), Mockito.anyString())).thenAnswer(
                (Answer<List<Candidate>>) answer ->
                        candidateList.stream().
                                filter(x -> x.getFirstName().equals(answer.getArgumentAt(0, String.class)) && x.getLastName().equals(answer.getArgumentAt(1, String.class))).
                                collect(Collectors.toList())
        );

        when(mockCandidateRepository.findAllByEmail(Mockito.anyString())).thenAnswer(
                (Answer<List<Candidate>>) answer ->
                        candidateList.stream().
                                filter(x -> x.getEmail().equals(answer.getArgumentAt(0, String.class))).
                                collect(Collectors.toList())
        );

        when(mockCandidateRepository.findAllByPhone(Mockito.anyString())).thenAnswer(
                (Answer<List<Candidate>>) answer ->
                        candidateList.stream().
                                filter(x -> x.getPhone().equals(answer.getArgumentAt(0, String.class))).
                                collect(Collectors.toList())
        );

        when(mockCandidateRepository.findOne(Mockito.anyLong())).thenAnswer(
                (Answer<Candidate>) answer ->
                        candidateList.stream().
                                filter(x -> x.getId().equals(answer.getArgumentAt(0, Long.class))).
                                collect(Collectors.toList()).get(0)
        );
    }

    private void setUpCandidateList() {
        candidateList = new ArrayList<>();
        //2 with nothing in common
        candidateList.add(getCandidate(1,"Nicolae"    , "Simon"       , "jrangle0@jalbum.net"                 , "9925848027"));
        candidateList.add(getCandidate(2,"Horia"      , "Victoria"    , "cshallow1@baidu.com,"                , "1054647890"));

        //2 with name in common
        candidateList.add(getCandidate(7,"Elena"      , "Marin"       , "phasely2@google.es"                  , "9565314990"));
        candidateList.add(getCandidate(8,"Elena"      , "Marin"       , "rvillalta3@de.vu"                    , "8161697903"));

        //2 with phone in common
        candidateList.add(getCandidate(14,"Ramona"     , "Veronica"    , "vosbidston4@privacy.gov.au"          , "6697627176"));
        candidateList.add(getCandidate(15,"Olimpia"    , "Luca"        , "ivaines5@deliciousdays.com"          , "6697627176"));

        //2 with email in common
        candidateList.add(getCandidate(21,"Petre"      , "Marin"       , "vmizzi6@hugedomains.com"             , "1679370689"));
        candidateList.add(getCandidate(22,"Cornelia"   , "Iuliu"       , "vmizzi6@hugedomains.com"             , "8876908185"));

        //2 with name and phone in common
        candidateList.add(getCandidate(28,"Olimpia"    , "Andrei"      , "scassely8@scientificamerican.com"    , "6097644586"));
        candidateList.add(getCandidate(29,"Olimpia"    , "Andrei"      , "rmerrisson9@gmpg.org"                , "6097644586"));

        //2 with name and email in common
        candidateList.add(getCandidate(35,"Angela"     , "Narcisa"     , "cmellishb@phpbb.com"                 , "8047009418"));
        candidateList.add(getCandidate(36,"Angela"     , "Narcisa"     , "cmellishb@phpbb.com"                 , "7036829152"));

        //2 with phone and email in common
        candidateList.add(getCandidate(42,"Costache"   , "Doina"       , "dbennied@aol.com"                    , "2712211158"));
        candidateList.add(getCandidate(43,"Roxana"     , "Constantin"  , "dbennied@aol.com"                    , "2712211158"));

        //2 with name and phone and email in common
        candidateList.add(getCandidate(49,"Aurel"      , "Stelian"     , "nsutherley1@princeton.edu"           , "8266709053"));
        candidateList.add(getCandidate(50,"Aurel"      , "Stelian"     , "nsutherley1@princeton.edu"           , "8266709053"));
    }

    private Candidate getCandidate(long id,String firstName, String lastName, String email, String phone) {
        Candidate candidate = new Candidate();
        candidate.setId(id);
        candidate.setFirstName(firstName);
        candidate.setLastName(lastName);
        candidate.setEmail(email);
        candidate.setPhone(phone);
        return candidate;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setUpMockCandidateRepository();
        setUpCandidateList();
    }

    @Test
    public void findDuplicatesWithNothing1() {
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(1L);
        Assert.assertEquals("Assert the nr of duplications:",0,duplicates.size());
    }

    @Test
    public void findDuplicatesWithNothing2() {
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(2L);
        Assert.assertEquals("Assert the nr of duplications:",0,duplicates.size());
    }

    @Test
    public void findDuplicatesWithName1() {
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(7L);
        Assert.assertEquals("Assert the nr of duplications: ",1,duplicates.size());
        Assert.assertEquals("Assert type of duplication: ", DuplicateType.ON_NAME,duplicates.get(0).getDuplicateType());
        Assert.assertEquals("Assert duplication's id: ",8L,duplicates.get(0).getIds().toArray()[0]);
    }

    @Test
    public void findDuplicatesWithName2() {
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(8L);
        Assert.assertEquals("Assert the nr of duplications:",1,duplicates.size());
        Assert.assertEquals("Assert type of duplication:", DuplicateType.ON_NAME,duplicates.get(0).getDuplicateType());
        Assert.assertEquals("Assert duplication's id:",7L, duplicates.get(0).getIds().toArray()[0]);
    }

    @Test
    public void findDuplicatesWithPhone1() {
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(14L);
        Assert.assertEquals("Assert the nr of duplications:",1,duplicates.size());
        Assert.assertEquals("Assert type of duplication:", DuplicateType.ON_PHONE,duplicates.get(0).getDuplicateType());
        Assert.assertEquals("Assert duplication's id:", 15L,duplicates.get(0).getIds().toArray()[0]);
    }

    @Test
    public void findDuplicatesWithPhone2() {
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(15L);
        Assert.assertEquals("Assert the nr of duplications:",1,duplicates.size());
        Assert.assertEquals("Assert type of duplication:", DuplicateType.ON_PHONE,duplicates.get(0).getDuplicateType());
        Assert.assertEquals("Assert duplication's id:", 14L,duplicates.get(0).getIds().toArray()[0]);
    }

    @Test
    public void findDuplicatesWithEmail1() {
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(21L);
        Assert.assertEquals("Assert the nr of duplications:",1,duplicates.size());
        Assert.assertEquals("Assert type of duplication:", DuplicateType.ON_EMAIL,duplicates.get(0).getDuplicateType());
        Assert.assertEquals("Assert duplication's id:", 22L,duplicates.get(0).getIds().toArray()[0]);
    }

    @Test
    public void findDuplicatesWithEmail2() {
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(22L);
        Assert.assertEquals("Assert the nr of duplications:",1,duplicates.size());
        Assert.assertEquals("Assert type of duplication:", DuplicateType.ON_EMAIL,duplicates.get(0).getDuplicateType());
        Assert.assertEquals("Assert duplication's id:", 21L,duplicates.get(0).getIds().toArray()[0]);
    }

    @Test
    public void findDuplicatesWithNameAndPhone1() {
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(28L);
        Assert.assertEquals("Assert the nr of duplications:",2,duplicates.size());
        for (Duplicate duplicate : duplicates) {
            Assert.assertTrue("Assert type of duplication:",duplicate.getDuplicateType()!= DuplicateType.ON_EMAIL);
        }
        Assert.assertEquals("Assert duplication's id:", 29L,duplicates.get(0).getIds().toArray()[0]);
        Assert.assertEquals("Assert duplication's id:", 29L,duplicates.get(1).getIds().toArray()[0]);
    }

    @Test
    public void findDuplicatesWithNameAndPhone2() {
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(29L);
        Assert.assertEquals("Assert the nr of duplications:",2,duplicates.size());
        for (Duplicate duplicate : duplicates) {
            Assert.assertTrue("Assert type of duplication:",duplicate.getDuplicateType()!= DuplicateType.ON_EMAIL);
        }
        Assert.assertEquals("Assert duplication's id:", 28L,duplicates.get(0).getIds().toArray()[0]);
        Assert.assertEquals("Assert duplication's id:", 28L,duplicates.get(1).getIds().toArray()[0]);
    }

    @Test
    public void findDuplicatesWithNameAndEmail1() {
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(35L);
        Assert.assertEquals("Assert the nr of duplications:", 2, duplicates.size());
        for (Duplicate duplicate : duplicates) {
            Assert.assertTrue("Assert type of duplication:", duplicate.getDuplicateType() != DuplicateType.ON_PHONE);
        }
        Assert.assertEquals("Assert duplication's id:", 36L, duplicates.get(0).getIds().toArray()[0]);
        Assert.assertEquals("Assert duplication's id:", 36L, duplicates.get(1).getIds().toArray()[0]);
    }

    @Test
    public void findDuplicatesWithNameAndEmail2() {
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(36L);
        Assert.assertEquals("Assert the nr of duplications:", 2, duplicates.size());
        for (Duplicate duplicate : duplicates) {
            Assert.assertTrue("Assert type of duplication:", duplicate.getDuplicateType() != DuplicateType.ON_PHONE);
        }
        Assert.assertEquals("Assert duplication's id:", 35L, duplicates.get(0).getIds().toArray()[0]);
        Assert.assertEquals("Assert duplication's id:", 35L, duplicates.get(1).getIds().toArray()[0]);
    }

    @Test
    public void findDuplicatesWithPhoneAndEmail1(){
    List<Duplicate> duplicates = duplicateFinderService.getDuplicates(42L);
        Assert.assertEquals("Assert the nr of duplications:", 2, duplicates.size());
        for (Duplicate duplicate : duplicates) {
        Assert.assertTrue("Assert type of duplication:", duplicate.getDuplicateType() != DuplicateType.ON_NAME);
    }
        Assert.assertEquals("Assert duplication's id:", 43L, duplicates.get(0).getIds().toArray()[0]);
        Assert.assertEquals("Assert duplication's id:", 43L, duplicates.get(1).getIds().toArray()[0]);
    }

    @Test
    public void findDuplicatesWithPhoneAndEmail2(){
    List<Duplicate> duplicates = duplicateFinderService.getDuplicates(43L);
        Assert.assertEquals("Assert the nr of duplications:", 2, duplicates.size());
        for (Duplicate duplicate : duplicates) {
        Assert.assertTrue("Assert type of duplication:", duplicate.getDuplicateType() != DuplicateType.ON_NAME);
    }
        Assert.assertEquals("Assert duplication's id:", 42L, duplicates.get(0).getIds().toArray()[0]);
        Assert.assertEquals("Assert duplication's id:", 42L, duplicates.get(1).getIds().toArray()[0]);
    }

    @Test
    public void findDuplicatesWithNameAndPhoneAndEmail1() {
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(49L);
        Assert.assertEquals("Assert the nr of duplications:", 3, duplicates.size());
        Assert.assertEquals("Assert duplication's id:", 50L, duplicates.get(0).getIds().toArray()[0]);
        Assert.assertEquals("Assert duplication's id:", 50L, duplicates.get(1).getIds().toArray()[0]);
        Assert.assertEquals("Assert duplication's id:", 50L, duplicates.get(2).getIds().toArray()[0]);
    }

    @Test
    public void findDuplicatesWithNameAndPhoneAndEmail2() {
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(50L);
        Assert.assertEquals("Assert the nr of duplications:", 3, duplicates.size());
        Assert.assertEquals("Assert duplication's id:", 49L, duplicates.get(0).getIds().toArray()[0]);
        Assert.assertEquals("Assert duplication's id:", 49L, duplicates.get(1).getIds().toArray()[0]);
        Assert.assertEquals("Assert duplication's id:", 49L, duplicates.get(2).getIds().toArray()[0]);
    }
}