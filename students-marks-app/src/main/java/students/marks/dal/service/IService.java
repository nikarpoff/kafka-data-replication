package students.marks.dal.service;


import students.marks.dal.exception.DatabaseException;

public interface IService<Entity> {

    Iterable<Entity> listAll();

    void delete(Long id) throws DatabaseException;

    Entity add(Entity entity) throws DatabaseException;

    Entity update(Entity entity) throws DatabaseException;

    Entity findById(Long id) throws DatabaseException;

}
