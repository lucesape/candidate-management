/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.msg.cm.repository;

import org.springframework.data.repository.CrudRepository;
import ro.msg.cm.model.Candidate;
import ro.msg.cm.types.CandidateCheck;

import java.util.List;
import java.util.Set;

public interface CandidateRepository extends CrudRepository<Candidate, Long> {

    List<Candidate> findAllByCheckCandidate(CandidateCheck candidateCheck);

    Set<Candidate> findAllByFirstNameAndLastName(String firstName, String lastName);

    Set<Candidate> findAllByEmail(String email);

    Set<Candidate> findAllByPhone(String phone);
}
