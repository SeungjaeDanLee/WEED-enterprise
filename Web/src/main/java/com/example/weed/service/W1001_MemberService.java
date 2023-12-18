package com.example.weed.service;

import com.example.weed.authority.W1001_MemberAuthority;
import com.example.weed.dto.W1001_CustomDetails;
import com.example.weed.dto.W1001_MailDTO;
import com.example.weed.dto.W1001_UserSessionDto;
import com.example.weed.entity.Dept;
import com.example.weed.entity.File;
import com.example.weed.entity.Member;
import com.example.weed.repository.W1008_FileRepository;
import com.example.weed.repository.W1001_MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class W1001_MemberService implements UserDetailsService {


    private final HttpSession session;
    private final W1001_MemberRepository w1001MemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private final W1008_FileRepository w1008FileRepository;


    @Value("${cloud.aws.s3.bucket}")
    private String uploadPath;


    public Member W1001_getMemberInfo(String email) {
        return w1001MemberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("email"));
    }

    public W1001_MemberService(HttpSession session, W1001_MemberRepository w1001MemberRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, W1008_FileRepository w1008FileRepository) {
        this.session = session;
        this.w1001MemberRepository = w1001MemberRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.w1008FileRepository = w1008FileRepository;
    }

    public Member getLoggedInMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            // 사용자가 인증되지 않았거나 인증 정보가 없는 경우
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof W1001_CustomDetails) {

            W1001_CustomDetails w1001CustomDetails = (W1001_CustomDetails) principal;
            Member loggedInMember = w1001CustomDetails.getLoggedInMember();

            // 로그인한 사용자 정보가 있는 경우
            // 파일 리스트를 멤버와 연관시켜 저장
            File file = loggedInMember.getFile();
            loggedInMember.setFile(file);

            return loggedInMember;
        }

        return null;
    }

    public void save(Member.SaveRequest member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        w1001MemberRepository.save(member.toEntity());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = w1001MemberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("email"));
        Member loggedInMember = member;
        if (loggedInMember != null) {
            // 로그인한 사용자 정보가 있는 경우
            // 파일 리스트를 멤버와 연관시켜 저장
            File file = loggedInMember.getFile();

            if (file == null) {
                file = new File();
                String saveName = "default.png";
                file.setFileName(saveName);


            }
            loggedInMember.setFile(file);
            // 멤버를 저장하면서 파일 연관 업데이트
            w1001MemberRepository.save(loggedInMember);
        }

        W1001_UserSessionDto w1001UserSessionDto = new W1001_UserSessionDto(member);
        session.setAttribute("userSession", w1001UserSessionDto);

        return toUserDetails(member, loggedInMember);
    }

    private UserDetails toUserDetails(Member member, Member loggedInMember) {
        return new W1001_CustomDetails(
                member.getEmail(),
                member.getName(),
                member.getPassword(),
                member.getDept().getDeptName(),
                member.getAuthority().getName(),
                member.getFile().getFileName(),
                loggedInMember

        );
    }

    public boolean isEmailInUse(String email) {
        return w1001MemberRepository.existsByEmail(email);
    }

    public W1001_MailDTO createMailAndChangePassword(String memberEmail) {
        String tempPassword = getTempPassword();
        String encryptedPassword = passwordEncoder.encode(tempPassword); // 임시 비밀번호 암호화

        W1001_MailDTO dto = new W1001_MailDTO();
        dto.setAddress(memberEmail);
        dto.setTitle("Weed 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. Weed 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + tempPassword + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요");

        updatePassword(encryptedPassword, memberEmail); // 암호화된 임시 비밀번호로 업데이트

        return dto;
    }

    //임시 비밀번호로 업데이트
//    public void updatePassword(String str, String userEmail){
//        String memberPassword = str;
//        Long memberId = memberRepository.findByEmail(userEmail).get().getId();
//        memberRepository.updatePassword(memberId,memberPassword);
//    }

    //랜덤함수로 임시비밀번호 구문 만들기
    public String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    // 메일보내기
    public void mailSend(W1001_MailDTO w1001MailDTO) {
        System.out.println("전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(w1001MailDTO.getAddress());
        message.setSubject(w1001MailDTO.getTitle());
        message.setText(w1001MailDTO.getMessage());
        message.setFrom("comeday7741@gmail.com");
        message.setReplyTo("comeday7741@gmail.com");
        System.out.println("message" + message);
        mailSender.send(message);
    }

    //비밀번호 변경
    public void updatePassword(String str, String userEmail) {
        w1001MemberRepository.updatePassword(str, userEmail);

    }

    @Transactional
    public void updateMemberFiles(Member member) {
        member.setFile(w1008FileRepository.findByMemberId(member.getId()));
        w1001MemberRepository.save(member);
        w1001MemberRepository.updateId(member.getFile().getId(), member.getId());

    }

    public String getProfileImageName(Long memberId) {
        Member member = w1001MemberRepository.findById(memberId).orElse(null);
        if (member != null) {
            File file = member.getFile();
            if (file != null) {
                return file.getFileName();
            }
        }
        return null;
    }

    public void saveMember(Member loggedInMember) {
        w1001MemberRepository.save(loggedInMember);
    }

    public Member findById(Long memberId) {
        return w1001MemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 멤버를 찾을 수 없습니다."));
    }

    public void updateMemberAuthority(Long memberId, String authority) {
        Optional<Member> optionalMember = w1001MemberRepository.findById(memberId);
        optionalMember.ifPresent(member -> {
            member.setAuthority(W1001_MemberAuthority.valueOf(authority));
            w1001MemberRepository.save(member);
        });
    }

    public void updateDept(Long memberId, Long deptId) {
        Optional<Member> optionalMember = w1001MemberRepository.findById(memberId);

        optionalMember.ifPresent(member -> {
            // 부서 업데이트 로직을 구현
            // 예를 들어, 부서 엔터티를 새로 만들거나, 기존 부서 엔터티를 가져와서 설정
            Dept newDept = new Dept();
            newDept.setId(deptId);
            member.setDept(newDept);

            // 리파지토리를 사용하여 업데이트
            w1001MemberRepository.save(member);
        });

    }
    public void updateMemberDept(Long memberId, Long deptId) {
        Optional<Member> optionalMember = w1001MemberRepository.findById(memberId);

        optionalMember.ifPresent(member -> {
            // 부서 업데이트 로직을 구현
            // 예를 들어, 부서 엔터티를 새로 만들거나, 기존 부서 엔터티를 가져와서 설정
            Dept dept = new Dept();
            dept.setId(deptId);
            member.setDept(dept);

            // 리파지토리를 사용하여 업데이트
            w1001MemberRepository.save(member);
        });
    }

    public boolean isMemberExistByDeptId(Long deptId) {
        return w1001MemberRepository.existsByDeptId(deptId);
    }
}

