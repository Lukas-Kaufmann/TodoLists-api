package lukas.todolists

import java.time.LocalDateTime
import javax.persistence.Embedded
import javax.persistence.GeneratedValue
import javax.persistence.Id

class TaskDTO(
    var name: String,
    var completed: Boolean
) {
    fun toObjWithId(id: Long) = Task(id, this.name, this.completed)
}

class ListDTO(
    var title: String
)

class CreateListDTO(
    var title: String,
    var tasks: MutableList<TaskDTO>
) {
    fun toObjWithId(id: Long) : TodoList {
        var taskObjs = mutableListOf<Task>()
        this.tasks.forEachIndexed { index, task ->
            taskObjs.add(task.toObjWithId(index.toLong()))
        }

        return TodoList(id, this.title, taskObjs)
    }
}