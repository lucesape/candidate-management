package ro.msg.cm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import ro.msg.cm.exception.CandidateIsAlreadyValidated;
import ro.msg.cm.exception.CandidateNotFound;
import ro.msg.cm.model.Candidate;
import ro.msg.cm.repository.CandidateRepository;
import ro.msg.cm.types.CandidateCheck;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
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

    public Candidate patchCandidate(Map<Object,Object> patchCandidate, Long id) {
        Candidate candidate = candidateRepository.findOne(id);
        if (candidate != null) {
            patchCandidate.forEach((k, v) -> {
                Method method = ReflectionUtils.findMethod(Candidate.class, "set" + StringUtils.capitalize(String.valueOf(k)));
                try {
                    method.invoke(candidate, v);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.debug("Message = " + e.getMessage() + " StackTrace = " + Arrays.toString(e.getStackTrace()));
                }
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

