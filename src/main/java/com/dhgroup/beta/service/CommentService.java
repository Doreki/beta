package com.dhgroup.beta.service;

import com.dhgroup.beta.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    CommentRepository commentRepository;


//    public Long commentWrite() {
//        commentRepository.save(Com);
//    }
}
