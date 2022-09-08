package lukas.todolists

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
class ListController @Autowired constructor(var listRepository: ListRepository){

    @GetMapping("lists")
    fun getLists() = listRepository.findAll().toList()

    @GetMapping("lists/{id}")
    fun getList(@PathVariable id: Long): TodoList = listRepository.findById(id).orElseThrow {
            ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "list with id $id not found"
            )
        }

    @PostMapping("lists")
    @Transactional
    fun createList(@RequestBody listDTO: CreateListDTO) : TodoList {
        val nextId = listRepository.findAll().toList().stream().mapToLong { t -> t.id }.max().orElse(0) + 1
        var list = listDTO.toObjWithId(nextId)
        listRepository.save(list)
        return list
    }

    @DeleteMapping("lists/{id}")
    @Transactional
    fun deleteList(@PathVariable id: Long) {
        listRepository.deleteById(id)
    }

    @PutMapping("lists/{id}")
    @Transactional
    fun modifyList(@PathVariable id: Long, @RequestBody listDTO: ListDTO) : TodoList {
        var list = getList(id)
        list.title = listDTO.title
        return list
    }

    @PostMapping("lists/{id}/tasks")
    @Transactional
    fun addTask(@RequestBody taskDTO: TaskDTO, @PathVariable id: Long) : Task {
        val list = listRepository.findById(id).orElseThrow {
            ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "list with id $id not found"
            )
        }
        val nextId = list.tasks.stream().mapToLong { t -> t.id }.max().orElse(0) + 1
        val task = taskDTO.toObjWithId(nextId)
        list.tasks.add(task)
        return task
    }
    @GetMapping("lists/{id}/tasks/{taskId}")
    fun getTask(@PathVariable id: Long, @PathVariable taskId: Long): Task {
        val list = listRepository.findById(id).orElseThrow {
            ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "list with id $id not found"
            )
        }
        return list.tasks.stream().filter{t -> t.id == taskId}.findFirst().orElseThrow {
            ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "task with id $taskId not found"
            )
        }
    }

    @PutMapping("lists/{id}/tasks/{taskId}")
    @Transactional
    fun modifyTask(@PathVariable id: Long, @PathVariable taskId: Long, @RequestBody taskDTO: TaskDTO) : Task {
        val task = getTask(id, taskId)
        task.completed = taskDTO.completed
        task.name = taskDTO.name
        return task
    }

    @DeleteMapping("lists/{id}/tasks/{taskId}")
    @Transactional
    fun deleteTask(@PathVariable id: Long, @PathVariable taskId: Long) {
        val list = getList(id)
        list.tasks.removeIf { t -> t.id == taskId }
    }
}
