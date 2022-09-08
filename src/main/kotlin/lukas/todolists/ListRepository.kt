package lukas.todolists

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service

@Service
interface ListRepository : CrudRepository<TodoList, Long> {

}