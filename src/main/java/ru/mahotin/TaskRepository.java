package ru.mahotin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mahotin.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
