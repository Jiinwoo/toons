package day.toons.domain.alarm

import day.toons.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AlarmRepository : JpaRepository<Alarm, Long> {
    fun findByMemberAndWebtoonName(member: Member, name: String): Optional<Alarm>
    fun findAllByMember(member: Member): List<Alarm>
    fun findByMemberAndId(member: Member, id: Long): Optional<Alarm>
}