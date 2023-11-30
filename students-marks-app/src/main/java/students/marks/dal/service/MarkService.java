package students.marks.dal.service;

import org.springframework.stereotype.Service;
import students.marks.dal.repository.MarkRepository;
import students.marks.dal.exception.DatabaseException;
import students.marks.dal.model.Mark;
import students.marks.front.model.MarkTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MarkService implements IService<Mark> {
    private final MarkRepository repository;

    public MarkService(MarkRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<Mark> listAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long markId) throws DatabaseException {
        try {
            repository.deleteById(markId);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public Mark add(Mark mark) throws DatabaseException {
        try {
            return repository.save(mark);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public Mark update(Mark mark) throws DatabaseException {
        // FIRST - save() will check is mark new or not.
        // IF mark is new (primary key lab+student were not in DB), Hibernate will persist() new mark
        // IF mark already exists (PK exists), Hibernate will merge() data
        try {
            return repository.save(mark);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public Mark findById(Long id) throws DatabaseException {
        Optional<Mark> mark = repository.findById(id);

        if (mark.isEmpty()) {
            throw new DatabaseException("Mark not found");
        }

        return mark.get();
    }

    public List<MarkTable> findAllStudentsWithMarksOnOneSubject() throws DatabaseException {
        List<Object[]> marks = repository.findAllStudentsWithMarksOnOneSubject();

        if (marks == null || marks.isEmpty()) {
            throw new DatabaseException("No content in marks table");
        }

        List<MarkTable> castedMarks = new ArrayList<>(marks.size());

        for (Object[] mark : marks) {
            MarkTable castedMark = new MarkTable();
            castedMark.setStudentId((Long) mark[0]);
            castedMark.setLabNum((int) mark[1]);
            castedMark.setValue((int) mark[2]);
            castedMarks.add(castedMark);
        }

        return castedMarks;
    }

}
