package ro.msg.cm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.cm.model.Candidate;
import ro.msg.cm.pojo.Duplicate;
import ro.msg.cm.repository.CandidateRepository;
import ro.msg.cm.types.DuplicateType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DuplicateFinderService {

    private final CandidateRepository candidateRepository;

    @Autowired
    public DuplicateFinderService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    /**
     * Method that returns all the duplicates of the Candidate
     *
     * @param id is the id of the original Candidate you compare to
     * @return the list of duplicates
     */
    public List<Duplicate> getDuplicates(Long id) {
        Candidate original = candidateRepository.findOne(id);
        List<Duplicate> allDuplicates = new ArrayList<>();
        Set<Long> ids;
        ids = candidateRepository.findAllByFirstNameAndLastName(original.getFirstName(), original.getLastName()).stream().map(Candidate::getId).collect(Collectors.toSet());
        ids.remove(original.getId());
        if (!ids.isEmpty()) {
            allDuplicates.add(new Duplicate(original.getId(), ids, DuplicateType.ON_NAME));
        }
        ids = candidateRepository.findAllByEmail(original.getEmail()).stream().map(Candidate::getId).collect(Collectors.toSet());
        ids.remove(original.getId());
        if (!ids.isEmpty()) {
            allDuplicates.add(new Duplicate(original.getId(), ids, DuplicateType.ON_EMAIL));
        }
        ids = candidateRepository.findAllByPhone(original.getPhone()).stream().map(Candidate::getId).collect(Collectors.toSet());
        ids.remove(original.getId());
        if (!ids.isEmpty()) {
            allDuplicates.add(new Duplicate(original.getId(), ids, DuplicateType.ON_PHONE));
        }
        return allDuplicates;
    }
}
