package ro.msg.cm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Service;
import ro.msg.cm.model.Candidate;
import ro.msg.cm.repository.CandidateRepository;
import ro.msg.cm.types.CandidateCheck;

import java.lang.reflect.Field;
import java.util.Map;

@Service
public class ValidationService {

    private final CandidateRepository  candidateRepository;

    @Autowired
    public ValidationService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public void patchCandidate(Map<Object, Object> patchCandidate, Long id){
        Candidate candidate = candidateRepository.findOne(id);
           if(candidate!=null) {
               patchCandidate.forEach((k, v) -> {
                   Field field = ReflectionUtils.findField(Candidate.class, String.valueOf(k));
                   field.setAccessible(true);
                   ReflectionUtils.setField(field, candidate, v);
                   field.setAccessible(false);
               });
               candidateRepository.save(candidate);
           }else {
               throw new CandidateNotFound();
           }
    }

    public void deleteSelected(Long id){
        Candidate candidate = candidateRepository.findOne(id);
        if(candidate!=null){
            if(!candidate.getCheckCandidate().equals(CandidateCheck.VALIDATED)) {
                candidateRepository.delete(id);
            } else {
                throw new CandidateIsAlreadyValidated();
            }
        }else{
            throw new CandidateNotFound();
        }
    }
}
