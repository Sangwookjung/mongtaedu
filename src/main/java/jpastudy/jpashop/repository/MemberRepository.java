package jpastudy.jpashop.repository;

import jpastudy.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
//    @PersistenceContext //@Autowired 대체 가능 ->
//                  @RequiredArgsConstructor final 변수를 인자로 받는 생성자
    private final EntityManager em;

    //등록
    public void save(Member member) {

        em.persist(member);
    }

    //id로 Member 하나 조회
    public Member findOne(Long id) {

        return em.find(Member.class, id);
    }

    //Member 모두 조회, TypedQuery
    public List<Member> findAll() {

        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    //name으로 Member 조회, TypedQuery
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name",name).getResultList();
    }








}
