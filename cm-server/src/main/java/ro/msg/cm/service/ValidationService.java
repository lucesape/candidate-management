package ro.msg.cm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Service;
import ro.msg.cm.exception.CandidateIsAlreadyValidated;
import ro.msg.cm.exception.CandidateNotFound;
import ro.msg.cm.model.Candidate;
import ro.msg.cm.repository.CandidateRepository;
import ro.msg.cm.types.CandidateCheck;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class ValidationService {

    private final CandidateRepository candidateRepository;

    @Autowired
    public ValidationService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public Candidate patchCandidate(Map<Object,Object> patchCandidate, Long id) {
        Candidate candidate = candidateRepository.findOne(id);
        if (candidate != null) {
            patchCandidate.forEach((k, v) -> {
                Field field = ReflectionUtils.findField(Candidate.class, String.valueOf(k));
                field.setAccessible(true);
                ReflectionUtils.setField(field, candidate, v);
                field.setAccessible(false);
            });
            return candidateRepository.save(candidate);
        } else {
            throw new CandidateNotFound();
        }
    }

    public void deleteSelectedEntry(Long id) {
        Candidate candidate = candidateRepository.findOne(id);
        if (candidate != null) {
            if (!candidate.getCheckCandidate().equals(CandidateCheck.VALIDATED) && candidate.getCheckCandidate() != null) {
                candidateRepository.delete(id);
            } else {
                throw new CandidateIsAlreadyValidated();
            }
        } else {
            throw new CandidateNotFound();
        }
    }

    public void deleteSelectedEntries(List<Long> ids) {
        for (Long id : ids) {
            if (candidateRepository.exists(id)) {
                candidateRepository.delete(id);
            }else {
                throw new CandidateNotFound("Candidate with id: " + id + " was not found");
            }
        }
    }

}

