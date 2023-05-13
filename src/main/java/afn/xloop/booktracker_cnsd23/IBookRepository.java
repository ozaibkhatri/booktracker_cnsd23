package afn.xloop.booktracker_cnsd23;

import java.util.Collection;

public interface IBookRepository<T> {
    public void save(T t);
    public Collection<T> getAllBook();

    public void delete(T t);

    public void update(T t);
}