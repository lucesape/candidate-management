package ro.msg.cm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.msg.cm.model.Candidate;
import ro.msg.cm.service.ValidationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/candidateValidation")
public class CandidateValidationController {

    private final ValidationService validationService;

    @Autowired
    public CandidateValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PatchMapping("/updateCandidate/{id}")
    public @ResponseBody Candidate patchCandidate(@RequestBody Map<Object, Object> toPatch, @PathVariable Long id){
        return validationService.patchCandidate(toPatch, id);
    }

    @DeleteMapping("/deleteCandidate/{id}")
    public void deleteCandidate(@PathVariable Long id){
        validationService.deleteSelectedEntry(id);
    }

    @DeleteMapping("/deleteCandidates/{ids}")
    public void deleteCandidate(@PathVariable List<Long> ids){
        validationService.deleteSelectedEntries(ids);
    }

}
