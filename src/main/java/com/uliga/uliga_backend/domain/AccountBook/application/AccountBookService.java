package com.uliga.uliga_backend.domain.AccountBook.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQuery.AccountBookInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.JoinTable.dao.AccountBookMemberRepository;
import com.uliga.uliga_backend.domain.JoinTable.model.AccountBookMember;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.InvitationInfo;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBookService {
    private final AccountBookRepository accountBookRepository;
    private final AccountBookMemberRepository accountBookMemberRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final CategoryRepository categoryRepository;
    @Transactional
    public GetAccountBookInfos getMemberAccountBook(Long id) {
        List<AccountBookInfoQ> accountBookInfosByMemberId = accountBookRepository.findAccountBookInfosByMemberId(id);

        return GetAccountBookInfos.builder()
                .accountBooks(accountBookInfosByMemberId).build();
    }
    @Transactional
    public AccountBookInfo createAccountBookPrivate(Long id, CreateRequestPrivate createRequest) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        AccountBook accountBook = createRequest.toEntity();
        accountBookRepository.save(accountBook);
        AccountBookMember bookMember = AccountBookMember.builder()
                .accountBook(accountBook)
                .member(member)
                .accountBookAuthority(AccountBookAuthority.ADMIN)
                .getNotification(true).build();
        accountBookMemberRepository.save(bookMember);
        return accountBook.toInfoDto();
    }
    @Transactional
    public AccountBookInfo createAccountBook(Long id, CreateRequest createRequest) throws JsonProcessingException {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        AccountBook accountBook = createRequest.toEntity();
        accountBookRepository.save(accountBook);
        AccountBookMember bookMember = AccountBookMember.builder()
                .accountBook(accountBook)
                .member(member)
                .accountBookAuthority(AccountBookAuthority.ADMIN)
                .getNotification(true).build();
        accountBookMemberRepository.save(bookMember);

        for (String category : createRequest.getCategories()) {
            Category newCategory = Category.builder()
                    .accountBook(accountBook)
                    .name(category)
                    .build();
            categoryRepository.save(newCategory);
        }
        for (String email : createRequest.getEmails()) {
            InvitationInfo info = InvitationInfo.builder()
                    .id(accountBook.getId())
                    .memberName(member.getUserName())
                    .accountBookName(accountBook.getName()).build();
            ListOperations<String, Object> valueOperations = redisTemplate.opsForList();
            valueOperations.rightPush(email, objectMapper.writeValueAsString(info));
        }

        return accountBook.toInfoDto();
    }


}
