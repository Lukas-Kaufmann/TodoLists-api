package lukas.todolists

import net.minidev.json.annotate.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.ElementCollection
import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id


@Entity
class TodoList(
    @Id @GeneratedValue var id: Long,
    var title: String,
    @ElementCollection
    var tasks: MutableList<Task>
)

@Embeddable
class Task(
    @JsonIgnore
    var id: Long,
    var name: String,
    var completed: Boolean,
)