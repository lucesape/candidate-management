package ro.msg.cm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ro.msg.cm.exception.CandidateIsAlreadyValidatedException;
import ro.msg.cm.exception.CandidateNotFoundException;
import ro.msg.cm.model.Candidate;
import ro.msg.cm.pojo.Duplicate;
import ro.msg.cm.repository.CandidateRepository;
import ro.msg.cm.types.CandidateCheck;
import ro.msg.cm.types.DuplicateType;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ValidationService {

    private final CandidateRepository candidateRepository;

    @Autowired
    public ValidationService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public Candidate patchCandidate(Map<Object, Object> patchCandidate, Long id){
        Candidate candidate = candidateRepository.findByIdAndCheckCandidate(id, CandidateCheck.NOT_YET_VALIDATED);
        if(candidate!=null) {
            patchCandidate.forEach((k, v) -> {
                Field field = ReflectionUtils.findField(Candidate.class, String.valueOf(k));
                field.setAccessible(true);
                ReflectionUtils.setField(field, candidate, v);
                field.setAccessible(false);
            });
            return candidateRepository.save(candidate);
        }else {
            throw new CandidateNotFoundException();
        }
    }

    public void deleteSelectedEntry(Long id) {
        Candidate candidate = candidateRepository.findByIdAndCheckCandidate(id, CandidateCheck.NOT_YET_VALIDATED);
        if (candidate != null) {
                candidateRepository.delete(id);
            } else {
                throw new CandidateIsAlreadyValidatedException();
            }
    }

    public void deleteSelectedEntries(List<Long> ids) {
        for (long id : ids) {
            if (candidateRepository.exists(id)) {
                candidateRepository.delete(id);
            }else {
                throw new CandidateNotFoundException("Candidate with id: " + id + " was not found");
            }
        }
    }

    public void validate(Long id) {
        if(candidateRepository.findCandidateById(id).isPresent()) {
            if (!duplicateOn(id, DuplicateType.ON_EMAIL)) {
                candidateRepository.setCheckCandidateForId(CandidateCheck.VALIDATED, id);
            }
        } else {
            throw new CandidateNotFoundException();
        }
    }

    public void validate(List<Long> ids) {
        for (long id : ids) {
                if (duplicateOn(id, DuplicateType.ON_EMAIL)) {
                    ids.remove(id);
                }
        }
        candidateRepository.setCheckCandidateForIdIn(CandidateCheck.VALIDATED, ids);
    }

    private boolean duplicateOn(Long id, DuplicateType duplicateType) {
        DuplicateFinderService duplicateFinderService = new DuplicateFinderService(candidateRepository);
        List<Duplicate> duplicates = duplicateFinderService.getDuplicates(id, CandidateCheck.VALIDATED);
        boolean condition = false;
        for (Duplicate duplicate : duplicates) {
            condition = duplicate.getDuplicateType().equals(duplicateType);
            if (condition) {
                break;
            }
        }
        return condition;
    }

    public List<Candidate> getValidCandidates() {
        return candidateRepository.findAllByCheckCandidate(CandidateCheck.VALIDATED);
    }

    public List<Candidate> getNonValidCandidates() {
        return candidateRepository.findAllByCheckCandidate(CandidateCheck.NOT_YET_VALIDATED);
    }
}

